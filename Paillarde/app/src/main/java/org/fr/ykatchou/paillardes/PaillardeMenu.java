package org.fr.ykatchou.paillardes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Paillarde
 * Application sous GPL v3
 * @author ykatchou
 * Cette classe affiche le menu principal.
 */
public class PaillardeMenu extends AppCompatActivity implements BillingManager.Listener {

    private static final String FALLBACK_URL = "https://ko-fi.com/ykatchou";

    private BillingManager billingManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Chanson.dbhelp = new DatabaseHelper(this);

        billingManager = new BillingManager(this);
        billingManager.setListener(this);
        billingManager.initialize();

        Button btnList = findViewById(R.id.btn_list);
        btnList.setOnClickListener(v -> startActivity(new Intent(this, PaillardeList.class)));

        Button btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(v -> onSearchRequested());

        Button btnAbout = findViewById(R.id.btn_about);
        btnAbout.setOnClickListener(v -> startActivity(new Intent(this, About.class)));

        Button btnBuyBeer = findViewById(R.id.btn_buybeer);
        btnBuyBeer.setOnClickListener(v -> {
            if (billingManager.getState() == BillingManager.State.READY) {
                billingManager.launchPurchase(this);
            } else {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(FALLBACK_URL)));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        billingManager.destroy();
    }

    @Override
    public void onPurchaseSuccess() {
        runOnUiThread(() ->
                Toast.makeText(this, "Merci pour votre soutien! 🍺", Toast.LENGTH_LONG).show());
    }

    @Override
    public void onPurchaseError() {
        runOnUiThread(() ->
                Toast.makeText(this, "Erreur de paiement. Essayez ko-fi.com!", Toast.LENGTH_LONG).show());
    }
}
