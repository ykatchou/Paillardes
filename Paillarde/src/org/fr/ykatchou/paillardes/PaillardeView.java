package org.fr.ykatchou.paillardes;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PaillardeView extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.paillardeview);

		Bundle b = getIntent().getBundleExtra("data");
		String Id = b.getString(Chanson.Id);

		Chanson c = Chanson.dbhelp.getChanson(Long.valueOf(Id));

		// / BINDINGS
		// ///////////////////////////////////////////////////////
		TextView tv = (TextView) findViewById(R.id.ch_titre);
		tv.setText(c.get(Chanson.Titre));

		tv = (TextView) findViewById(R.id.ch_paroles);
		tv.setText(c.get(Chanson.Paroles));

		Button btn = (Button) findViewById(R.id.btn_retour);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
