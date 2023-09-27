package com.moutamid.tiptop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.moutamid.tiptop.databinding.ActivityMainBinding;
import com.moutamid.tiptop.receiver_side.activities.ReceiverSignupActivity;
import com.moutamid.tiptop.tiper_side.activities.SignUpTiperActivity;
import com.moutamid.tiptop.utilis.Constants;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.checkApp(this);

        binding.tipper.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpTiperActivity.class));
        });

        binding.receiver.setOnClickListener(v -> {
            startActivity(new Intent(this, ReceiverSignupActivity.class));
        });

    }
}