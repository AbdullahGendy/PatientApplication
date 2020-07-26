package com.company.patientapplication.activities.Splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.company.patientapplication.R;
import com.company.patientapplication.Utills.Constant;
import com.company.patientapplication.Utills.Validation;
import com.company.patientapplication.activities.Home.HomeActivity;
import com.company.patientapplication.activities.Login.LoginActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import rx.subscriptions.CompositeSubscription;


public class Splash extends AppCompatActivity {

    SharedPreferences mSharedPreferences;
    CompositeSubscription mSubscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mSubscriptions = new CompositeSubscription();
        mSharedPreferences = getSharedPreferences("MySharedPreference", MODE_PRIVATE);

        /*if (Validation.isConnected(this)) {
            if (mSharedPreferences.getString(Constant.accessToken, "").equals("")) {
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
                    String newToken = instanceIdResult.getToken();
                    if (newToken != null) {
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putString(Constant.notificationToken, newToken);
                        editor.apply();
                    }

                });
            }
        }*/
        new Handler().postDelayed(() -> {
            Intent intent;
            if ((mSharedPreferences.getBoolean(Constant.rememberMe, false))) {
                intent = new Intent(Splash.this, HomeActivity.class);
            } else {
                intent = new Intent(Splash.this, LoginActivity.class);
            }
            finish();
            startActivity(intent);
        }, 3000);
    }

}
