package org.fr.ykatchou.paillardes;

import java.util.List;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Paillarde
 * Application sous GPL v3
 * @author ykatchou
 * Cette classe affiche la liste des chansons.
 */
public class PaillardeList extends AppCompatActivity {
    public static String filtre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paillarde_list);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            filtre = intent.getStringExtra(SearchManager.QUERY);
        } else {
            filtre = "";
        }

        List<Chanson> datalist = Chanson.dbhelp.getTitres(filtre);
        if (datalist.isEmpty()) {
            Toast.makeText(this, "Pas de résultats", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (datalist.size() == 1) {
            Chanson ch = datalist.get(0);
            Bundle b = new Bundle();
            b.putString(Chanson.Id, ch.get(Chanson.Id));
            Intent i = new Intent(this, PaillardeView.class);
            i.putExtra("data", b);
            startActivity(i);
            finish();
            return;
        }

        SimpleAdapter titres = new SimpleAdapter(this, datalist,
                R.layout.paillardelist,
                new String[]{Chanson.Titre, Chanson.Tags, Chanson.isMidi, Chanson.Id},
                new int[]{R.id.ch_titre, R.id.ch_tags, R.id.ch_midi});

        ListView listView = findViewById(R.id.list);
        listView.setAdapter(titres);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            List<Chanson> songs = Chanson.dbhelp.getTitres(filtre);
            if (position < songs.size()) {
                Chanson ch = songs.get(position);
                Bundle b = new Bundle();
                b.putString(Chanson.Id, ch.get(Chanson.Id));
                Intent i = new Intent(this, PaillardeView.class);
                i.putExtra("data", b);
                startActivity(i);
            } else {
                filtre = "";
            }
        });
    }
}
