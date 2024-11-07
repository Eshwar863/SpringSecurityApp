package com.example.springsecurity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.springsecurity.API.ApiService;
import com.example.springsecurity.Model.ApiResponse;
import com.example.springsecurity.Model.EmailVerificationResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ApiService apiService;
    private Gson gson;
    private Retrofit retrofit;
    EditText otpField ,emailTf,usernameField;
    Button verifyEmailButton ,verifyOtpButton;
    ApiResponse apiResponse;
    EmailVerificationResponse emailVerificationResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_actvity);

        gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://finalssecurity1-v1-0.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        apiService = retrofit.create(ApiService.class);

         otpField = findViewById(R.id.editTextOtp);
         emailTf = findViewById(R.id.editTextEmail);
         usernameField = findViewById(R.id.editTextUsername);
         verifyEmailButton = findViewById(R.id.buttonVerifyEmail);
         verifyOtpButton = findViewById(R.id.buttonVerifyOtp);

        verifyEmailButton.setOnClickListener(v -> {
            String email = emailTf.getText().toString().trim();
            if (!email.isEmpty()) {
                    verifyEmail(email);

            } else {
                Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show();
            }
        });

        verifyOtpButton.setOnClickListener(v -> {
              String otp  = otpField.getText().toString().trim();
              String username = usernameField.getText().toString().trim();
            valid(otp ,username);
            Toast.makeText(this, "Please Wait", Toast.LENGTH_SHORT).show();
        });
    }

    private void verifyEmail(String email) {
        Call<EmailVerificationResponse> call = apiService.verifyEmail(email);
        call.enqueue(new Callback<EmailVerificationResponse>() {
            @Override
            public void onResponse(Call<EmailVerificationResponse> call, Response<EmailVerificationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {


                     emailVerificationResponse =response.body();
                    showDialog("Message",emailVerificationResponse.getBody() );
                    Toast.makeText(ForgotPasswordActivity.this,emailVerificationResponse.getBody() , Toast.LENGTH_SHORT).show();
                  try {
                      if(emailVerificationResponse.getStatusCodeValue()!= 404){
                      otpField.setVisibility(View.VISIBLE);
                      verifyOtpButton.setVisibility(View.VISIBLE);
                      usernameField.setVisibility(View.VISIBLE);
                      verifyEmailButton.setVisibility(View.GONE);
                  }}
                    catch (Exception e){

                    }

                } else {
                    showDialog("Error",emailVerificationResponse.getBody());
                }
            }

            @Override
            public void onFailure(Call<EmailVerificationResponse> call, Throwable t) {
                Toast.makeText(ForgotPasswordActivity.this, "Request timeout", Toast.LENGTH_SHORT).show();
            }
        });
    }





    private void valid(String otp ,String username) {
        Call<EmailVerificationResponse> call = apiService.valid(otp,username);
        call.enqueue(new Callback<EmailVerificationResponse>() {
            @Override
            public void onResponse(Call<EmailVerificationResponse> call, Response<EmailVerificationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    emailVerificationResponse =response.body();
                    String token = emailVerificationResponse.getBody().trim();

                    SharedPreferences preferences = getSharedPreferences("Login",MODE_PRIVATE);
                    SharedPreferences.Editor editor =preferences.edit();
                    editor.putString("jwttoken",token);
                    editor.apply();
                    Toast.makeText(ForgotPasswordActivity.this, token, Toast.LENGTH_SHORT).show();

                    if(emailVerificationResponse.getStatusCodeValue() == 200) {

                        Toast.makeText(ForgotPasswordActivity.this, "Otp Verified", Toast.LENGTH_SHORT).show();
                        Intent  intent = new Intent(ForgotPasswordActivity.this, Password.class);
                        startActivity(intent);
                        finish();

                    }
                    else {
                        showDialog("Message",emailVerificationResponse.getBody());
                    }
                } else {
                    showDialog("Error",emailVerificationResponse.getBody());
                }
            }

            @Override
            public void onFailure(Call<EmailVerificationResponse> call, Throwable t) {
                Toast.makeText(ForgotPasswordActivity.this, "Request timeout", Toast.LENGTH_SHORT).show();
            }
        });
    }






    private void showDialog(String title, String msg) {
        new AlertDialog.Builder(ForgotPasswordActivity.this)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
