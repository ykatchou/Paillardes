package org.fr.ykatchou.paillardes;

import android.app.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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

		setContentView(R.layout.about);
		
		bind_btn_send_mail();
	}
	
	protected void bind_btn_send_mail() {
		Button btn_send_mail = (Button) findViewById(R.id.btn_mail);

		btn_send_mail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String mailAdress = "mailto:ykatchou@gmail.com?subject=Application%20Android%20-%20Paillardes";
				Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(mailAdress));
				startActivity(intent);
			}
		});
	}
}
