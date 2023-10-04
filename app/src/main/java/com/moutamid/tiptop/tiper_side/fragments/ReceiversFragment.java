package com.moutamid.tiptop.tiper_side.fragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.moutamid.tiptop.R;
import com.moutamid.tiptop.databinding.FragmentReceiversBinding;
import com.moutamid.tiptop.models.UserModel;
import com.moutamid.tiptop.tiper_side.TipInterface;
import com.moutamid.tiptop.tiper_side.adapter.UsersAdapter;
import com.moutamid.tiptop.utilis.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReceiversFragment extends Fragment {
    FragmentReceiversBinding binding;
    UsersAdapter adapter;
    ArrayList<UserModel> list;
    RequestQueue requestQueue;

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

        binding.receiversRC.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.receiversRC.setHasFixedSize(false);

        requestQueue = Volley.newRequestQueue(binding.getRoot().getContext());

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
            if (model.getBankDetails() != null) {
                openDIalog(model);
            } else {
                Toast.makeText(requireContext(), "User didn't setup his bank account", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void openDIalog(UserModel model) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.request_money_fragment);
        dialog.setCancelable(true);

        TextInputLayout walletAccount = dialog.findViewById(R.id.walletAccount);
        TextInputLayout amount = dialog.findViewById(R.id.amount);
        EditText message = dialog.findViewById(R.id.message);
        MaterialButton request = dialog.findViewById(R.id.request);

        request.setText("Send");
        walletAccount.getEditText().setText(model.getBankDetails().getBankID());

        request.setOnClickListener(v -> {
            if (
                    walletAccount.getEditText().getText().toString().isEmpty() ||
                            amount.getEditText().getText().toString().isEmpty() ||
                            message.getText().toString().isEmpty()
            ) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            } else {
                double money = Double.parseDouble(amount.getEditText().getText().toString());
                String note = message.getText().toString();
                sendTIP(model, money, note);
            }
        });

        dialog.show();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void sendTIP(UserModel model, double amount, String note) {
        try {
            JSONObject mainObject = getJSON2(model, amount, note);
            Log.d("response", "mainObject   " + mainObject.toString());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SQUARE_TESTING_API_2, mainObject,
                    response -> Log.d("response", "response   " + response.toString()),
                    error -> {
                        Log.d("response", "error   " + error.getMessage());
                      //  requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                    }
            ) {
                @Nullable
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    params.put("Authorization", "Bearer " + Constants.ACCESS_TOKEN);
                    params.put("Square-Version", "2023-09-25");
                    return params;
                }
            };

            requestQueue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private JSONObject getJSON(UserModel model, double amount, String note) throws JSONException {
        JSONObject object = new JSONObject();
        UserModel sn = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);

        JSONObject amount_money = new JSONObject();
        amount_money.put("amount", amount);
        amount_money.put("currency", "USD");

        JSONObject app_fee_money = new JSONObject();
        double value = (amount * 0.10);
        double roundedValue = Math.round(value * 100) / 100;
        app_fee_money.put("amount", roundedValue);
        app_fee_money.put("currency", "USD");

        object.put("idempotency_key", UUID.randomUUID().toString());
        object.put("amount_money", amount_money);
        object.put("app_fee_money", app_fee_money);
        object.put("location_id", Constants.Location_ID);
        object.put("autocomplete", true);
        object.put("source_id", "cnon:card-nonce-ok");
        object.put("customer_id", model.getBankDetails().getBankID());
        object.put("reference_id", UUID.randomUUID().toString());
        object.put("note", note);

        return object;
    }

    private JSONObject getJSON2(UserModel model, double amount, String note) throws JSONException {
        JSONObject object = new JSONObject();
        UserModel sn = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);

        object.put("sender_id", "1234567890");
        object.put("recipient_id", "9876543210");
        object.put("amount", amount);

        return object;
    }

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