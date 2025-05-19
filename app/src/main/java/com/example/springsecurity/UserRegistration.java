package com.example.springsecurity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.springsecurity.Model.RegisterResponce;
import com.example.springsecurity.Model.UserRegister;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRegistration extends AppCompatActivity {
    private ApiService apiService;
    EditText usernametf,passwordtf ,emailtf;
    Button buttonRegister,backtologin;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_registration);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://springsecurity-latest-om3m.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        progressBar = findViewById(R.id.progressbarregistration);
        apiService = retrofit.create(ApiService.class);
        backtologin = findViewById(R.id.buttonbacktologin);
        buttonRegister = findViewById(R.id.buttonRegister);
        usernametf = findViewById(R.id.editTextuserNameregister);
        passwordtf = findViewById(R.id.editTextPasswordregister);
        emailtf = findViewById(R.id.editTextEmailregister);
        backtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserRegistration.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = usernametf.getText().toString().trim();
                String password = passwordtf.getText().toString().trim();
                String email = emailtf.getText().toString().trim();
                if(username.isEmpty()||password.isEmpty()){
                    Toast.makeText(UserRegistration.this,"Enter UserName and Password",Toast.LENGTH_SHORT).show();
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    userregistration(username,email,password);
                }
            }
        });
    }

    private void userregistration(String username,String email,String password){
        UserRegister userRegister = new UserRegister(username,password,email);
        Call<RegisterResponce> call = apiService.register(userRegister);

        call.enqueue(new Callback<RegisterResponce>() {
            @Override
            public void onResponse(Call<RegisterResponce> call, Response<RegisterResponce> response) {
                if(response.isSuccessful() && response.body()!=null){
                    Log.d("Register", "Raw response: " + response.body());
                    progressBar.setVisibility(View.GONE);
                    String title = "Registration Completed";
                    String msg ="Back to Login";
                    showDailog(title,msg);
                }
                else if(response.isSuccessful() && response.body() == null){
                    progressBar.setVisibility(View.GONE);
                    String title = "Request Timeout";
                    String msg ="Unable to connect to the server.";
                    showDailog(title,msg);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponce> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                String title = "Registration Fail";
                String msg ="Unable to register";
                showDailog(title,msg);
            }
        });
    }


    private void showDailog(String title,String msg) {
        AlertDialog.Builder showRetryDialogExit = new AlertDialog.Builder(UserRegistration.this);
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