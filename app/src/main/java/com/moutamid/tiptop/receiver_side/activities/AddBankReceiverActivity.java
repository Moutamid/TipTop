package com.moutamid.tiptop.receiver_side.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.tiptop.R;
import com.moutamid.tiptop.databinding.ActivityAddBankReceiverBinding;
import com.moutamid.tiptop.models.BankDetails;
import com.moutamid.tiptop.models.UserModel;
import com.moutamid.tiptop.tiper_side.activities.EditProfileActivity;
import com.moutamid.tiptop.utilis.Constants;

public class AddBankReceiverActivity extends AppCompatActivity {
    ActivityAddBankReceiverBinding binding;
    UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBankReceiverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.initDialog(this);

        Constants.showDialog();

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.title.setText("Bank Details");

        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    Constants.dismissDialog();
                    if (dataSnapshot.exists()) {
                        userModel = dataSnapshot.getValue(UserModel.class);
                        Stash.put(Constants.STASH_USER, userModel);
                        if (userModel.getBankDetails() != null) {
                            binding.account.getEditText().setText(userModel.getBankDetails().getBankID());
                            binding.name.getEditText().setText(userModel.getBankDetails().getName());
                            binding.email.getEditText().setText(userModel.getBankDetails().getEmail());
                        }
                    }
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        binding.save.setOnClickListener(v -> {
            if (valid()) {
                Constants.showDialog();
                userModel.setBankDetails(new BankDetails(Constants.auth().getCurrentUser().getUid(),
                        binding.account.getEditText().getText().toString(),
                        binding.name.getEditText().getText().toString(),
                        binding.email.getEditText().getText().toString()));
                Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid()).setValue(userModel)
                        .addOnSuccessListener(unused -> {
                            Constants.dismissDialog();
                            Toast.makeText(this, "Bank details Added", Toast.LENGTH_SHORT).show();
                            Stash.put(Constants.STASH_USER, userModel);
                        }).addOnFailureListener(e -> {
                            Constants.dismissDialog();
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

    }

    private boolean valid() {

        if (binding.name.getEditText().getText().toString().isEmpty()){
            binding.name.setErrorEnabled(true);
            binding.name.setError("Name is empty");
            return false;
        }

        if (binding.email.getEditText().getText().toString().isEmpty()){
            binding.email.setErrorEnabled(true);
            binding.email.setError("Email is empty");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getEditText().getText().toString()).matches()){
            binding.email.setErrorEnabled(true);
            binding.email.setError("Email is not valid");
            return false;
        }
        return true;
    }
}