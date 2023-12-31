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
import com.moutamid.tiptop.receiver_side.activities.AddBankReceiverActivity;
import com.moutamid.tiptop.receiver_side.activities.ReceiverDashboardActivity;
import com.moutamid.tiptop.utilis.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;
    UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.initDialog(this);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.title.setText("Settings");

        binding.switchMode.setOnClickListener(v -> {
            if (!binding.switchMode.isChecked()) {
                if (!userModel.isSubscribed()) {
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

        binding.editProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, EditProfileActivity.class));
        });
        binding.bank.setOnClickListener(v -> {
            startActivity(new Intent(this, AddBankTipperActivity.class));
        });

    }

    private void changeMode() {
        Constants.showDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("tipper", false);
        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .updateChildren(map)
                .addOnSuccessListener(unused -> {
                    userModel.setTipper(false);
                    Stash.put(Constants.STASH_USER, userModel);
                    Constants.dismissDialog();
                    startActivity(new Intent(this, ReceiverDashboardActivity.class));
                    finish();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);
        binding.switchMode.setChecked(userModel.isTipper());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, TipperDashboardActivity.class));
        finish();
    }
}