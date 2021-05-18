package org.fr.ykatchou.paillardes;

import android.app.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
/**
 * Paillarde
 * Application sous GPL v3
 * @author ykatchou
 * Cettte classe contient le a propos.
 */
public class About extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.about);
		
		bind_btn_buy_beer();
	}
	
	protected void bind_btn_buy_beer() {
		Button btn_send_mail = (Button) findViewById(R.id.btn_buybeer);

		btn_send_mail.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String Address = "https://www.buymeacoffee.com/ykatchou";
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Address));
				startActivity(intent);
			}
		});
	}
}
