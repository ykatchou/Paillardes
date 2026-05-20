package org.fr.ykatchou.paillardes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Paillarde
 * Application sous GPL v3
 * @author ykatchou
 * Cette classe contient le à propos.
 */
public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        Button btnBuyBeer = findViewById(R.id.btn_buybeer);
        btnBuyBeer.setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://ko-fi.com/ykatchou"))));
    }
}
