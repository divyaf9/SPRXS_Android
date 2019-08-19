package com.divya.sprxs.model;

public class ShowAvailableTokensToOfferResponse {

    private int AvailableTokens;
    private String error;

    public ShowAvailableTokensToOfferResponse(int availableTokens, String error) {
        AvailableTokens = availableTokens;
        this.error = error;
    }

    public int getAvailableTokens() {
        return AvailableTokens;
    }

    public String getError() {
        return error;
    }
}
