package com.moutamid.tiptop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.moutamid.tiptop.databinding.ActivityPaymentScreenBinding;
import com.moutamid.tiptop.utilis.Constants;

import java.util.HashMap;
import java.util.Map;


public class PaymentScreenActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    ActivityPaymentScreenBinding binding;
    boolean isLifetime = true;
    BillingProcessor bp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);

        binding.lifetime.setStrokeWidth(5);

        binding.back.setOnClickListener(v -> onBackPressed());

        bp = BillingProcessor.newBillingProcessor(this, Constants.LICENSE_KEY, this);
        bp.initialize();

        binding.subscribe.setOnClickListener(v -> {
            String subscribe = isLifetime ? Constants.LIFETIME : Constants.MONTHLY;
            bp.subscribe(PaymentScreenActivity.this, subscribe);
        });


        binding.month.setOnClickListener(v -> {
            isLifetime = false;

            binding.lifetime.setStrokeWidth(0);
            binding.month.setStrokeWidth(5);

            binding.lifetime.setCardElevation(0);
            binding.month.setCardElevation(6);

        });

        binding.lifetime.setOnClickListener(v -> {
            isLifetime = true;

            binding.lifetime.setStrokeWidth(5);
            binding.month.setStrokeWidth(0);

            binding.lifetime.setCardElevation(6);
            binding.month.setCardElevation(0);

        });


    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo details) {
        Map<String, Object> subscribe = new HashMap<>();
        subscribe.put("subscribed", true);
        subscribe.put("tipper", false);
        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid()).updateChildren(subscribe)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Subscribed to plan", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }
}