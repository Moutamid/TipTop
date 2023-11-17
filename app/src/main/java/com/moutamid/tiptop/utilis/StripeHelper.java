package com.moutamid.tiptop.utilis;

import android.content.Context;

import com.moutamid.tiptop.models.UserModel;
import com.stripe.Stripe;

import java.util.HashMap;
import java.util.Map;

public class StripeHelper {
    UserModel model;
    Context context;

    public StripeHelper(UserModel model, Context context) {
        this.model = model;
        this.context = context;
    }

    public void stripe() {

        Stripe.apiKey = "sk_test_26PHem9AhJZvU623DfE1x4sd";


        Map<String, Object> cardPayments =
                new HashMap<>();
        cardPayments.put("requested", true);
        Map<String, Object> transfers = new HashMap<>();
        transfers.put("requested", true);
        Map<String, Object> capabilities =
                new HashMap<>();
        capabilities.put("card_payments", cardPayments);
        capabilities.put("transfers", transfers);
        Map<String, Object> support_address = new HashMap<>();
        support_address.put("city", model.getCompany());
        support_address.put("country", "US");
        support_address.put("line1", "300 North Akard Street");
        support_address.put("postal_code", "75201");
        support_address.put("state", "TX");
        Map<String, Object> params = new HashMap<>();
        params.put("type", "custom");
        params.put("country", "US");
        params.put("email", model.getEmail());
        params.put("business_type", "individual");
        Map<String, Object> business_profile = new HashMap<>();
        business_profile.put("mcc", "5999");
        business_profile.put("product_description", "glass art. Cigar cases.\\nCustomers will pay when they purchase");
        business_profile.put("support_address", support_address);
//        business_profile.put("support_phone", model.getPhone());
        business_profile.put("url", "https://www.instagram.com/lux_sesh_supply/");
//        String last4 = accountNo.substring(accountNo.length()-4);
        Map<String, Object> company = new HashMap<>();
        company.put("address", support_address);
        company.put("owners_provided", false);
//        company.put("phone", model.getPhone());

        Map<String, Object> bankAccount = new HashMap<>();
        bankAccount.put("object", "bank_account");
        bankAccount.put("country", "US");
        bankAccount.put("currency", "usd");
//        bankAccount.put("account_holder_name",
//                fname
//        );
//
//        bankAccount.put(
//                "account_holder_type",
//                stype
//        );
//        bankAccount.put("routing_number", routing_number);
//        bankAccount.put("account_number", accountNo);
//        bankAccount.put("last4", last4);
//

    }

}
