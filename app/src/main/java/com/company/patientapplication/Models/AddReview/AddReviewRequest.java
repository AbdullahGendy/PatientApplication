package com.company.patientapplication.Models.AddReview;

public class AddReviewRequest {
    private String UserId;
    private int RateValue;
    private String Review;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public int getRateValue() {
        return RateValue;
    }

    public void setRateValue(int rateValue) {
        RateValue = rateValue;
    }

    public String getReview() {
        return Review;
    }

    public void setReview(String review) {
        Review = review;
    }
}
