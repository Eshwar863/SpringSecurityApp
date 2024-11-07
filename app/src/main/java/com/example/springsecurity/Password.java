package com.example.springsecurity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

                Log.d("PasswordActivity", "Password entered: " + password);  // Log entered password
                Log.d("PasswordActivity", "Re-entered password: " + reenteredPassword);  // Log re-entered password

                if (password.equals(reenteredPassword)) {
                    Log.d("PasswordActivity", "Passwords match, calling forgotpass method.");
                    forgotpass(password, reenteredPassword);
                } else {
                    Log.e("PasswordActivity", "Passwords don't match.");
                    Toast.makeText(Password.this, "Password Doesn't Match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("jwttoken", null);
        Log.d("PasswordActivity", "Token retrieved from SharedPreferences: " + token);  // Log the token
        return token;
    }

    private void forgotpass(String password, String reenterPassword) {
        String token = getTokenFromSharedPreferences();

        if (token == null || token.isEmpty()) {
            Log.e("PasswordActivity", "Token is unavailable.");
            Toast.makeText(Password.this, "Token is unavailable", Toast.LENGTH_SHORT).show();
            return;
        }

        ForgotPassword forgotPassword = new ForgotPassword(password, reenterPassword);

        Log.d("PasswordActivity", "Calling API with token: " + token);  // Log the token used for API call

        Call<ApiResponse> call = apiService.forgotpassword("Bearer " + token, forgotPassword);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("PasswordActivity", "API response received: " + response.code());  // Log the response code

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    Log.d("PasswordActivity", "Password updated successfully, API message: " + apiResponse.getMessage());

                    // Clear the shared preferences (if needed)
                    SharedPreferences preferences = getSharedPreferences("Login", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    Toast.makeText(Password.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Password.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("PasswordActivity", "Failed to update password, response code: " + response.code());
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
                Log.e("PasswordActivity", "Request failed: " + t.getMessage());  // Log the failure
                Toast.makeText(Password.this, "Request timeout or network failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
