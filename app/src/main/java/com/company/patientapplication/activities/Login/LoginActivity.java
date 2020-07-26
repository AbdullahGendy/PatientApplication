package com.company.patientapplication.activities.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.company.patientapplication.Models.DeviceToken.UpdateDeviceTokenResponse;
import com.company.patientapplication.Models.Login.LoginRequestModel;
import com.company.patientapplication.Models.Login.LoginResponseModel;
import com.company.patientapplication.Network.NetworkUtil;
import com.company.patientapplication.R;
import com.company.patientapplication.Utills.Constant;
import com.company.patientapplication.Utills.Validation;
import com.company.patientapplication.activities.Home.HomeActivity;
import com.company.patientapplication.activities.SignUp.SignUpActivity;

import java.util.Objects;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.company.patientapplication.Utills.Constant.ErrorDialog;
import static com.company.patientapplication.Utills.Constant.errorBuildDialog;

public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    SharedPreferences mSharedPreferences;
    TextView textViewSignUp;
    AppCompatEditText username_text_view;
    AppCompatEditText password_text_view;
    CheckBox rememberMeCheckBox;
    String mAccessToken;
    private CompositeSubscription mSubscriptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mSubscriptions = new CompositeSubscription();
        mSharedPreferences = getSharedPreferences("MySharedPreference", MODE_PRIVATE);

        rememberMeCheckBox = findViewById(R.id.remember_me_check_box);
        loginButton = findViewById(R.id.login_button);
        textViewSignUp = findViewById(R.id.text_view_sign_up);
        username_text_view = findViewById(R.id.username_text_view);
        password_text_view = findViewById(R.id.password_text_view);
        textViewSignUp.setOnClickListener(view -> {

            Intent intent = new Intent(this, SignUpActivity.class);
            finish();
            startActivity(intent);
        });
        loginButton.setOnClickListener(v -> validation(Objects.requireNonNull(username_text_view.getText()).toString(),
                Objects.requireNonNull(password_text_view.getText()).toString()));


    }

    void validation(String userName, String password) {
        if (!Validation.validateFields(userName)) {
            errorBuildDialog(this, "username is Empty", "Error");
        } else if (!Validation.validateFields(password)) {
            errorBuildDialog(this, "Password is Empty", "Error");
        } else {
            if (Validation.isConnected(this)) {
                LoginRequestModel loginRequestModel = new LoginRequestModel();
                loginRequestModel.setName(userName);
                loginRequestModel.setPassword(password);

                mSubscriptions.add(NetworkUtil.getRetrofitNoHeader()
                        .Login(loginRequestModel)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::handleResponse, this::handleErrorLogin));
            } else {
                Constant.buildDialog(this);
            }
        }
    }

    private void handleErrorLogin(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
        Log.e("LoginThrowable", throwable.getMessage(), throwable);
    }

    private void handleError(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
        Log.e("NewTokenThrowable", throwable.getMessage(), throwable);
    }

    private void handleResponse(LoginResponseModel loginResponseModel) {
        switch (loginResponseModel.getResultCode()) {
            case "1":
               /* UpdateDeviceTokenRequest updateDeviceTokenRequest = new UpdateDeviceTokenRequest();
                updateDeviceTokenRequest.setDonatorId(loginResponseModel.getData());
                mAccessToken = loginResponseModel.getData();
                updateDeviceTokenRequest.setDeviceToken(mSharedPreferences.getString(Constant.notificationToken, ""));
                mSubscriptions.add(NetworkUtil.getRetrofitNoHeader()
                        .UpdateDonatorDeviceToken(updateDeviceTokenRequest)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::handleResponseNewToken, this::handleError));*/
                SharedPreferences.Editor edit = mSharedPreferences.edit();
                edit.putBoolean(Constant.rememberMe, rememberMeCheckBox.isChecked());
                edit.putString(Constant.accessToken, loginResponseModel.getData());
                edit.apply();
                Intent intent = new Intent(this, HomeActivity.class);
                finish();
                startActivity(intent);
                break;
            case "0":
                Constant.errorBuildDialog(this, "Username or password is incorrect, Please try again.", "Error");
                Log.e("LoginServerError(0)", loginResponseModel.getResultMessageEn());

                break;
            case "2":
                Log.e("LoginServerError(2)", loginResponseModel.getResultMessageEn());
                break;
        }
    }

    private void handleResponseNewToken(UpdateDeviceTokenResponse updateDeviceTokenResponse) {

        switch (updateDeviceTokenResponse.getResultCode()) {
            case "1":
                //Toast.makeText(this, updateDeviceTokenResponse.getResultMessageEn(), Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor edit = mSharedPreferences.edit();
                edit.putBoolean(Constant.rememberMe, rememberMeCheckBox.isChecked());
                edit.putString(Constant.accessToken, mAccessToken);
                edit.apply();
                Intent intent = new Intent(this, HomeActivity.class);
                finish();
                startActivity(intent);
                break;
            case "0":
                Constant.errorBuildDialog(this, "Username or password is incorrect, Please try again.", "Error");
                Log.e("NewTokenServerError(0)", updateDeviceTokenResponse.getResultMessageEn());

                break;
            case "2":
                Log.e("NewTokenServerError(2)", updateDeviceTokenResponse.getResultMessageEn());
                break;
        }
    }

}
