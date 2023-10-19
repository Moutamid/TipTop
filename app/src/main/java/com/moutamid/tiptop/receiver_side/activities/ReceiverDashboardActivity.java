package com.moutamid.tiptop.receiver_side.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.anjlab.android.iab.v3.SkuDetails;
import com.fxn.stash.Stash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.tiptop.R;
import com.moutamid.tiptop.bottomsheets.WithdrawMoneyFragment;
import com.moutamid.tiptop.databinding.ActivityReceiverDashboardBinding;
import com.moutamid.tiptop.models.TransactionModel;
import com.moutamid.tiptop.models.UserModel;
import com.moutamid.tiptop.tiper_side.adapter.TransactionAdapter;
import com.moutamid.tiptop.utilis.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiverDashboardActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    ActivityReceiverDashboardBinding binding;
    BillingProcessor bp;
    ArrayList<TransactionModel> list;
    TransactionAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReceiverDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        bp = BillingProcessor.newBillingProcessor(this, Constants.LICENSE_KEY, this);
        bp.initialize();

        ArrayList<String> ids = new ArrayList<>();
        ids.add(Constants.LIFETIME);
        ids.add(Constants.MONTHLY);
        bp.getSubscriptionsListingDetailsAsync(ids, new BillingProcessor.ISkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(@Nullable List<SkuDetails> products) {
                Log.d("PURSS", "Size : " + products.size());
                for (int i = 0; i < products.size(); i++){
                    boolean isSub = products.get(i).isSubscription;
                    Stash.put(Constants.IS_VIP, isSub);
                }
            }

            @Override
            public void onSkuDetailsError(String error) {

            }
        });
        Log.d("PURSS", "init : " + bp.isInitialized());

        boolean b =  bp.isSubscribed(Constants.LIFETIME) || bp.isSubscribed(Constants.MONTHLY);
        Stash.put(Constants.IS_VIP, b);

        list = new ArrayList<>();

        Map<String, Object> subscribe = new HashMap<>();
        subscribe.put("subscribed", b);
        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid()).updateChildren(subscribe)
                .addOnSuccessListener(unused -> {
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        binding.settings.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
            finish();
        });

        binding.transactionRC.setLayoutManager(new LinearLayoutManager(this));
        binding.transactionRC.setHasFixedSize(false);

        Constants.databaseReference().child(Constants.TRANSACTIONS).child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            list.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                TransactionModel model = dataSnapshot.getValue(TransactionModel.class);
                                list.add(model);
                            }

                            if (list.size() > 0) {

                                list.sort(Comparator.comparing(TransactionModel::getTimestamp));
                                Collections.reverse(list);
                                binding.transactionRC.setVisibility(View.VISIBLE);
                                binding.nothingFound.setVisibility(View.GONE);
                                adapter = new TransactionAdapter(binding.getRoot().getContext(), list);
                                binding.transactionRC.setAdapter(adapter);
                            } else {
                                binding.transactionRC.setVisibility(View.GONE);
                                binding.nothingFound.setVisibility(View.VISIBLE);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ReceiverDashboardActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        binding.withdraw.setOnClickListener(v -> {
            WithdrawMoneyFragment bottomSheetFragment = new WithdrawMoneyFragment();
            bottomSheetFragment.setListener(() -> onResume());
            bottomSheetFragment.show(this.getSupportFragmentManager(), bottomSheetFragment.getTag());
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.initDialog(this);
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
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable PurchaseInfo details) {

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }
}