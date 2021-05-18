package org.fr.ykatchou.paillardes;

import java.util.List;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * Paillarde
 * Application sous GPL v3
 * @author ykatchou
 * Cettte classe affiche la liste des chansons.
 */
public class PaillardeList extends ListActivity {
	public static String filtre;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			filtre = intent.getStringExtra(SearchManager.QUERY);
		} else {
			filtre = "";
		}

		List<Chanson> datalist = Chanson.dbhelp.getTitres(filtre);
		if (datalist.size() == 0) {
			Toast.makeText(this, "Pas de résultats", Toast.LENGTH_LONG).show();
			finish();
		} else {
			if (datalist.size() == 1) {
				Chanson ch = datalist.get(0);
				Intent i = new Intent(this, PaillardeView.class);
				Bundle b = new Bundle();
				b.putString(Chanson.Id, ch.get(Chanson.Id));
				i.putExtra("data", b);
				startActivity(i);
				finish();
			} else {
				SimpleAdapter titres = new SimpleAdapter(this, datalist,
						R.layout.paillardelist, new String[] { Chanson.Titre,
								Chanson.Tags, Chanson.isMidi, Chanson.Id}, new int[] { R.id.ch_titre, R.id.ch_tags, R.id.ch_midi });
				this.setListAdapter(titres);
			}
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Bundle b = new Bundle();
		Intent i = new Intent(v.getContext(), PaillardeView.class);

		if(position < Chanson.dbhelp.getTitres(filtre).size()){
			Chanson ch = Chanson.dbhelp.getTitres(filtre).get(position);
			b.putString(Chanson.Id, ch.get(Chanson.Id));

			i.putExtra("data", b);
			startActivity(i);
		}else{
			filtre = "";
		}
	}
}
