package com.moutamid.tiptop.tiper_side.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fxn.stash.Stash;
import com.moutamid.tiptop.R;
import com.moutamid.tiptop.databinding.FragmentWalletBinding;
import com.moutamid.tiptop.models.UserModel;
import com.moutamid.tiptop.tiper_side.activities.TipperDashboardActivity;
import com.moutamid.tiptop.utilis.Constants;

public class WalletFragment extends Fragment {
    FragmentWalletBinding binding;
    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWalletBinding.inflate(getLayoutInflater(), container, false);

        TipperDashboardActivity activity = (TipperDashboardActivity) getActivity();

        UserModel sn = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);
        binding.walletAccount.setText(Constants.EURO_SYMBOL + Constants.decimalFormat(sn.getWalletMoney()));

        binding.pay.setOnClickListener(v -> {
            if (activity != null) {
                ViewPager viewPager = activity.findViewById(R.id.viewPager);
                viewPager.setCurrentItem(1);
            }
        });

        binding.request.setOnClickListener(v -> {

        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Constants.initDialog(requireContext());
    }

}