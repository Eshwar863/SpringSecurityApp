package com.example.springsecurity.API;

import com.example.springsecurity.Model.LoginRequest;
import com.example.springsecurity.Model.RegisterResponce;
import com.example.springsecurity.Model.UserRegister;
import com.example.springsecurity.Model.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

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
}
