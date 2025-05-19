package com.example.springsecurity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.example.springsecurity.API.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashScreen extends AppCompatActivity {
    Gson gson;
    Retrofit retrofit;
    private ApiService apiService;
    private ProgressBar progressbarsplashscreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://springsecurity-latest-om3m.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        apiService = retrofit.create(ApiService.class);
        progressbarsplashscreen = findViewById(R.id.progressbarsplashscreen);
       // progressbarsplashscreen.setVisibility(View.VISIBLE);
        alive();
    }

    private void alive(){
        Call<String> call =apiService.alive();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body()!=null){
                    SharedPreferences preferences = getSharedPreferences("Login" , MODE_PRIVATE);
                    String token = preferences.getString("jwttoken",null);
                    if(token == null){
                        Intent intent = new Intent(SplashScreen.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        progressbarsplashscreen.setVisibility(View.VISIBLE);
                        Intent Mainscreen = new Intent(SplashScreen.this , MainActivity.class);
                    startActivity(Mainscreen);
                    finish();
                    }

                }else {
                    progressbarsplashscreen.setVisibility(View.VISIBLE);
                    Toast.makeText(SplashScreen.this, "request time out", Toast.LENGTH_SHORT).show();
                    showRetryDialog();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showRetryDialog();
            }
        });

    }

    private void showRetryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
        builder.setTitle("Request Failed");
        builder.setMessage("Unable to connect to the server. Would you like to retry?");
        builder.setCancelable(false); // Make sure the dialog can't be dismissed without choosing an option

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressbarsplashscreen.setVisibility(View.VISIBLE);
                alive();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showRetryDialogExit();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void showRetryDialogExit() {
        AlertDialog.Builder showRetryDialogExit = new AlertDialog.Builder(SplashScreen.this);
        showRetryDialogExit.setTitle("Exit");
        showRetryDialogExit.setMessage("Do you want to Exit?");
        showRetryDialogExit.setCancelable(false);
        showRetryDialogExit.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        showRetryDialogExit.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alive();
                dialog.dismiss();
            }
        });

        AlertDialog dialog = showRetryDialogExit.create();
        dialog.show();

    }
}
