package com.moutamid.tiptop.tiper_side.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.moutamid.tiptop.R;
import com.moutamid.tiptop.databinding.FragmentReceiversBinding;
import com.moutamid.tiptop.models.UserModel;
import com.moutamid.tiptop.tiper_side.TipInterface;
import com.moutamid.tiptop.tiper_side.activities.TipperDashboardActivity;
import com.moutamid.tiptop.tiper_side.adapter.UsersAdapter;
import com.moutamid.tiptop.utilis.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

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

/*        SquareClient client = new SquareClient.Builder()
                .environment(Environment.SANDBOX)
                .accessToken(getString(R.string.SECRET_TOKEN))
                .build();

        TipperDashboardActivity activity = (TipperDashboardActivity) getActivity();

        InputStream inputStream = TipperDashboardActivity.class.getResourceAsStream("/config.properties");
        Properties prop = new Properties();

        try {
            prop.load(inputStream);
        } catch (IOException e) {
            System.out.println("Error reading properties file");
            e.printStackTrace();
        }
        locationsApi = client.getLocationsApi();*/

        binding.receiversRC.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.receiversRC.setHasFixedSize(false);

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
                        if (dataSnapshot.exists()) {
                            list.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                UserModel userModel = snapshot.getValue(UserModel.class);

                                if (bothNotEmpty()) {
                                    if (
                                            userModel.getName().toLowerCase().contains(binding.searchName.getEditText().getText().toString().toLowerCase()) &&
                                                    userModel.getCompany().toLowerCase().contains(binding.searchCompany.getEditText().getText().toString().toLowerCase()) &&
                                                    !userModel.getID().equals(Constants.auth().getCurrentUser().getUid()) &&
                                                    !userModel.isTipper()
                                    ) {
                                        list.add(userModel);
                                    }
                                } else if (!nameEmpty()) {
                                    if (
                                            userModel.getName().toLowerCase().contains(binding.searchName.getEditText().getText().toString().toLowerCase()) &&
                                                    !userModel.getID().equals(Constants.auth().getCurrentUser().getUid()) &&
                                                    !userModel.isTipper()
                                    ) {
                                        list.add(userModel);
                                    }
                                } else if (!companyEmpty()) {
                                    if (
                                            userModel.getCompany().toLowerCase().contains(binding.searchCompany.getEditText().getText().toString().toLowerCase()) &&
                                                    !userModel.getID().equals(Constants.auth().getCurrentUser().getUid()) &&
                                                    !userModel.isTipper()
                                    ) {
                                        list.add(userModel);
                                    }
                                }
                            }

                            if (list.size() > 0) {
                                binding.receiversRC.setVisibility(View.VISIBLE);
                                binding.nothingFound.setVisibility(View.GONE);
                                adapter = new UsersAdapter(binding.getRoot().getContext(), list, onClick);
                                binding.receiversRC.setAdapter(adapter);
                            } else {
                                binding.receiversRC.setVisibility(View.GONE);
                                binding.nothingFound.setVisibility(View.VISIBLE);
                            }

                        }
                        Constants.dismissDialog();
                    }).addOnFailureListener(e -> {
                        Constants.dismissDialog();
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    TipInterface onClick = new TipInterface() {
        @Override
        public void onClick(UserModel model) {
/*            locationsApi.listLocationsAsync()
                    .thenAccept(result -> {
                        for (Location l : result.getLocations()) {

                        }
                    })
                    .exceptionally(exception -> {
                        try {
                            throw exception.getCause();
                        } catch (ApiException ae) {
                            for (Error err : ae.getErrors()) {
                                System.out.println(err.getCategory());
                                System.out.println(err.getCode());
                                System.out.println(err.getDetail());
                            }
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                        return null;
                    })
                    .join();
            SquareClient.shutdown();*/
        }
    };

    private boolean valid() {
        return !nameEmpty() || !companyEmpty();
    }

    private boolean nameEmpty() {
        return binding.searchName.getEditText().getText().toString().isEmpty();
    }

    private boolean companyEmpty() {
        return binding.searchCompany.getEditText().getText().toString().isEmpty();
    }

    private boolean bothNotEmpty() {
        return !nameEmpty() && !companyEmpty();
    }
}