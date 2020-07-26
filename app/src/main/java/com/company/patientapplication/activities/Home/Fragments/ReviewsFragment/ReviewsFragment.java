package com.company.patientapplication.activities.Home.Fragments.ReviewsFragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.company.patientapplication.Models.AddReview.AddReviewRequest;
import com.company.patientapplication.Models.AddReview.AddReviewResponse;
import com.company.patientapplication.Network.NetworkUtil;
import com.company.patientapplication.R;
import com.company.patientapplication.Utills.Constant;
import com.company.patientapplication.Utills.Validation;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.Objects;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.content.Context.MODE_PRIVATE;


public class ReviewsFragment extends Fragment {
    private View view;
    private CompositeSubscription mSubscriptions;
    private SharedPreferences mSharedPreferences;
    private EditText textViewReviewComment;
    private SimpleRatingBar simpleRatingBarReviewDoctor;
    private Button reviewButton;
    private int rating;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reviews, container, false);
        textViewReviewComment = view.findViewById(R.id.text_view_review_comment);
        mSubscriptions = new CompositeSubscription();
        simpleRatingBarReviewDoctor = view.findViewById(R.id.simple_rating_bar_review_doctor);
        reviewButton = view.findViewById(R.id.review_button);
        simpleRatingBarReviewDoctor.setOnRatingBarChangeListener((simpleRatingBar, rating, fromUser) -> {
            if (fromUser) {
                this.rating = ((int) rating);
            }
        });
        reviewButton.setOnClickListener(view1 -> {

            if (Validation.isConnected(Objects.requireNonNull(getActivity()))) {
                mSharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("MySharedPreference", MODE_PRIVATE);
                AddReviewRequest addReviewRequest = new AddReviewRequest();
                addReviewRequest.setRateValue(rating);
                addReviewRequest.setReview(textViewReviewComment.getText().toString());
                addReviewRequest.setUserId(mSharedPreferences.getString(Constant.accessToken, ""));

                mSubscriptions.add(NetworkUtil.getRetrofitNoHeader()
                        .AddReview(addReviewRequest)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::handleResponse, this::handleError));
            } else {
                Constant.buildDialog(getActivity());
            }
        });

        return view;
    }

    private void handleError(Throwable throwable) {
        Log.e("AddReviewThrowable", throwable.getMessage(), throwable);
    }

    private void handleResponse(AddReviewResponse addReviewResponse) {
        switch (addReviewResponse.getResultCode()) {
            case "1":
                Toast.makeText(getActivity(), "Review Added Successfully", Toast.LENGTH_SHORT).show();
                reviewButton.setVisibility(View.GONE);

                break;
            case "0":
                Constant.errorBuildDialog(getActivity(), addReviewResponse.getResultMessageEn(), "Error");
                Log.e("getClinicInfoError(0)", addReviewResponse.getResultMessageEn());
                break;
            case "2":
                Toast.makeText(getActivity(), addReviewResponse.getResultMessageEn(), Toast.LENGTH_SHORT).show();
                Log.e("getClinicInfoError(2)", addReviewResponse.getResultMessageEn());
                break;
        }

    }


}
