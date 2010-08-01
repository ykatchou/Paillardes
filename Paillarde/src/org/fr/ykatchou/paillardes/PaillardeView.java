package org.fr.ykatchou.paillardes;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PaillardeView extends Activity {
	private Chanson tmp_chanson;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.paillardeview);

		Bundle b = getIntent().getBundleExtra("data");
		String Id = b.getString(Chanson.Id);

		tmp_chanson = Chanson.dbhelp.getChanson(Long.valueOf(Id));

		// / BINDINGS
		// ///////////////////////////////////////////////////////
		TextView tv = (TextView) findViewById(R.id.ch_titre);
		tv.setText(tmp_chanson.get(Chanson.Titre));

		tv = (TextView) findViewById(R.id.ch_paroles);
		tv.setText(tmp_chanson.get(Chanson.Paroles));

		Button btn = (Button) findViewById(R.id.btn_retour);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		if(tmp_chanson != null){
			String url =tmp_chanson.get(Chanson.url); 
			if(url != null && url != ""){
				menu.add(R.string.menu_url);
			}
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String url =tmp_chanson.get(Chanson.url);
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(i);
		finish();
		
		return super.onOptionsItemSelected(item);
	}
}
