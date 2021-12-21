package com.splat.data.DTO.request;

public class DeleteReviewRequest {
    private String sessionToken;
    private String restName;

    public DeleteReviewRequest() {

    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }
}
