package org.fr.ykatchou.paillardes;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Paillarde
 * Application sous GPL v3
 *
 * @author ykatchou Cette classe affiche les paroles d'une chanson.
 */
public class PaillardeView extends AppCompatActivity {
    private Chanson tmp_chanson;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paillardeview);

        Bundle b = getIntent().getBundleExtra("data");
        if (b == null) {
            finish();
            return;
        }
        String id = b.getString(Chanson.Id);
        tmp_chanson = Chanson.dbhelp.getChanson(Long.valueOf(id));

        bind_data();
        bind_button_retour();
        bind_button_site_web();

        String midi_file = tmp_chanson.get(Chanson.Midi);
        int midi_id = 0;
        if (midi_file != null && !midi_file.isEmpty()) {
            midi_id = getResources().getIdentifier(midi_file, "raw", "org.fr.ykatchou.paillardes");
            if (midi_id != 0) {
                mp = MediaPlayer.create(this, midi_id);
            }
        }

        Button btn = findViewById(R.id.btn_play_midi);
        if (midi_id == 0) {
            btn.setVisibility(View.GONE);
        } else {
            btn.setVisibility(View.VISIBLE);
            bind_button_play_midi(btn);
        }
    }

    public void bind_data() {
        TextView tv = findViewById(R.id.ch_titre);
        tv.setText(tmp_chanson.get(Chanson.Titre));

        tv = findViewById(R.id.ch_paroles);
        tv.setText(tmp_chanson.get(Chanson.Paroles));
    }

    public void bind_button_play_midi(Button btn) {
        btn.setOnClickListener(v -> {
            if (mp != null) {
                if (mp.isPlaying()) {
                    mp.pause();
                    ((Button) v).setText(R.string.btn_play);
                } else {
                    mp.setLooping(true);
                    mp.seekTo(0);
                    mp.start();
                    ((Button) v).setText(R.string.stop);
                }
            }
        });
    }

    public void bind_button_retour() {
        Button btn = findViewById(R.id.btn_retour);
        btn.setOnClickListener(v -> finish());
    }

    public void bind_button_site_web() {
        String url = tmp_chanson.get(Chanson.url);
        Button btn = findViewById(R.id.btn_site_web);
        if (url != null && !url.isEmpty()) {
            btn.setOnClickListener(v -> {
                String siteUrl = tmp_chanson.get(Chanson.url);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(siteUrl)));
            });
        } else {
            btn.setEnabled(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mp != null && mp.isPlaying()) {
            mp.pause();
            Button btn = findViewById(R.id.btn_play_midi);
            if (btn != null) btn.setText(R.string.btn_play);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (tmp_chanson != null) {
            String url = tmp_chanson.get(Chanson.url);
            if (url != null && !url.isEmpty()) {
                menu.add(R.string.menu_url);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String url = tmp_chanson.get(Chanson.url);
        if (url != null && !url.isEmpty()) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
