package com.moutamid.tiptop.receiver_side.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.moutamid.tiptop.R;
import com.moutamid.tiptop.databinding.ActivityEditProfileReceiverBinding;
import com.moutamid.tiptop.models.UserModel;
import com.moutamid.tiptop.tiper_side.activities.EditProfileActivity;
import com.moutamid.tiptop.utilis.Constants;

import java.util.Date;
import java.util.Locale;

public class EditProfileReceiverActivity extends AppCompatActivity {
    ActivityEditProfileReceiverBinding binding;
    Uri imageUri;
    UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileReceiverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.initDialog(this);

        Constants.showDialog();

        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    Constants.dismissDialog();
                    if (dataSnapshot.exists()) {
                        userModel = dataSnapshot.getValue(UserModel.class);
                        Stash.put(Constants.STASH_USER, userModel);

                        Glide.with(EditProfileReceiverActivity.this).load(userModel.getImage()).placeholder(R.drawable.profile_icon).into(binding.profile);
                        binding.username.getEditText().setText(userModel.getUsername());
                        binding.name.getEditText().setText(userModel.getName());
                        binding.company.getEditText().setText(userModel.getCompany());
                        binding.jobTitle.getEditText().setText(userModel.getJobTitle());
                    }
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.toolbar.title.setText("Edit Profile");

        binding.profile.setOnClickListener(v -> openGallery());
        binding.uploadImage.setOnClickListener(v -> openGallery());

        binding.update.setOnClickListener(v -> {
            Constants.showDialog();
            if (imageUri != null) {
                uploadImage();
            } else {
                update(userModel.getImage());
            }
        });

    }

    private void uploadImage() {
        String name = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault()).format(new Date());
        Constants.storageReference(Constants.auth().getCurrentUser().getUid())
                .child("profile_images").child(name + "/").putFile(imageUri)
                .addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                })
                .addOnSuccessListener(taskSnapshot -> {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                        update(uri.toString());
                    });
                });
    }

    private void update(String image) {
        userModel.setName(binding.name.getEditText().getText().toString());
        userModel.setCompany(binding.company.getEditText().getText().toString());
        userModel.setJobTitle(binding.jobTitle.getEditText().getText().toString());
        userModel.setImage(image);

        Stash.put(Constants.STASH_USER, userModel);

        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid()).setValue(userModel)
                .addOnSuccessListener(unused -> {
                    Constants.dismissDialog();
                    onBackPressed();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void openGallery() {
        ImagePicker.with(this)
                .cropSquare()
                .compress(512)
                .maxResultSize(1080, 1080)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(EditProfileReceiverActivity.this).load(imageUri).placeholder(R.drawable.profile_icon).into(binding.profile);
        }
    }

}