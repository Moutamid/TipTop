package com.moutamid.tiptop.tiper_side.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.moutamid.tiptop.R;
import com.moutamid.tiptop.databinding.ActivitySignUpTiperBinding;
import com.moutamid.tiptop.models.UserModel;
import com.moutamid.tiptop.utilis.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpTiperActivity extends AppCompatActivity {
    ActivitySignUpTiperBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpTiperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.title.setText("Sign Up");

        binding.login.setOnClickListener(v -> startActivity(new Intent(this, LoginTiperActivity.class)));

        binding.signup.setOnClickListener(v -> {
            if (valid()) {
                Constants.showDialog();
                Constants.auth().createUserWithEmailAndPassword(
                        binding.email.getEditText().getText().toString(),
                        binding.password.getEditText().getText().toString()
                ).addOnSuccessListener(authResult -> {
                    UserModel userModel = new UserModel (
                            Constants.auth().getCurrentUser().getUid(),
                            binding.name.getEditText().getText().toString(),
                            getUserName(binding.email.getEditText().getText().toString()),
                            binding.email.getEditText().getText().toString(),
                            binding.password.getEditText().getText().toString(),
                            binding.company.getEditText().getText().toString(),
                            binding.jobTitle.getEditText().getText().toString(),
                            0, "",
                            true, false
                    );
                    Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid()).setValue(userModel)
                            .addOnSuccessListener(unused -> {
                                Constants.dismissDialog();
                                startActivity(new Intent(this, TipperDashboardActivity.class));
                                finish();
                            }).addOnFailureListener(e -> {
                                Constants.dismissDialog();
                                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.initDialog(this);
    }

    private String getUserName(String email) {
        Pattern pattern = Pattern.compile("(.+)@.+");
        Matcher matcher = pattern.matcher(email);
        if (matcher.find()) {
            String username = matcher.group(1);
            return username;
        }
        return "";
    }

    private boolean valid() {

        if (binding.name.getEditText().getText().toString().isEmpty()) {
            binding.name.setErrorEnabled(true);
            binding.name.setError("Name is empty");
            return false;
        }
        if (binding.company.getEditText().getText().toString().isEmpty()) {
            binding.company.setErrorEnabled(true);
            binding.company.setError("Company is empty");
            return false;
        }
        if (binding.jobTitle.getEditText().getText().toString().isEmpty()) {
            binding.jobTitle.setErrorEnabled(true);
            binding.jobTitle.setError("Email is empty");
            return false;
        }
/*        if (binding.linkBank.getEditText().getText().toString().isEmpty()) {
            binding.linkBank.setErrorEnabled(true);
            binding.linkBank.setError("Bank link is empty");
            return false;
        }*/
        if (binding.email.getEditText().getText().toString().isEmpty()) {
            binding.email.setErrorEnabled(true);
            binding.email.setError("Email is empty");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getEditText().getText().toString()).matches()) {
            binding.email.setErrorEnabled(true);
            binding.email.setError("Email is not valid");
            return false;
        }
        if (binding.password.getEditText().getText().toString().isEmpty()) {
            binding.password.setErrorEnabled(true);
            binding.password.setError("Password is empty");
            return false;
        }

        return true;
    }
}