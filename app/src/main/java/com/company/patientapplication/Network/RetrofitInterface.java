package com.company.patientapplication.Network;


import com.company.patientapplication.Models.AddReview.AddReviewRequest;
import com.company.patientapplication.Models.AddReview.AddReviewResponse;
import com.company.patientapplication.Models.ChangeUserPassword.ChangeUserPasswordRequest;
import com.company.patientapplication.Models.ChangeUserPassword.ChangeUserPasswordResponse;
import com.company.patientapplication.Models.ConfirmReservation.ConfirmReservationRequest;
import com.company.patientapplication.Models.ConfirmReservation.ConfirmReservationResponse;
import com.company.patientapplication.Models.DeviceToken.UpdateDeviceTokenRequest;
import com.company.patientapplication.Models.DeviceToken.UpdateDeviceTokenResponse;
import com.company.patientapplication.Models.GetClinicInfoByClinicId.GetClinicInfoByClinicIdResponse;
import com.company.patientapplication.Models.GetClinics.GetClinicsResponse;
import com.company.patientapplication.Models.GetDoctorInfo.GetDoctorInfoResponse;
import com.company.patientapplication.Models.GetEstimatedReservationTime.GetEstimatedReservationTimeRequest;
import com.company.patientapplication.Models.GetEstimatedReservationTime.GetEstimatedReservationTimeResponse;
import com.company.patientapplication.Models.Login.LoginRequestModel;
import com.company.patientapplication.Models.Login.LoginResponseModel;
import com.company.patientapplication.Models.Registration.RegistrationRequest;
import com.company.patientapplication.Models.Registration.RegistrationResponse;
import com.company.patientapplication.Models.Reviews.ReviewsResponse;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public interface RetrofitInterface {


    @POST("UserAccount/Login")
    Observable<LoginResponseModel> Login(@Body LoginRequestModel loginRequestModel);

    @POST("UserAccount/Registration")
    Observable<RegistrationResponse> Registration(@Body RegistrationRequest registrationRequest);


    @GET("DoctorAccount/GetDoctorInfoByDoctorId/{id}")
    Observable<GetDoctorInfoResponse> GetDoctorInfoByDoctorId(@Path("id") String id);

    @POST("UserAccount/ChangeUserPassword")
    Observable<ChangeUserPasswordResponse> ChangeUserPassword(@Body ChangeUserPasswordRequest changeUserPasswordRequest);

    @POST("Reservation/GetEstimatedReservationTime")
    Observable<GetEstimatedReservationTimeResponse> GetEstimatedReservationTime
            (@Body GetEstimatedReservationTimeRequest getEstimatedReservationTimeRequest);

    @GET("Clinic/GetClinicInfoByClinicId/{id}")
    Observable<GetClinicInfoByClinicIdResponse> GetClinicInfoByClinicId(@Path("id") String id);


    @POST("Reservation/ConfirmReservation")
    Observable<ConfirmReservationResponse> ConfirmReservation(@Body ConfirmReservationRequest confirmReservationRequest);


    @POST("RateAndReview/AddReview")
    Observable<AddReviewResponse> AddReview(@Body AddReviewRequest addReviewRequest);


    @GET("RateAndReview/GetAllReviews")
    Observable<ReviewsResponse> GetAllReviews();

    @GET("Clinic/GetClinicsByDoctorId/1")
    Observable<GetClinicsResponse> GetClinics();


    @PUT("DonatorAccount/UpdateDonatorDeviceToken")
    Observable<UpdateDeviceTokenResponse> UpdateDonatorDeviceToken(@Body UpdateDeviceTokenRequest updateDeviceTokenRequest);

}