package com.moutamid.tiptop.receiver_side.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.tiptop.R;
import com.moutamid.tiptop.utilis.Constants;

public class ReceiverDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_dashboard);
        Constants.checkApp(this);
    }
}