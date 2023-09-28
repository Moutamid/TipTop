package com.moutamid.tiptop.receiver_side.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.moutamid.tiptop.R;
import com.moutamid.tiptop.databinding.ActivityReceiverDashboardBinding;
import com.moutamid.tiptop.models.UserModel;
import com.moutamid.tiptop.utilis.Constants;

public class ReceiverDashboardActivity extends AppCompatActivity {
    ActivityReceiverDashboardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReceiverDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        UserModel model = dataSnapshot.getValue(UserModel.class);
                        Stash.put(Constants.STASH_USER, model);
                        binding.welcomeUser.setText("Welcome " + Constants.getUserFirstName());
                        binding.username.setText("@" + model.getUsername());
                        binding.walletAccount.setText(Constants.EURO_SYMBOL + Constants.decimalFormat(model.getWalletMoney()));
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        binding.settings.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
            finish();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.welcomeUser.setText("Welcome " + Constants.getUserFirstName());
    }

}