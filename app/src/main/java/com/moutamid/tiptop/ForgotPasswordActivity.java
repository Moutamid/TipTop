package com.moutamid.tiptop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.moutamid.tiptop.databinding.ActivityForgotPasswordBinding;
import com.moutamid.tiptop.utilis.Constants;

public class ForgotPasswordActivity extends AppCompatActivity {
    ActivityForgotPasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.initDialog(this);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.title.setText("Password Reset");

        binding.reset.setOnClickListener(v -> {
            if (valid()){
                Constants.showDialog();
                Constants.auth().sendPasswordResetEmail(binding.email.getEditText().getText().toString())
                        .addOnFailureListener(e -> {
                            Constants.dismissDialog();
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        })
                        .addOnSuccessListener(unused -> {
                            Constants.dismissDialog();
                            Toast.makeText(this, "A Password Reset email is sent to your account", Toast.LENGTH_SHORT).show();
                        });
            }
        });

    }

    private boolean valid() {
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
        return true;
    }
}