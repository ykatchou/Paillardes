package org.fr.ykatchou.paillardes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Paillarde
 * Application sous GPL v3
 * @author ykatchou
 * Cettte classe aide à l'accès à la base de données.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	// The Android's default system path of your application database.
	private static String DB_PATH = "/data/data/org.fr.ykatchou.paillardes/databases/";
	private static String DB_NAME = "paillardes.db";
	private SQLiteDatabase myDatabase;
	private final Context myContext;

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.myContext = context;

		try {
			this.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void close() {
		if (myDatabase != null)
			myDatabase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	private void createDataBase() throws IOException {
		boolean dbExist = checkDatabase();
		if (dbExist) {
			// do nothing - database already exist
		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();
			try {

				copyDataBase();

			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	private boolean checkDatabase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	private void copyDataBase() throws IOException {
		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;
		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);
		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	private void openDataBase() throws SQLException {
		// Open the database
		String myPath = DB_PATH + DB_NAME;
		myDatabase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);
	}

	public void init() throws IOException {
		if (!checkDatabase())
			createDataBase();
		copyDataBase();
		openDataBase();
	}

	public List<Chanson> getTitres() {
		List<Chanson> data = new LinkedList<Chanson>();
		Set<Long> ids = new TreeSet<Long>();
		Long tmp_id;

		String allTitresQuery = "select ch.id, ch.titre,t.value as tags";
		allTitresQuery += " from chanson ch join chansontag cht on ch.id = cht.chanson_id";
		allTitresQuery += " join tag t on t.id = cht.tag_id order by ch.titre, tags";
		
		Cursor d = myDatabase.rawQuery(allTitresQuery, null);
		while (d.moveToNext()) {
			tmp_id = d.getLong(0);
			//Si l'on a déjà ajouté un des tags...
			if(!ids.contains(tmp_id)){
				Chanson c = new Chanson(tmp_id, d.getString(1));
				c.addTags(d.getString(2));
				ids.add(tmp_id);
				data.add(c);
			}else{
				//Recherche de la chanson dans la liste
				for(Chanson c : data){
					if(c.private_id.equals(tmp_id)){
						c.addTags(d.getString(2));
						break;
					}
				}
			}
		}
		return data;
	}

	public List<Chanson> getTitres(String filter) {
		if (filter == null || filter == "")
			return getTitres();

		List<Chanson> data = new LinkedList<Chanson>();

		String allTitresQuery = "select ch.id, ch.titre,t.value as tags";
		allTitresQuery += " from chanson ch join chansontag cht on ch.id = cht.chanson_id";
		allTitresQuery += " join tag t on t.id = cht.tag_id";
		allTitresQuery += " where ch.titre like ? or ch.paroles like ? ";
		allTitresQuery += " order by ch.titre, tags";
		String[] params = new String[2];

		params[0] = "%" + filter + "%";
		params[1] = "%" + filter + "%";

		Cursor d = myDatabase.rawQuery(allTitresQuery, params);

		Set<Long> ids = new TreeSet<Long>();
		Long tmp_id;
		
		while (d.moveToNext()) {
			tmp_id = d.getLong(0);
			if(!ids.contains(tmp_id)){
				Chanson c = new Chanson(tmp_id, d.getString(1));
				c.addTags(d.getString(2));
				ids.add(tmp_id);
				data.add(c);
			}else
			{
				//Recherche de la chanson dans la liste
				for(Chanson c : data){
					if(c.private_id.equals(tmp_id)){
						c.addTags(d.getString(2));
						break;
					}
				}	
			}
		}
		return data;
	}

	public Chanson getChanson(Long id) {
		Chanson data = new Chanson();
		String[] params = new String[1];
		
		String getChansonQuery = "select ch.id, ch.titre, ch.paroles, ch.url, t.value";
		getChansonQuery +=" from chanson ch join chansontag cht on cht.chanson_id = ch.id";
		getChansonQuery +=" join tag t on t.id = cht.tag_id where ch.id = ? ";
		
		params[0] = String.valueOf(id);
		Cursor d = myDatabase.rawQuery(getChansonQuery, params);

		while (d.moveToNext()) {
			if(!data.containsKey(Chanson.Id)){
				data.setId(d.getLong(0));
				data.put(Chanson.Titre, d.getString(1));
				data.put(Chanson.Paroles, d.getString(2));
				data.put(Chanson.url, d.getString(3));
			}
			data.addTags(d.getString(4));
		}
		return data;
	}

	public Long getChansonCount() {
		String getCountQuery = "select count(*) from chanson";
		Cursor d = myDatabase.rawQuery(getCountQuery, null);

		if (d.moveToNext())
			return d.getLong(0);

		return Long.valueOf(0);
	}
}
