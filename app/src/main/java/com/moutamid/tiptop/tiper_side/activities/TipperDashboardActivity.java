package com.moutamid.tiptop.tiper_side.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.anjlab.android.iab.v3.SkuDetails;
import com.fxn.stash.Stash;
import com.google.android.material.tabs.TabLayout;
import com.moutamid.tiptop.MainActivity;
import com.moutamid.tiptop.PaymentScreenActivity;
import com.moutamid.tiptop.R;
import com.moutamid.tiptop.SplashScreenActivity;
import com.moutamid.tiptop.databinding.ActivityTipperDashboardBinding;
import com.moutamid.tiptop.models.UserModel;
import com.moutamid.tiptop.receiver_side.activities.ReceiverDashboardActivity;
import com.moutamid.tiptop.tiper_side.fragments.ReceiversFragment;
import com.moutamid.tiptop.tiper_side.fragments.WalletFragment;
import com.moutamid.tiptop.utilis.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TipperDashboardActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    ActivityTipperDashboardBinding binding;
    BillingProcessor bp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTipperDashboardBinding.inflate(getLayoutInflater());
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

        Map<String, Object> subscribe = new HashMap<>();
        subscribe.put("subscribed", b);
        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid()).updateChildren(subscribe)
                .addOnSuccessListener(unused -> {

                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        CustomPagerAdapter adapter = new CustomPagerAdapter(getSupportFragmentManager()); // Replace with your own PagerAdapter
        binding.viewPager.setAdapter(adapter);

        adapter.addFragment(new WalletFragment(), R.drawable.wallet_solid);
        adapter.addFragment(new ReceiversFragment(), R.drawable.tipping_dollor);
        adapter.notifyDataSetChanged();
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        for (int i = 0; i < adapter.getCount(); i++) {
            TabLayout.Tab tab = binding.tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setIcon(adapter.getIconResId(i));
            }
        }

        binding.settings.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
            finish();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        UserModel model = dataSnapshot.getValue(UserModel.class);
                        Stash.put(Constants.STASH_USER, model);
                        binding.welcomeUser.setText("Welcome " + Constants.getUserFirstName());
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


    public class CustomPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> fragmentList = new ArrayList<>();
        private final ArrayList<Integer> iconResIds = new ArrayList<>();

        public CustomPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Add a fragment to the adapter
        public void addFragment(Fragment fragment, int iconResId) {
            fragmentList.add(fragment);
            iconResIds.add(iconResId);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        // Get the icon resource ID for a specific position
        public int getIconResId(int position) {
            return iconResIds.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Return null to display no titles
            return null;
        }
    }

}