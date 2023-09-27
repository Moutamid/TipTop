package com.moutamid.tiptop.receiver_side.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.moutamid.tiptop.ForgotPasswordActivity;
import com.moutamid.tiptop.MainActivity;
import com.moutamid.tiptop.PaymentScreenActivity;
import com.moutamid.tiptop.R;
import com.moutamid.tiptop.SplashScreenActivity;
import com.moutamid.tiptop.databinding.ActivityRecieverLoginBinding;
import com.moutamid.tiptop.models.UserModel;
import com.moutamid.tiptop.tiper_side.activities.SignUpTiperActivity;
import com.moutamid.tiptop.tiper_side.activities.TipperDashboardActivity;
import com.moutamid.tiptop.utilis.Constants;

public class ReceiverLoginActivity extends AppCompatActivity {
    ActivityRecieverLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecieverLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.title.setText("Sign In");

        binding.create.setOnClickListener(v -> {
            startActivity(new Intent(this, ReceiverSignupActivity.class));
            finish();
        });
        binding.forgot.setOnClickListener(v -> startActivity(new Intent(this, ForgotPasswordActivity.class)));

        binding.login.setOnClickListener(v -> {
            if (valid()) {
                Constants.showDialog();
                Constants.auth().signInWithEmailAndPassword(
                                binding.email.getEditText().getText().toString(),
                                binding.password.getEditText().getText().toString()
                        ).addOnSuccessListener(authResult -> {
                            Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                                    .get().addOnSuccessListener(dataSnapshot -> {
                                        Constants.dismissDialog();
                                        if (dataSnapshot.exists()) {
                                            UserModel model = dataSnapshot.getValue(UserModel.class);
                                            if (model.isTipper()) {
                                                startActivity(new Intent(ReceiverLoginActivity.this, TipperDashboardActivity.class));
                                                finish();
                                            } else {
                                                if (model.isSubscribed()) {
                                                    startActivity(new Intent(ReceiverLoginActivity.this, ReceiverDashboardActivity.class));
                                                    finish();
                                                } else {
                                                    startActivity(new Intent(ReceiverLoginActivity.this, PaymentScreenActivity.class));
                                                    finish();
                                                }
                                            }
                                        }
                                    }).addOnFailureListener(e -> {
                                        Constants.dismissDialog();
                                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .addOnFailureListener(e -> {
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


    private boolean valid() {
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

        if (binding.password.getEditText().getText().toString().isEmpty()){
            binding.password.setErrorEnabled(true);
            binding.password.setError("Password is empty");
            return false;
        }
        return true;
    }

}