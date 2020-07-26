package com.company.patientapplication.activities.ChangePassword;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.company.patientapplication.Models.ChangeUserPassword.ChangeUserPasswordRequest;
import com.company.patientapplication.Models.ChangeUserPassword.ChangeUserPasswordResponse;
import com.company.patientapplication.Network.NetworkUtil;
import com.company.patientapplication.R;
import com.company.patientapplication.Utills.Constant;
import com.company.patientapplication.Utills.Validation;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class ChangePasswordActivity extends AppCompatActivity {
    EditText editTextOldPassword;
    EditText editTextNewPassword;
    EditText editTextReEnterNewPassword;
    CompositeSubscription mSubscriptions;
    Button buttonChangePassword;
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mSubscriptions = new CompositeSubscription();
        mSharedPreferences = getSharedPreferences("MySharedPreference", MODE_PRIVATE);

        editTextOldPassword = findViewById(R.id.edit_text_old_password);
        editTextReEnterNewPassword = findViewById(R.id.edit_text_re_enter_new_password);
        editTextNewPassword = findViewById(R.id.edit_text_new_password);
        buttonChangePassword = findViewById(R.id.button_change_password);
        buttonChangePassword.setOnClickListener(view -> {

            validation(editTextOldPassword.getText().toString(),
                    editTextNewPassword.getText().toString(),
                    editTextReEnterNewPassword.getText().toString());
        });

    }

    void validation(String oldPassword, String password, String rePassword) {
        if (!Validation.validateFields(oldPassword)) {
            Constant.errorBuildDialog(this, "username is Empty","Error");
        } else if (!Validation.validateFields(password)) {
            Constant.errorBuildDialog(this, "Password is Empty","Error");
        } else if (!Validation.validateFields(rePassword)) {
            Constant.errorBuildDialog(this, "Re-Password is Empty","Error");
        } else if (!password.equals(rePassword)) {
            Constant.errorBuildDialog(this, "Password is not matched","Error");
        } else {
            if (Validation.isConnected(this)) {
                ChangeUserPasswordRequest changeUserPasswordRequest = new ChangeUserPasswordRequest();
                changeUserPasswordRequest.setUserId(mSharedPreferences.getString(Constant.accessToken, ""));
                changeUserPasswordRequest.setUserOldPassword(oldPassword);
                changeUserPasswordRequest.setUserNewPassword(password);

                mSubscriptions.add(NetworkUtil.getRetrofitNoHeader()
                        .ChangeUserPassword(changeUserPasswordRequest)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::handleResponse, this::handleErrorLogin));
            } else {
                Constant.buildDialog(this);
            }
        }
    }

    private void handleErrorLogin(Throwable throwable) {
        Log.e("ChangePasswordError", throwable.getMessage(), throwable);
    }


    private void handleResponse(ChangeUserPasswordResponse changeUserPasswordResponse) {
        switch (changeUserPasswordResponse.getResultCode()) {
            case "1":
                Log.e("LoginServer(1)", changeUserPasswordResponse.getResultMessageEn());
                Toast.makeText(this, "Password Has been Changed Successfully.", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(this::finish, 1000);
                finish();
                break;
            case "0":
                Toast.makeText(this, "Some Data is incorrect,Try Again.", Toast.LENGTH_SHORT).show();

                Constant.errorBuildDialog(this, "Username or password is incorrect, Please try again.", "Error");
                Log.e("LoginServerError(0)", changeUserPasswordResponse.getResultMessageEn());

                break;
            case "2":
                Toast.makeText(this, "Some Data is incorrect,Try Again.", Toast.LENGTH_SHORT).show();
                Log.e("LoginServerError(2)", changeUserPasswordResponse.getResultMessageEn());
                break;
        }
    }

}
