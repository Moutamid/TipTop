package com.moutamid.tiptop.tiper_side.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.moutamid.tiptop.R;
import com.moutamid.tiptop.databinding.FragmentReceiversBinding;
import com.moutamid.tiptop.models.UserModel;
import com.moutamid.tiptop.tiper_side.adapter.UsersAdapter;
import com.moutamid.tiptop.utilis.Constants;

import java.util.ArrayList;

public class ReceiversFragment extends Fragment {
    FragmentReceiversBinding binding;

    UsersAdapter adapter;
    ArrayList<UserModel> list;

    public ReceiversFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        Constants.initDialog(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReceiversBinding.inflate(getLayoutInflater(), container, false);

        list = new ArrayList<>();

        binding.search.setOnClickListener(v -> {
            searchUsers();
        });

        return binding.getRoot();
    }

    private void searchUsers() {
        if (valid()) {
            Constants.showDialog();
            Constants.databaseReference().child(Constants.USER)
                    .get().addOnSuccessListener(dataSnapshot -> {
                        if (dataSnapshot.exists()){
                            list.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                UserModel userModel = snapshot.getValue(UserModel.class);

                                if (bothNotEmpty()) {
                                    if (
                                            userModel.getName().equalsIgnoreCase(binding.searchName.getEditText().getText().toString()) &&
                                            userModel.getCompany().equalsIgnoreCase(binding.searchCompany.getEditText().getText().toString()) &&
                                            !userModel.getID().equals(Constants.auth().getCurrentUser().getUid())
                                    ) {
                                        list.add(userModel);
                                    }
                                } else if (!nameEmpty()) {
                                    if (
                                            userModel.getName().equalsIgnoreCase(binding.searchName.getEditText().getText().toString()) &&
                                                    !userModel.getID().equals(Constants.auth().getCurrentUser().getUid())
                                    ) {
                                        list.add(userModel);
                                    }
                                } else if (!companyEmpty()) {
                                    if (
                                            userModel.getName().equalsIgnoreCase(binding.searchCompany.getEditText().getText().toString()) &&
                                                    !userModel.getID().equals(Constants.auth().getCurrentUser().getUid())
                                    ) {
                                        list.add(userModel);
                                    }
                                }

                                if (list.size() > 0){
                                    binding.receiversRC.setVisibility(View.VISIBLE);
                                    binding.nothingFound.setVisibility(View.GONE);
                                    adapter = new UsersAdapter(binding.getRoot().getContext(), list);
                                    binding.receiversRC.setAdapter(adapter);
                                } else {
                                    binding.receiversRC.setVisibility(View.GONE);
                                    binding.nothingFound.setVisibility(View.VISIBLE);
                                }

                            }
                        }
                        Constants.dismissDialog();
                    }).addOnFailureListener(e -> {
                        Constants.dismissDialog();
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private boolean valid() {
        return !nameEmpty() || !companyEmpty();
    }
    private boolean nameEmpty() {
        return binding.searchName.getEditText().getText().toString().isEmpty();
    }
    private boolean companyEmpty() {
        return binding.searchName.getEditText().getText().toString().isEmpty();
    }
    private boolean bothNotEmpty() {
        return !nameEmpty() && !companyEmpty();
    }
}