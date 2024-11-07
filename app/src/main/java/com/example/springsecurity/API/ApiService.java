package com.example.springsecurity.API;

import com.example.springsecurity.Model.ApiResponse;
import com.example.springsecurity.Model.EmailVerificationResponse;
import com.example.springsecurity.Model.ForgotPassword;
import com.example.springsecurity.Model.LoginRequest;
import com.example.springsecurity.Model.RegisterResponce;
import com.example.springsecurity.Model.UpdateDetails;
import com.example.springsecurity.Model.UserRegister;
import com.example.springsecurity.Model.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("api/auth/login")
    Call<String> login(@Body LoginRequest loginRequest);
    @POST("api/register")
    Call<RegisterResponce>register(@Body UserRegister userRegister);
    @GET("api/alive")
    Call<String>alive();
    @GET("api/home")
    Call<String>Home();
    @GET("api/users")
    Call<List<Users>> AllUsers(@Header("Authorization") String token);
    @GET("api/user/{username}")
    Call<Users> getUser(@Header("Authorization") String token, @Path("username") String userName);
    @POST("api/mail/forgotpassword")
    Call<ApiResponse> forgotpassword(@Header("Authorization") String token, @Body ForgotPassword forgotPassword);
    @PUT("api/updateuserdetails")
    Call<Users> update(@Header("Authorization") String token, @Body Users users);
    @POST("api/mail/valid/{otp}/{username}")
    Call<EmailVerificationResponse> valid(@Path("otp")String otp, @Path("username") String userName);
    @POST("api/mail/verifymail/{email}")
    Call<EmailVerificationResponse> verifyEmail(@Path ("email") String email);

}
