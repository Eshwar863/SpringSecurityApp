package com.example.springsecurity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.springsecurity.API.ApiService;
import com.example.springsecurity.Model.LoginRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private EditText editUsername;
    private EditText editpass;
    private Button RegisterBT, forgotpasswordbt;
    private Button LoginBT;
    private Button button;
    private ApiService apiService;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        editUsername = findViewById(R.id.editTextuserNameLogin);
        editpass = findViewById(R.id.editTextPasswordlogin);
        RegisterBT =  findViewById(R.id.buttonRegisterLogin);
        forgotpasswordbt =  findViewById(R.id.buttonForgotPassword);
        LoginBT =  findViewById(R.id.buttonLogin);
        progressBar = findViewById(R.id.progressBar);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://springsecurity-latest-om3m.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        apiService = retrofit.create(ApiService.class);
        LoginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username =editUsername.getText().toString().trim();
                String password =editpass.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter Username and Password", Toast.LENGTH_SHORT).show();
                } else {
                    sendLoginReq(username, password);
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });

        forgotpasswordbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ForgotpasswordPage = new Intent(LoginActivity.this ,ForgotPasswordActivity.class);
                startActivity(ForgotpasswordPage);
            }
        });
        RegisterBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegisterPage = new Intent(LoginActivity.this ,UserRegistration.class);
                startActivity(RegisterPage);
                finish();
            }
        });
    }

    private void sendLoginReq(String username , String password){
        LoginRequest loginRequest = new LoginRequest(username,password);
        Call<String> call = apiService.login(loginRequest);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful() && response.body()!= null){
                String token = response.body().trim();
                Toast.makeText(LoginActivity.this,"Success",Toast.LENGTH_SHORT);
                SharedPreferences preferences = getSharedPreferences("Login",MODE_PRIVATE);
                SharedPreferences.Editor editor =preferences.edit();
                editor.putString("jwttoken",token);
                editor.putString("username",username);
                editor.apply();
                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                progressBar.setVisibility(View.GONE);
                }
                else{
                    if(response.body() == null){
                        Toast.makeText(LoginActivity.this, "Invalid User Credentials", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        showDailog("Invalid Credentials","Incorrect UserName or Password");
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Responce Time out", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Failed to Login", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showDailog(String title,String msg) {
        AlertDialog.Builder showRetryDialogExit = new AlertDialog.Builder(LoginActivity.this);
        showRetryDialogExit.setTitle(title);
        showRetryDialogExit.setMessage(msg);
        showRetryDialogExit.setCancelable(true);
        showRetryDialogExit.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = showRetryDialogExit.create();
        dialog.show();

    }
}