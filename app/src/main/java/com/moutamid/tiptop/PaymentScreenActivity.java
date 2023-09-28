package com.moutamid.tiptop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.moutamid.tiptop.databinding.ActivityPaymentScreenBinding;
import com.moutamid.tiptop.utilis.ChargeService;
import com.moutamid.tiptop.utilis.OrderSheet;


public class PaymentScreenActivity extends AppCompatActivity {
    ActivityPaymentScreenBinding binding;
    private OrderSheet orderSheet;
    boolean isLifetime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);

        binding.lifetime.setStrokeWidth(5);

        binding.back.setOnClickListener(v -> onBackPressed());

/*
        OkHttpClient squareOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new SquareAuthorizationInterceptor("YOUR_ACCESS_TOKEN"))
                .build();

        Retrofit squareRetrofit = new Retrofit.Builder()
                .baseUrl("https://connect.squareup.com")
                .client(squareOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
*/


        binding.subscribe.setOnClickListener(v -> {
//            if (InAppPaymentsSdk.INSTANCE.getSquareApplicationId().equals(getString(R.string.APPLICATION_ID))) {
//                showMissingSquareApplicationIdDialog();
//            } else {
//                showOrderSheet();
//            }

    //        CardEntry.startCardEntryActivity(PaymentScreenActivity.this, true, 100);


        });

        orderSheet = new OrderSheet();
        orderSheet.setOnPayWithCardClickListener(this::startCardEntryActivity);


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

    private void startCardEntryActivity() {
//        CardEntry.startCardEntryActivity(this);
    }

    private void showMissingSquareApplicationIdDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Missing Application ID")
                .setMessage("Missing Application ID")
                .setPositiveButton(android.R.string.ok, (dialog, which) -> showOrderSheet())
                .show();
    }

    private void showOrderSheet() {
        orderSheet.show(PaymentScreenActivity.this);
    }

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CardEntry.handleActivityResult(data, cardEntryActivityResult -> {
            if (cardEntryActivityResult.isSuccess()) {
                if (!ConfigHelper.serverHostSet()) {
                    showServerHostNotSet();
                } else {
                    showSuccessCharge();
                }
            } else if (cardEntryActivityResult.isCanceled()) {
                Resources res = getResources();
                int delayMs = res.getInteger(R.integer.card_entry_activity_animation_duration_ms);
                handler.postDelayed(this::showOrderSheet, delayMs);
            }
        });
    }*/

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/*        CardEntry.handleActivityResult(data, result -> {
            if (result.isSuccess()) {
                CardDetails cardResult = result.getSuccessValue();
                Card card = cardResult.getCard();
                String nonce = cardResult.getNonce();
            } else if (result.isCanceled()) {
                Toast.makeText(PaymentScreenActivity.this,
                                "Canceled",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });*/
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        orderSheet.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        orderSheet.onRestoreInstanceState(this, savedInstanceState);
    }

}