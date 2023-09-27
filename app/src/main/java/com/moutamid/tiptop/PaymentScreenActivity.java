package com.moutamid.tiptop;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.moutamid.tiptop.databinding.ActivityPaymentScreenBinding;

public class PaymentScreenActivity extends AppCompatActivity {
    ActivityPaymentScreenBinding binding;

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

        binding.subscribe.setOnClickListener(v -> {

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

}