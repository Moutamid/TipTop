package com.moutamid.tiptop.tiper_side.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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

public class TipperDashboardActivity extends AppCompatActivity {
    ActivityTipperDashboardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTipperDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        UserModel model = dataSnapshot.getValue(UserModel.class);
                        Stash.put(Constants.USERNAME, model.getName());
                        binding.welcomeUser.setText("Welcome " + getUserFirstName());
                    }
                }).addOnFailureListener(e -> {
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

        binding.settings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.welcomeUser.setText("Welcome " + getUserFirstName());
    }

    private String getUserFirstName() {

        String name = Stash.getString(Constants.USERNAME, "");
        String[] n;
        if (name.contains(" ")){
            n = name.split(" ");
            name = n[0];
        }

        return name + "!";
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