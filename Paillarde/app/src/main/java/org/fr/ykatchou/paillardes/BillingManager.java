package org.fr.ykatchou.paillardes;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;

import java.util.Collections;
import java.util.List;

/**
 * Manages in-app purchases via Google Play Billing.
 * Mirrors the approach used in ylauncher: single consumable product "tip_coffee",
 * with ko-fi.com fallback when billing is unavailable.
 */
public class BillingManager implements PurchasesUpdatedListener {

    private static final String PRODUCT_ID = "tip_coffee";

    public enum State { DISCONNECTED, CONNECTING, READY, UNAVAILABLE }

    public interface Listener {
        void onPurchaseSuccess();
        void onPurchaseError();
    }

    private final Context context;
    private BillingClient billingClient;
    private ProductDetails productDetails;
    private Listener listener;
    private State state = State.DISCONNECTED;

    public BillingManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public State getState() {
        return state;
    }

    public void initialize() {
        state = State.CONNECTING;
        billingClient = BillingClient.newBuilder(context)
                .setListener(this)
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult result) {
                if (result.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    queryProducts();
                } else {
                    state = State.UNAVAILABLE;
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                state = State.DISCONNECTED;
            }
        });
    }

    private void queryProducts() {
        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(Collections.singletonList(
                        QueryProductDetailsParams.Product.newBuilder()
                                .setProductId(PRODUCT_ID)
                                .setProductType(BillingClient.ProductType.INAPP)
                                .build()))
                .build();

        billingClient.queryProductDetailsAsync(params, (result, detailsList) -> {
            if (result.getResponseCode() == BillingClient.BillingResponseCode.OK
                    && !detailsList.isEmpty()) {
                productDetails = detailsList.get(0);
                state = State.READY;
            } else {
                state = State.UNAVAILABLE;
            }
        });
    }

    public void launchPurchase(Activity activity) {
        if (productDetails == null || state != State.READY) return;

        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(Collections.singletonList(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails)
                                .build()))
                .build();

        billingClient.launchBillingFlow(activity, flowParams);
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult result, List<Purchase> purchases) {
        if (result.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                consumePurchase(purchase);
            }
        } else if (result.getResponseCode() != BillingClient.BillingResponseCode.USER_CANCELED) {
            if (listener != null) listener.onPurchaseError();
        }
    }

    private void consumePurchase(Purchase purchase) {
        ConsumeParams params = ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken())
                .build();

        billingClient.consumeAsync(params, (result, token) -> {
            if (result.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                if (listener != null) listener.onPurchaseSuccess();
            } else {
                if (listener != null) listener.onPurchaseError();
            }
        });
    }

    public void destroy() {
        if (billingClient != null) {
            billingClient.endConnection();
        }
    }
}
