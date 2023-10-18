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
import com.moutamid.tiptop.models.TransactionModel;
import com.moutamid.tiptop.models.UserModel;
import com.moutamid.tiptop.tiper_side.TipInterface;
import com.moutamid.tiptop.tiper_side.adapter.UsersAdapter;
import com.moutamid.tiptop.utilis.Constants;
import com.squareup.square.Environment;
import com.squareup.square.SquareClient;
import com.squareup.square.api.CheckoutApi;
import com.squareup.square.api.DefaultCheckoutApi;
import com.squareup.square.api.DefaultPaymentsApi;
import com.squareup.square.api.PaymentsApi;
import com.squareup.square.api.TransactionsApi;
import com.squareup.square.exceptions.ApiException;
import com.squareup.square.models.CancelPaymentByIdempotencyKeyRequest;
import com.squareup.square.models.CancelPaymentByIdempotencyKeyResponse;
import com.squareup.square.models.CancelPaymentResponse;
import com.squareup.square.models.CaptureTransactionResponse;
import com.squareup.square.models.CashPaymentDetails;
import com.squareup.square.models.CompletePaymentRequest;
import com.squareup.square.models.CompletePaymentResponse;
import com.squareup.square.models.CreatePaymentRequest;
import com.squareup.square.models.CreatePaymentResponse;
import com.squareup.square.models.GetPaymentResponse;
import com.squareup.square.models.ListPaymentsResponse;
import com.squareup.square.models.ListTransactionsResponse;
import com.squareup.square.models.Money;
import com.squareup.square.models.Payment;
import com.squareup.square.models.RetrieveTransactionResponse;
import com.squareup.square.models.Transaction;
import com.squareup.square.models.UpdatePaymentRequest;
import com.squareup.square.models.UpdatePaymentResponse;
import com.squareup.square.models.VoidTransactionResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import io.apimatic.core.GlobalConfiguration;

public class ReceiversFragment extends Fragment {
    FragmentReceiversBinding binding;
    UsersAdapter adapter;
    ArrayList<UserModel> list;
    RequestQueue requestQueue;
    SquareClient client;

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

        client = new SquareClient.Builder()
                .environment(Environment.SANDBOX)
                .accessToken(Constants.ACCESS_TOKEN)
                .build();

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
                UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);
                if (userModel.getWalletMoney() >= money) {
                    dialog.dismiss();
                    sendTip(model, money, note);
                } else {
                    Toast.makeText(requireContext(), "Insufficient Balance", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    private void sendTip(UserModel model, double money, String note) {
        UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);
        String ID = UUID.randomUUID().toString();
        Constants.showDialog();

        TransactionModel senderTransaction = new TransactionModel(
                ID, userModel.getBankDetails().getEmail(), model.getBankDetails().getEmail(),
                String.valueOf(money), userModel.getName(), model.getName(), note,
                Constants.PAY, new Date().getTime()
        );

        Constants.databaseReference().child(Constants.TRANSACTIONS).child(Constants.auth().getCurrentUser().getUid())
                .child(ID).setValue(senderTransaction).addOnSuccessListener(unused -> {
                    Map<String, Object> map = new HashMap<>();
                    double deduction = userModel.getWalletMoney() - money;
                    map.put("walletMoney", deduction);
                    Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid()).updateChildren(map)
                            .addOnSuccessListener(unused1 -> {

                                Constants.databaseReference().child(Constants.TRANSACTIONS).child(model.getID())
                                        .child(ID).setValue(senderTransaction).addOnSuccessListener(unused12 -> {
                                            Map<String, Object> map1 = new HashMap<>();
                                            double tip = model.getWalletMoney() + money;
                                            map1.put("walletMoney", tip);
                                            Constants.databaseReference().child(Constants.USER).child(model.getID()).updateChildren(map1)
                                                    .addOnSuccessListener(unused11 -> {
                                                        Constants.dismissDialog();
                                                        Toast.makeText(binding.getRoot().getContext(), "Transaction Sent Successfully", Toast.LENGTH_SHORT).show();
                                                    }).addOnFailureListener(e -> {
                                                        Constants.dismissDialog();
                                                        Toast.makeText(binding.getRoot().getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    });

                                        }).addOnFailureListener(e -> {
                                            Constants.dismissDialog();
                                            Toast.makeText(binding.getRoot().getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });

                            }).addOnFailureListener(e -> {
                                Constants.dismissDialog();
                                Toast.makeText(binding.getRoot().getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            });

                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(binding.getRoot().getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }


    public void sendTIP(UserModel model, double amount, String note) {
        Money amountMoney = new Money.Builder()
                .amount(1000L)
                .currency("EUR")
                .build();

        Money appFeeMoney = new Money.Builder()
                .amount(10L)
                .currency("EUR")
                .build();

        Money tipMoney = new Money.Builder()
                .amount(10L)
                .currency("EUR")
                .build();

//        CreatePaymentRequest body = new CreatePaymentRequest.Builder("ccof:GaJGNaZa8x4OgDJn4GB", "7b0f3ec5-086a-4871-8f13-3c81b3875218")
//                .amountMoney(amountMoney)
//                .appFeeMoney(appFeeMoney)
//                .autocomplete(true)
//                .customerId("W92WH6P11H4Z77CTET0RNTGFW8")
//                .locationId("L88917AVBK2S5")
//                .referenceId("123456")
//                .note("Brief description")
//                .build();

        PaymentsApi paymentsApi = client.getPaymentsApi();

        CreatePaymentRequest body = new CreatePaymentRequest.Builder("cnon:card-nonce-ok", "31209d23-1dfb-4e11-80cf-45f42412473d")
                .amountMoney(amountMoney)
                .tipMoney(tipMoney)
                .appFeeMoney(appFeeMoney)
                .customerId("1234567890")
                .build();

        paymentsApi.createPaymentAsync(body)
                .thenAccept(result -> {
                    Log.d(TAG, "sendTIP: " + result.toString());
                    Log.d(TAG, "Success");
                })
                .exceptionally(exception -> {
                    Log.d(TAG, "Failed to make the request");
                    exception.printStackTrace();
                    Log.d(TAG, "Exception: " + exception.getCause());
                    return null;
                });

//        paymentsApi.createPaymentAsync(body)
//                .thenAccept(result -> {
//                    Log.d(TAG, "sendTIP: "+ result.toString());
//                    Log.d(TAG, "Success");
//                })
//                .exceptionally(exception -> {
//                    Log.d(TAG, "Failed to make the request");
//                    exception.printStackTrace();
//                    Log.d(TAG, "Exception: " + exception.getCause());
//                    return null;
//                });
    }

    private static final String TAG = "ReceiversFragment";

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