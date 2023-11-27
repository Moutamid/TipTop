package com.moutamid.tiptop.stripe;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fxn.stash.Stash;
import com.moutamid.tiptop.utilis.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class ExchangeAuthorizationCodeForAccessToken {

    // Test Account acct_1OEsy2Q5Jr5Uj6cU

    // https://connect.stripe.com/d/setup/e/_P2yvxcfvsUJPcgO9pNILvxUr17/YWNjdF8xT0VzeTJRNUpyNVVqNmNV/f4f6f22db863b6db0
    private static String AUTHORIZATION_CODE = "YOUR_AUTHORIZATION_CODE";
    private static final String CLIENT_ID = "ca_M7FdK9OE1HrByywvZCoFLAA0pZqKyj4l";
    private static final String CLIENT_SECRET = Constants.API_KEY;
    private static final String REDIRECT_URI = "https://google.com";
    private static final String TOKEN_ENDPOINT = "https://api.example.com/oauth/token";

    public ExchangeAuthorizationCodeForAccessToken(String AUTHORIZATION_CODE) {
        this.AUTHORIZATION_CODE = AUTHORIZATION_CODE;
    }

    public static void exchangeAuthorizationCode(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("grant_type", "authorization_code");
            requestBody.put("code", AUTHORIZATION_CODE);
            requestBody.put("client_id", CLIENT_ID);
            requestBody.put("client_secret", CLIENT_SECRET);
            requestBody.put("redirect_uri", REDIRECT_URI);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, TOKEN_ENDPOINT,
                requestBody, response -> {
            try {
                String accessToken = response.getString("access_token");
                Stash.put(Constants.ACCESS_TOKEN, accessToken);
                Toast.makeText(context, "Bank details Added", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(context, "Authorization Failed", Toast.LENGTH_SHORT).show();
            Log.e("ExchangeAuthorizationCode", "Error exchanging authorization code:", error);
        });

        queue.add(request);
    }
}
