package com.moutamid.tiptop.utilis;

public interface ChargeService {
/*
    @POST("/chargeForCookie")
    Call<Void> charge(@Body ChargeRequest request);

    class ChargeErrorResponse {
        String errorMessage;
    }
*/

    class ChargeRequest {
        final String nonce;

        ChargeRequest(String nonce) {
            this.nonce = nonce;
        }
    }
}
