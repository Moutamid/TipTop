package com.moutamid.tiptop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.moutamid.tiptop.models.UserModel;
import com.moutamid.tiptop.receiver_side.activities.ReceiverDashboardActivity;
import com.moutamid.tiptop.tiper_side.activities.TipperDashboardActivity;
import com.moutamid.tiptop.utilis.Constants;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (Constants.auth().getCurrentUser() != null) {
            Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                    .get().addOnSuccessListener(dataSnapshot -> {
                        if (dataSnapshot.exists()) {
                            UserModel model = dataSnapshot.getValue(UserModel.class);
                            if (model.isTipper()) {
                                startActivity(new Intent(SplashScreenActivity.this, TipperDashboardActivity.class));
                                finish();
                            } else {
                                if (model.isSubscribed()) {
                                    startActivity(new Intent(SplashScreenActivity.this, ReceiverDashboardActivity.class));
                                    finish();
                                } else {
                                    startActivity(new Intent(SplashScreenActivity.this, PaymentScreenActivity.class));
                                    finish();
                                }
                            }
                        } else {
                            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            }, 2000);
        }

    }
}