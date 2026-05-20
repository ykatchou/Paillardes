package org.fr.ykatchou.paillardes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;

/**
 * Paillarde
 * Application sous GPL v3
 *
 * @author ykatchou Cette classe affiche les paroles d'une chanson.
 */
public class PaillardeView extends AppCompatActivity {

    private static final String PREF_NAME = "paillardes";
    private static final String PREF_FONT_SIZE = "pref_font_size";
    private static final int FONT_DEFAULT = 18;
    private static final int FONT_MIN = 10;
    private static final int FONT_MAX = 40;
    private static final int FONT_STEP = 2;

    private Chanson tmp_chanson;
    private MediaPlayer mp;
    private int currentFontSize;
    private TextView tvParoles;
    private TextView tvFontSize;

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

        // Toolbar with back navigation
        Toolbar toolbar = findViewById(R.id.toolbar_view);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(tmp_chanson.get(Chanson.Titre));
        }

        tvParoles = findViewById(R.id.ch_paroles);
        tvFontSize = findViewById(R.id.tv_font_size);

        bind_data();
        bind_button_site_web();

        // Font size
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        currentFontSize = prefs.getInt(PREF_FONT_SIZE, FONT_DEFAULT);
        applyFontSize();
        bindFontSizeControls(prefs);

        // MIDI
        String midi_file = tmp_chanson.get(Chanson.Midi);
        int midi_id = 0;
        if (midi_file != null && !midi_file.isEmpty()) {
            midi_id = getResources().getIdentifier(midi_file, "raw", "org.fr.ykatchou.paillardes");
            if (midi_id != 0) {
                mp = MediaPlayer.create(this, midi_id);
            }
        }

        MaterialButton btnMidi = findViewById(R.id.btn_play_midi);
        if (midi_id == 0) {
            btnMidi.setVisibility(View.GONE);
        } else {
            btnMidi.setVisibility(View.VISIBLE);
            bind_button_play_midi(btnMidi);
        }
    }

    private void applyFontSize() {
        tvParoles.setTextSize(TypedValue.COMPLEX_UNIT_SP, currentFontSize);
        tvFontSize.setText(String.valueOf(currentFontSize));
    }

    private void bindFontSizeControls(SharedPreferences prefs) {
        MaterialButton btnDecrease = findViewById(R.id.btn_font_decrease);
        MaterialButton btnIncrease = findViewById(R.id.btn_font_increase);

        btnDecrease.setOnClickListener(v -> {
            if (currentFontSize > FONT_MIN) {
                currentFontSize = Math.max(currentFontSize - FONT_STEP, FONT_MIN);
                applyFontSize();
                prefs.edit().putInt(PREF_FONT_SIZE, currentFontSize).apply();
            }
        });

        btnIncrease.setOnClickListener(v -> {
            if (currentFontSize < FONT_MAX) {
                currentFontSize = Math.min(currentFontSize + FONT_STEP, FONT_MAX);
                applyFontSize();
                prefs.edit().putInt(PREF_FONT_SIZE, currentFontSize).apply();
            }
        });
    }

    public void bind_data() {
        TextView tv = findViewById(R.id.ch_titre);
        tv.setText(tmp_chanson.get(Chanson.Titre));
        tvParoles.setText(tmp_chanson.get(Chanson.Paroles));
    }

    public void bind_button_play_midi(Button btn) {
        btn.setOnClickListener(v -> {
            if (mp != null) {
                if (mp.isPlaying()) {
                    mp.pause();
                } else {
                    mp.setLooping(true);
                    mp.seekTo(0);
                    mp.start();
                }
            }
        });
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        String url = tmp_chanson.get(Chanson.url);
        if (url != null && !url.isEmpty()) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        if (tmp_chanson != null) {
            String url = tmp_chanson.get(Chanson.url);
            if (url != null && !url.isEmpty()) {
                menu.add(R.string.menu_url);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }
}
