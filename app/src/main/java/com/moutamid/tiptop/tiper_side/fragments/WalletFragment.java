package com.moutamid.tiptop.tiper_side.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.tiptop.R;
import com.moutamid.tiptop.bottomsheets.RequestMoneyFragment;
import com.moutamid.tiptop.databinding.FragmentWalletBinding;
import com.moutamid.tiptop.models.TransactionModel;
import com.moutamid.tiptop.models.UserModel;
import com.moutamid.tiptop.tiper_side.BottomSheetDismissListener;
import com.moutamid.tiptop.tiper_side.activities.TipperDashboardActivity;
import com.moutamid.tiptop.tiper_side.adapter.TransactionAdapter;
import com.moutamid.tiptop.tiper_side.adapter.UsersAdapter;
import com.moutamid.tiptop.utilis.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class WalletFragment extends Fragment {
    FragmentWalletBinding binding;
    TransactionAdapter adapter;
    ArrayList<TransactionModel> list;
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

        list = new ArrayList<>();

        binding.pay.setOnClickListener(v -> {
            if (sn.getWalletMoney() > 0){
                if (activity != null) {
                    ViewPager viewPager = activity.findViewById(R.id.viewPager);
                    viewPager.setCurrentItem(1);
                }
            } else {
                Toast.makeText(binding.getRoot().getContext(), "Insufficient Balance", Toast.LENGTH_LONG).show();
            }
        });

        binding.transactionRC.setLayoutManager(new LinearLayoutManager(requireContext()));
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
                                Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

        binding.request.setOnClickListener(v -> {
            RequestMoneyFragment bottomSheetFragment = new RequestMoneyFragment();
            bottomSheetFragment.setListener(() -> onResume());
            bottomSheetFragment.show(getActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Constants.initDialog(requireContext());
        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        UserModel model = dataSnapshot.getValue(UserModel.class);
                        Stash.put(Constants.STASH_USER, model);
                        binding.walletAccount.setText(Constants.EURO_SYMBOL + Constants.decimalFormat(model.getWalletMoney()));
                    }
                });
    }

}