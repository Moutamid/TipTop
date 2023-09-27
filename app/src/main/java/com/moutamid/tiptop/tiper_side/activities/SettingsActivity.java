package com.moutamid.tiptop.tiper_side.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.moutamid.tiptop.MainActivity;
import com.moutamid.tiptop.PaymentScreenActivity;
import com.moutamid.tiptop.R;
import com.moutamid.tiptop.SplashScreenActivity;
import com.moutamid.tiptop.databinding.ActivitySettingsBinding;
import com.moutamid.tiptop.models.UserModel;
import com.moutamid.tiptop.receiver_side.activities.ReceiverDashboardActivity;
import com.moutamid.tiptop.utilis.Constants;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.initDialog(this);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.title.setText("Settings");

        binding.switchMode.setOnClickListener(v -> {
            if (binding.switchMode.isChecked()){
                if (!Stash.getBoolean(Constants.IS_VIP, false)) {
                    startActivity(new Intent(this, PaymentScreenActivity.class));
                } else {
                    changeMode();
                }
            }
        });

        binding.logout.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(this)
                    .setCancelable(true)
                    .setTitle("Logout")
                    .setMessage("Do you really want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dialog.dismiss();
                        Constants.auth().signOut();
                        startActivity(new Intent(this, SplashScreenActivity.class));
                        finish();
                    }).setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .create().show();
        });

    }

    private void changeMode() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.switchMode.setChecked(Stash.getBoolean(Constants.IS_MODE_CHANGE, false));

        if (Stash.getBoolean(Constants.IS_MODE_CHANGE, false)){
            binding.switchMode.setText("Switch to Tipper Mode");
        } else {
            binding.switchMode.setText("Switch to Receiver Mode");
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, TipperDashboardActivity.class));
        finish();
    }
}