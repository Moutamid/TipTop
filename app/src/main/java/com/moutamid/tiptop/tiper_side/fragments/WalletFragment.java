package com.moutamid.tiptop.tiper_side.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.tiptop.R;
import com.moutamid.tiptop.databinding.FragmentWalletBinding;
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



        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Constants.initDialog(requireContext());
    }

}