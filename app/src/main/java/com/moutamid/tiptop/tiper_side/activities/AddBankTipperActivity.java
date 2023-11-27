package com.moutamid.tiptop.tiper_side.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.tiptop.R;
import com.moutamid.tiptop.databinding.ActivityAddBankTipperBinding;
import com.moutamid.tiptop.models.Address;
import com.moutamid.tiptop.models.BankDetails;
import com.moutamid.tiptop.models.UserModel;
import com.moutamid.tiptop.receiver_side.activities.AddBankReceiverActivity;
import com.moutamid.tiptop.stripe.ExchangeAuthorizationCodeForAccessToken;
import com.moutamid.tiptop.utilis.Constants;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.AccountLink;

import java.util.HashMap;
import java.util.Map;

public class AddBankTipperActivity extends AppCompatActivity {
    ActivityAddBankTipperBinding binding;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBankTipperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

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
                            binding.account.getEditText().setText(userModel.getBankDetails().getAccountNumber());
                            binding.routing.getEditText().setText(userModel.getBankDetails().getRoutingNumber());
                            binding.ssn.getEditText().setText(userModel.getBankDetails().getLast4SSN());
                            binding.dob.getEditText().setText(userModel.getBankDetails().getDOB());
                            binding.name.getEditText().setText(userModel.getBankDetails().getName());
                            binding.email.getEditText().setText(userModel.getBankDetails().getEmail());
                            binding.phone.getEditText().setText(userModel.getBankDetails().getPhone());
                            if (userModel.getBankDetails().getAddress() != null) {
                                binding.country.getEditText().setText(userModel.getBankDetails().getAddress().getCountry());
                                binding.city.getEditText().setText(userModel.getBankDetails().getAddress().getCity());
                                binding.state.getEditText().setText(userModel.getBankDetails().getAddress().getState());
                                binding.postalCode.getEditText().setText(userModel.getBankDetails().getAddress().getPostalCode());
                                binding.address.getEditText().setText(userModel.getBankDetails().getAddress().getAddress());
                            }
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
                        binding.name.getEditText().getText().toString(),
                        binding.email.getEditText().getText().toString(),
                        binding.phone.getEditText().getText().toString(),
                        binding.account.getEditText().getText().toString(),
                        binding.routing.getEditText().getText().toString(),
                        getLast4Digit(),
                        binding.dob.getEditText().getText().toString(),
                        binding.ssn.getEditText().getText().toString(),
                        new Address(
                                binding.country.getEditText().getText().toString(),
                                binding.city.getEditText().getText().toString(),
                                binding.address.getEditText().getText().toString(),
                                binding.state.getEditText().getText().toString(),
                                binding.postalCode.getEditText().getText().toString()
                        )
                        ));
                Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid()).setValue(userModel)
                        .addOnSuccessListener(unused -> {
                            Constants.dismissDialog();
                            Stash.put(Constants.STASH_USER, userModel);

                            Stripe.apiKey = Constants.API_KEY;
                            Map<String, Object> params = new HashMap<>();
                            params.put("account", binding.account.getEditText().getText().toString());
                            params.put("refresh_url", "https://google.com/");
                            params.put(
                                    "return_url",
                                    "https://google.com/"
                            );
                            params.put("type", "account_onboarding");

                            try {
                                AccountLink accountLink = AccountLink.create(params);
                                String url = accountLink.getUrl();
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                                browserIntent.setData(Uri.parse(url));
                                startActivity(browserIntent);
                            } catch (StripeException e) {
                                e.printStackTrace();
                            }

                        }).addOnFailureListener(e -> {
                            Constants.dismissDialog();
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getData() != null) {
            Uri uri = intent.getData();
            if (uri.getQueryParameter("code") != null) {
                // Exchange the authorization code for an access token
                String code = uri.getQueryParameter("code");
                new ExchangeAuthorizationCodeForAccessToken(code).exchangeAuthorizationCode(AddBankTipperActivity.this);
            }
        }
    }

    private String getLast4Digit() {
        String str = binding.account.getEditText().getText().toString();
        return str.substring(str.length() - 4);
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