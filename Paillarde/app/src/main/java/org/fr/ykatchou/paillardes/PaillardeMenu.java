package org.fr.ykatchou.paillardes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

/**
 * Paillarde
 * Application sous GPL v3
 * @author ykatchou
 * Cettte classe affiche le menu principal.
 */
public class PaillardeMenu extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);

		Chanson.dbhelp = new DatabaseHelper(this);

		// / BINDINGS
		// //////////////////////////////////////////////////

		Button b = (Button) findViewById(R.id.btn_list);
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), PaillardeList.class);
				startActivity(i);
			}
		});

		b = (Button) findViewById(R.id.btn_search);
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onSearchRequested();
			}
		});

		b = (Button) findViewById(R.id.btn_about);
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), About.class);
				startActivity(i);
			}
		});
	}
}