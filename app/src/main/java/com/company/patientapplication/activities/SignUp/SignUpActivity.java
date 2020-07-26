package com.company.patientapplication.activities.SignUp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.company.patientapplication.Models.Registration.RegistrationRequest;
import com.company.patientapplication.Models.Registration.RegistrationResponse;
import com.company.patientapplication.Network.NetworkUtil;
import com.company.patientapplication.R;
import com.company.patientapplication.Utills.Constant;
import com.company.patientapplication.Utills.Validation;
import com.company.patientapplication.activities.Home.HomeActivity;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.view.View.GONE;
import static com.company.patientapplication.Utills.Constant.ErrorDialog;
import static com.company.patientapplication.Utills.Constant.errorBuildDialog;

public class SignUpActivity extends AppCompatActivity {
    Button SignUpButton;
    String date;
    EditText Birthday;
    DatePicker datePicker;
    Button done;
    EditText nameTextView;
    EditText passwordTextView;
    EditText confirmPasswordTextView;
    EditText addressTextView;
    EditText mobileTextView;
    RadioButton maleRadioButton;
    RadioButton femaleRadioButton;
    private SharedPreferences mSharedPreferences;
    private CompositeSubscription mSubscriptions;
    String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mSharedPreferences = getSharedPreferences("MySharedPreference", MODE_PRIVATE);
        mSubscriptions = new CompositeSubscription();

        datePicker = findViewById(R.id.datePicker);
        done = findViewById(R.id.done);
        Birthday = findViewById(R.id.Birthday);
        nameTextView = findViewById(R.id.name_text_view);
        passwordTextView = findViewById(R.id.password_text_view);
        confirmPasswordTextView = findViewById(R.id.confirm_password_text_view);
        addressTextView = findViewById(R.id.text_view_address);
        mobileTextView = findViewById(R.id.mobile_text_view);
        maleRadioButton = findViewById(R.id.male_radio_button);
        femaleRadioButton = findViewById(R.id.female_radio_button);
        SignUpButton = findViewById(R.id.sign_up_button);

        datePicker.setVisibility(GONE);
        done.setVisibility(GONE);

        Birthday.setOnClickListener(view -> {

            datePicker.setVisibility(View.VISIBLE);
            done.setVisibility(View.VISIBLE);
        });
        done.setOnClickListener(view -> {
            date = datePicker.getMonth() + 1 + "-" + datePicker.getDayOfMonth() + "-" + datePicker.getYear();
            Birthday.setText(date);
            done.setVisibility(GONE);
            datePicker.setVisibility(GONE);
        });
        SignUpButton.setOnClickListener(v -> {

            if (maleRadioButton.isChecked()) {
                gender = "Male";
            } else {
                gender = "Female";
            }

            validation(nameTextView.getText().toString(), passwordTextView.getText().toString(), Birthday.getText().toString()
                    , confirmPasswordTextView.getText().toString(), addressTextView.getText().toString(), gender, mobileTextView.getText().toString()
            );
        });
    }

    void validation(String name, String password, String birthdate,
                    String confirmPassword, String address, String gender, String mobile) {
        if (!Validation.validateFields(name)) {
            errorBuildDialog(this, "Name is Empty", "Error");
        } else if (!Validation.validateFields(password)) {
            errorBuildDialog(this, "Password is Empty", "Error");
        } else if (!Validation.validateBirthDate(birthdate)) {
            errorBuildDialog(this, "BirthDate is Empty", "Error");
        } else if (!Validation.validatePassword(password)) {
            errorBuildDialog(this, "Password must be greater than 6", "Error");
        } else if (!Validation.validateFields(confirmPassword)) {
            errorBuildDialog(this, "confirmed Password is Empty", "Error");
        } else if (!Validation.validatePassword(confirmPassword)) {
            errorBuildDialog(this, "confirmed Password must be greater than 6", "Error");
        } else if (!password.equals(confirmPassword)) {
            errorBuildDialog(this, "password not match", "Error");
        } else if (!Validation.validateFields(address)) {
            errorBuildDialog(this, "address is Empty", "Error");
        } else if (!Validation.validateFields(mobile)) {
            errorBuildDialog(this, "phone is Empty", "Error");
        } else if (!Validation.validatePhone(mobile)) {
            errorBuildDialog(this, "please check phone", "Error");
        } else {

            sendData();


        }
    }

    private void sendData() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setImageUrl("asdas");
        registrationRequest.setAddress(addressTextView.getText().toString());
        registrationRequest.setName(nameTextView.getText().toString());
        registrationRequest.setMobileNumber(mobileTextView.getText().toString());
        registrationRequest.setGender(this.gender);
        registrationRequest.setPassword(passwordTextView.getText().toString());
        registrationRequest.setBirthDate(Birthday.getText().toString());
        //registrationRequest.setDeviceToken(mSharedPreferences.getString(Constant.notificationToken, ""));


        if (Validation.isConnected(this)) {
            mSubscriptions.add(NetworkUtil.getRetrofitNoHeader()
                    .Registration(registrationRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError));
        } else {
            Constant.buildDialog(this);
        }
    }

    private void handleError(Throwable throwable) {
        // Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
        Log.e("RegistrationThrowable", throwable.getMessage(), throwable);
    }

    private void handleResponse(RegistrationResponse registrationResponse) {
        switch (registrationResponse.getResultCode()) {
            case "1":
                SharedPreferences.Editor edit = mSharedPreferences.edit();
                edit.putString(Constant.accessToken, registrationResponse.getData());
                edit.apply();
                Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                finish();
                startActivity(intent);
                break;
            case "0":
            case "2":
                // Toast.makeText(this, registrationResponseModel.getResultMessageEn(), Toast.LENGTH_SHORT).show();
                Log.e("RegistrationServerError", registrationResponse.getResultCode() + "-" + registrationResponse.getResultMessageEn());
                break;
        }
    }
}
