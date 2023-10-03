package com.moutamid.tiptop.bottomsheets;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fxn.stash.Stash;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.moutamid.tiptop.databinding.RequestMoneyFragmentBinding;
import com.moutamid.tiptop.models.TransactionModel;
import com.moutamid.tiptop.models.UserModel;
import com.moutamid.tiptop.tiper_side.BottomSheetDismissListener;
import com.moutamid.tiptop.utilis.Constants;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WithdrawMoneyFragment extends BottomSheetDialogFragment {
    RequestMoneyFragmentBinding binding;
    UserModel userModel;
    private BottomSheetDismissListener listener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RequestMoneyFragmentBinding.inflate(getLayoutInflater(), container, false);

        userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);

        Constants.initDialog(requireContext());

        binding.request.setOnClickListener(v -> {
            if (valid()){
                String ID = UUID.randomUUID().toString();
                Constants.showDialog();
                TransactionModel transactionModel = new TransactionModel (
                        ID, Constants.auth().getCurrentUser().getUid(), Constants.auth().getCurrentUser().getUid(),
                        binding.amount.getEditText().getText().toString(), userModel.getName(), userModel.getName(), binding.email.getEditText().getText().toString(),
                        Constants.REQ, new Date().getTime()
                );
                Constants.databaseReference().child(Constants.TRANSACTIONS).child(Constants.auth().getCurrentUser().getUid())
                        .child(ID).setValue(transactionModel).addOnSuccessListener(unused -> {
                            Map<String, Object> map = new HashMap<>();
                            double money = userModel.getWalletMoney() + Double.parseDouble(binding.amount.getEditText().getText().toString());
                            map.put("walletMoney", money);
                            Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid()).updateChildren(map)
                                    .addOnSuccessListener(unused1 -> {
                                        Constants.dismissDialog();
                                        Toast.makeText(binding.getRoot().getContext(), "Money Added", Toast.LENGTH_SHORT).show();
                                        this.dismiss();
                                    }).addOnFailureListener(e -> {
                                        Constants.dismissDialog();
                                        Toast.makeText(binding.getRoot().getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });

                        }).addOnFailureListener(e -> {
                            Constants.dismissDialog();
                            Toast.makeText(binding.getRoot().getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        return binding.getRoot();
    }

    private boolean valid() {
        if (binding.email.getEditText().getText().toString().isEmpty()){
            binding.email.setErrorEnabled(true);
            binding.email.setError("Email is empty");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getEditText().getText().toString()).matches()){
            binding.email.setErrorEnabled(true);
            binding.email.setError("Email is not valid");
            return false;
        }

        if (binding.amount.getEditText().getText().toString().isEmpty()){
            binding.amount.setErrorEnabled(true);
            binding.amount.setError("Amount is empty");
            return false;
        }
        return true;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onBottomSheetDismissed();
        }
    }

    public void setListener(BottomSheetDismissListener listener) {
        this.listener = listener;
    }


}
