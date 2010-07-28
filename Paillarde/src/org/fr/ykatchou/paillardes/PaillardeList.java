package org.fr.ykatchou.paillardes;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class PaillardeList extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle b = getIntent().getBundleExtra("data");
		String filter = b.getString("filter");

		List<Chanson> datalist = Chanson.dbhelp.getTitres(filter);
		if (datalist.size() == 0) {
			Toast.makeText(this, "Pas de résultats", Toast.LENGTH_LONG).show();
		} else {
			if (datalist.size() == 1) {
				Chanson ch = Chanson.dbhelp.getTitres(filter).get(0);
				Intent i = new Intent(this, PaillardeView.class);
				b.putString(Chanson.Id, ch.get(Chanson.Id));
				i.putExtra("data", b);
				startActivity(i);

			} else {
				SimpleAdapter titres = new SimpleAdapter(this, datalist,
						R.layout.paillardelist, new String[] { Chanson.Titre },
						new int[] { R.id.ch_titre });
				this.setListAdapter(titres);
			}
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(v.getContext(), PaillardeView.class);
		Bundle b = getIntent().getBundleExtra("data");
		String filter = b.getString("filter");

		Chanson ch = Chanson.dbhelp.getTitres(filter).get(position);
		b.putString(Chanson.Id, ch.get(Chanson.Id));
		i.putExtra("data", b);
		startActivity(i);
	}
}
