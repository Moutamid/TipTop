package com.moutamid.tiptop;

import android.app.Application;

import com.fxn.stash.Stash;

public class MyApplication extends Application {

//    private ChargeCall.Factory chargeCallFactory;

    @Override
    public void onCreate() {
        super.onCreate();
        Stash.init(this);

/*
        Retrofit retrofit = ConfigHelper.createRetrofitInstance();
        chargeCallFactory = new ChargeCall.Factory(retrofit);

        CardEntryBackgroundHandler cardHandler = new CardEntryBackgroundHandler(chargeCallFactory, getResources());
        CardEntry.setCardNonceBackgroundHandler(cardHandler);
*/

    }
}
