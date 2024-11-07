package com.example.springsecurity;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.springsecurity.API.ApiService;
import com.example.springsecurity.Model.ApiResponse;
import com.example.springsecurity.Model.ForgotPassword;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Password extends AppCompatActivity {
    private ApiService apiService;
    private Gson gson;
    private Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password);

        gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://finalssecurity1-v1-0.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        apiService = retrofit.create(ApiService.class);

        EditText passwordEditText = findViewById(R.id.password);
        EditText reenterPasswordEditText = findViewById(R.id.reenter_password);
        Button submitButton = findViewById(R.id.submit_button);


        TextView passwordLabel = findViewById(R.id.password_label);
        TextView reenterPasswordLabel = findViewById(R.id.reenter_password_label);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = passwordEditText.getText().toString();
                String reenteredPassword = reenterPasswordEditText.getText().toString();


                if (password.equals(reenteredPassword)) {
                    forgotpass(password,reenteredPassword);
                } else {
                    Toast.makeText(Password.this, "Password Doesn't Match", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }






    private String getTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        return sharedPreferences.getString("jwttoken", null);
    }


    private void forgotpass(String password, String reenterPassword) {
        String token = getTokenFromSharedPreferences();

        if (token == null || token.isEmpty()) {

            Toast.makeText(Password.this, "Token is unavailable", Toast.LENGTH_SHORT).show();
            return;
        }

        ForgotPassword forgotPassword = new ForgotPassword(password, reenterPassword);

        Call<ApiResponse> call = apiService.forgotpassword("Bearer " + token, forgotPassword);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()&&response.body() !=null) {

                    if (response.body() != null) {

                        ApiResponse apiResponse = response.body();
                        //Toast.makeText(Password.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                        SharedPreferences preferences = getSharedPreferences("Login",MODE_PRIVATE);
                        SharedPreferences.Editor editor =preferences.edit();
                        editor.clear();
                        editor.apply();
                        Toast.makeText(Password.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();

//                        Intent intent =  new Intent(Password.this, LoginActivity.class);
//                        startActivity(intent);
//                        finish();
                    } else {
                        Toast.makeText(Password.this, "Empty response from the server", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    if (response.code() == 400) {
                        Toast.makeText(Password.this, "Bad request: Failed to update password", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 504) {
                        Toast.makeText(Password.this, "Gateway timeout: Failed to update password", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Password.this, "Unable to process request: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

                Toast.makeText(Password.this, "Request timeout or network failure", Toast.LENGTH_SHORT).show();
            }
        });
    }






}