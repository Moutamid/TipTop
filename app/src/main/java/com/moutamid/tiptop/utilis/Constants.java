package com.moutamid.tiptop.utilis;

import com.fxn.stash.Stash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.moutamid.tiptop.R;
import com.moutamid.tiptop.models.UserModel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constants {

    /*
    *
    *
    <include android:id="@+id/toolbar" layout="@layout/toolbar" />

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_below="@id/toolbar"
        android:background="?attr/colorPrimary">

        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@drawable/bottom_bg">



        </RelativeLayout>

    </RelativeLayout>
    *
    * */

    static Dialog dialog;

    public static final String SQUARE_TESTING_API = "https://connect.squareupsandbox.com/v2/payments";
    public static final String SQUARE_TESTING_API_2 = "https://connect.squareup.com/v2/sandbox/payments";
    public static final String SQUARE_PRODUCTION_API = "https://connect.squareup.com/v2/payments";
    public static final String Location_ID = "L4J3CPYZ3R7JD"; // SANDBOX
//    public static final String ACCESS_TOKEN = "EAAAEObzsGjBA0BWM-rXAWzAMvJV8QYatQKVdstya32epXKiuBusV8_XXl3MrnrV"; // SANDBOX
    public static final String ACCESS_TOKEN = "EAAAEE5JykSXD4qvxFbrtmlE1Ppw3Oi1R14bMn5lrQtox3rwIVAwj2wRjB0LdpSa"; // SANDBOX
    public static final String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArBueEiTWfZk7O+BE104DZiZh6Dj8tIMJdZter0tO2KH0MUzn1UB14nrg6bb5IGup29NEvG6UI0ZelHgxClkYvLxzgXjagFgFKN46U6kijHxsccY9evn06NWNyJbPFoAkTQyVIPk44SBufx7g4H5f0azOtY28DL3fg5nvLDJ7yPAejGSvdZXuiVGopwS1A05QrjrgA6ol1YOUzxu22Vanb4ncIDTdF35MA2arbVf74fYFcqkJgWcfWkENRDxoj8IPo1tzWH2rO1/vaILmosJKd1SOrMhrmmyLita0AzJXH/d59Gwu3ed53Ct/Qcq9bDIX3TOALPdQ+NaRkapT2w0twQIDAQAB";
    public static final String DATEFORMATE = "dd/MM/yyyy";
    public static final String USER = "USER";
    public static final String STASH_USER = "STASH_USER";
    public static final String IS_VIP = "IS_VIP";
    public static final String TRANSACTIONS = "TRANSACTIONS";
    public static final String IS_MODE_CHANGE = "IS_MODE_CHANGE";
    public static final String EURO_SYMBOL = "€";
    public static final String LIFETIME = "com.moutamid.tiptop.lifetime";
    public static final String MONTHLY = "com.moutamid.tiptop.monthly";
    public static final String REQ = "REQ";
    public static final String PAY = "PAY";
    public static final String WITHDRAW = "DRA";

    public static String getFormatedDate(long date) {
        return new SimpleDateFormat(DATEFORMATE, Locale.getDefault()).format(date);
    }

    public static void initDialog(Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
    }

    public static void showDialog() {
        dialog.show();
    }

    public static void dismissDialog() {
        dialog.dismiss();
    }

    public static void checkApp(Activity activity) {
        String appName = "tiptop";

        new Thread(() -> {
            URL google = null;
            try {
                google = new URL("https://raw.githubusercontent.com/Moutamid/Moutamid/main/apps.txt");
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if ((input = in != null ? in.readLine() : null) == null) break;
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            try {
                JSONObject myAppObject = new JSONObject(htmlData).getJSONObject(appName);

                boolean value = myAppObject.getBoolean("value");
                String msg = myAppObject.getString("msg");

                if (value) {
                    activity.runOnUiThread(() -> {
                        new AlertDialog.Builder(activity)
                                .setMessage(msg)
                                .setCancelable(false)
                                .show();
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).start();
    }

    public static FirebaseAuth auth() {
        return FirebaseAuth.getInstance();
    }

    public static DatabaseReference databaseReference() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("tiptop");
        db.keepSynced(true);
        return db;
    }

    public static StorageReference storageReference(String auth) {
        StorageReference sr = FirebaseStorage.getInstance().getReference().child("tiptop").child(auth);
        return sr;
    }

    public static String getUserFirstName() {

        UserModel sn = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);
        String name = "";
        String[] n;
        if (sn != null) {
            if (sn.getName().contains(" ")) {
                n = sn.getName().split(" ");
                name = n[0];
            }
        }

        return name + "!";
    }


    public static String decimalFormat(double walletMoney) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(walletMoney);
    }
}
