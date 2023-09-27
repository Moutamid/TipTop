package com.moutamid.tiptop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.moutamid.tiptop.models.UserModel;
import com.moutamid.tiptop.receiver_side.activities.ReceiverDashboardActivity;
import com.moutamid.tiptop.tiper_side.activities.TipperDashboardActivity;
import com.moutamid.tiptop.utilis.Constants;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);

        ( (TextView) findViewById(R.id.message)).setText("Switching Your Account");

        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        UserModel model = dataSnapshot.getValue(UserModel.class);
                        if (model.isTipper()) {
                            startActivity(new Intent(LoadingActivity.this, TipperDashboardActivity.class));
                            finish();
                        } else {
                            if (model.isSubscribed()) {
                                startActivity(new Intent(LoadingActivity.this, ReceiverDashboardActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(LoadingActivity.this, PaymentScreenActivity.class));
                                finish();
                            }
                        }
                    } else {
                        startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }
}