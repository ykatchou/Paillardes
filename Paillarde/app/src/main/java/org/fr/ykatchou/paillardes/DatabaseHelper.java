package org.fr.ykatchou.paillardes;

import java.io.File;
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
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Paillarde
 * Application sous GPL v3
 * @author ykatchou
 * Cette classe aide à l'accès à la base de données.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "paillardes.db";
    private SQLiteDatabase myDatabase;
    private final Context myContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;

        try {
            this.init();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize database", e);
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

    private boolean checkDatabase() {
        return myContext.getDatabasePath(DB_NAME).exists();
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        File dbFile = myContext.getDatabasePath(DB_NAME);
        dbFile.getParentFile().mkdirs();
        OutputStream myOutput = new FileOutputStream(dbFile);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        myDatabase = SQLiteDatabase.openDatabase(
                myContext.getDatabasePath(DB_NAME).getAbsolutePath(),
                null,
                SQLiteDatabase.OPEN_READWRITE);
    }

    public void init() throws IOException {
        if (!checkDatabase()) {
            copyDataBase();
        }
        openDataBase();
    }

    public List<Chanson> getTitres() {
        List<Chanson> data = new LinkedList<>();
        Set<Long> ids = new TreeSet<>();

        String query = "select ch.id, ch.titre, t.value as tags, ch.midi"
                + " from chanson ch join chansontag cht on ch.id = cht.chanson_id"
                + " join tag t on t.id = cht.tag_id order by ch.titre, tags";

        Cursor d = myDatabase.rawQuery(query, null);
        try {
            while (d.moveToNext()) {
                long tmp_id = d.getLong(0);
                if (!ids.contains(tmp_id)) {
                    Chanson c = new Chanson(tmp_id, d.getString(1));
                    ChansonHelper.GenerateMidi(c, d.getString(3));
                    c.addTags(d.getString(2));
                    ids.add(tmp_id);
                    data.add(c);
                } else {
                    for (Chanson c : data) {
                        if (c.private_id.equals(tmp_id)) {
                            c.addTags(d.getString(2));
                            break;
                        }
                    }
                }
            }
        } finally {
            d.close();
        }
        return data;
    }

    public List<Chanson> getTitres(String filter) {
        if (filter == null || filter.isEmpty())
            return getTitres();

        List<Chanson> data = new LinkedList<>();

        String query = "select ch.id, ch.titre, t.value as tags, ch.midi"
                + " from chanson ch join chansontag cht on ch.id = cht.chanson_id"
                + " join tag t on t.id = cht.tag_id"
                + " where ch.titre like ? or ch.paroles like ?"
                + " order by ch.titre, tags";
        String[] params = {"%" + filter + "%", "%" + filter + "%"};

        Cursor d = myDatabase.rawQuery(query, params);
        Set<Long> ids = new TreeSet<>();

        try {
            while (d.moveToNext()) {
                long tmp_id = d.getLong(0);
                if (!ids.contains(tmp_id)) {
                    Chanson c = new Chanson(tmp_id, d.getString(1));
                    ChansonHelper.GenerateMidi(c, d.getString(3));
                    c.addTags(d.getString(2));
                    ids.add(tmp_id);
                    data.add(c);
                } else {
                    for (Chanson c : data) {
                        if (c.private_id.equals(tmp_id)) {
                            c.addTags(d.getString(2));
                            break;
                        }
                    }
                }
            }
        } finally {
            d.close();
        }
        return data;
    }

    public Chanson getChanson(Long id) {
        Chanson data = new Chanson();

        String query = "select ch.id, ch.titre, ch.paroles, ch.url, t.value, ch.midi"
                + " from chanson ch join chansontag cht on cht.chanson_id = ch.id"
                + " join tag t on t.id = cht.tag_id where ch.id = ?";
        String[] params = {String.valueOf(id)};

        Cursor d = myDatabase.rawQuery(query, params);
        try {
            while (d.moveToNext()) {
                if (!data.containsKey(Chanson.Id)) {
                    data.setId(d.getLong(0));
                    data.put(Chanson.Titre, d.getString(1));
                    data.put(Chanson.Paroles, d.getString(2));
                    data.put(Chanson.url, d.getString(3));
                    ChansonHelper.GenerateMidi(data, d.getString(5));
                }
                data.addTags(d.getString(4));
            }
        } finally {
            d.close();
        }
        return data;
    }

    public Long getChansonCount() {
        Cursor d = myDatabase.rawQuery("select count(*) from chanson", null);
        try {
            if (d.moveToNext())
                return d.getLong(0);
        } finally {
            d.close();
        }
        return 0L;
    }
}
