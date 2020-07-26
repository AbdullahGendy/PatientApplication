package com.company.patientapplication.Models.ChangeUserPassword;

public class ChangeUserPasswordRequest {

    private String UserOldPassword;

    private String UserNewPassword;

    private String UserId;

    public String getUserOldPassword ()
    {
        return UserOldPassword;
    }

    public void setUserOldPassword (String UserOldPassword)
    {
        this.UserOldPassword = UserOldPassword;
    }

    public String getUserNewPassword ()
    {
        return UserNewPassword;
    }

    public void setUserNewPassword (String UserNewPassword)
    {
        this.UserNewPassword = UserNewPassword;
    }

    public String getUserId ()
    {
        return UserId;
    }

    public void setUserId (String UserId)
    {
        this.UserId = UserId;
    }

}
