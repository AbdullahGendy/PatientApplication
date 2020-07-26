package com.company.patientapplication.Models.AddReview;

public class AddReviewResponse {
    private String ResultCode;
    private String ResultMessageEn;
    private String Data;

    public String getResultCode() {
        return ResultCode;
    }

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public String getResultMessageEn() {
        return ResultMessageEn;
    }

    public void setResultMessageEn(String resultMessageEn) {
        ResultMessageEn = resultMessageEn;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
