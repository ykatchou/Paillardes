package org.fr.ykatchou.paillardes;

import java.io.FileDescriptor;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Paillarde Application sous GPL v3
 * 
 * @author ykatchou Cettte classe affiche les paroles d'une chanson.
 */
public class PaillardeView extends Activity {
	private Chanson tmp_chanson;
	private MediaPlayer mp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.paillardeview);

		Bundle b = getIntent().getBundleExtra("data");
		String Id = b.getString(Chanson.Id);

		tmp_chanson = Chanson.dbhelp.getChanson(Long.valueOf(Id));

		bind_data();
		bind_button_play_midi();
		bind_button_retour();
		bind_button_site_web();

		String midi_file = tmp_chanson.get(Chanson.Midi);
		int midi_id = 0;
		if (midi_file != null && midi_file != "") {
			// Load MIDI
			midi_id = getResources().getIdentifier(midi_file, "raw",
					"org.fr.ykatchou.paillardes");

			if (midi_id != 0) {
				mp = MediaPlayer.create(this, midi_id);
			}
		}

		Button btn = (Button) findViewById(R.id.btn_play_midi);
		if (midi_id == 0) {
			btn.setVisibility(View.GONE);
		} else {
			btn.setVisibility(View.VISIBLE);
		}
	}

	// / BINDINGS
	// ///////////////////////////////////////////////////////

	public void bind_data() {
		TextView tv = (TextView) findViewById(R.id.ch_titre);
		tv.setText(tmp_chanson.get(Chanson.Titre));

		tv = (TextView) findViewById(R.id.ch_paroles);
		tv.setText(tmp_chanson.get(Chanson.Paroles));
	}

	public void bind_button_play_midi() {
		Button btn = (Button) findViewById(R.id.btn_play_midi);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
			}
		});
	}

	public void bind_button_retour() {
		Button btn = (Button) findViewById(R.id.btn_retour);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void bind_button_site_web() {
		String url = tmp_chanson.get(Chanson.url);
		Button btn = (Button) findViewById(R.id.btn_site_web);
		if (url != null && url != "") {
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String url = tmp_chanson.get(Chanson.url);
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(i);
				}
			});
		} else {
			btn.setEnabled(false);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mp != null) {
			mp.pause();
			((Button) findViewById(R.id.btn_play_midi)).setText(R.string.btn_play);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mp!=null){
			mp.release();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		if (tmp_chanson != null) {
			String url = tmp_chanson.get(Chanson.url);
			if (url != null && url != "") {
				menu.add(R.string.menu_url);
			}
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String url = tmp_chanson.get(Chanson.url);
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(i);
		finish();

		return super.onOptionsItemSelected(item);
	}
}
