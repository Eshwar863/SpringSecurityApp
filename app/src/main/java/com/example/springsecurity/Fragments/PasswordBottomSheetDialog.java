package com.example.springsecurity.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.springsecurity.API.ApiService;
import com.example.springsecurity.Model.ApiResponse;
import com.example.springsecurity.Model.ForgotPassword;
import com.example.springsecurity.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PasswordBottomSheetDialog extends BottomSheetDialogFragment {
     static final String TAG = "hello"; // Log tag
    private ApiService apiService;
    private EditText passwordEditText;
    private EditText reenterPasswordEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_password, container, false);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://finalssecurity1-v1-0.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(ApiService.class);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        reenterPasswordEditText = view.findViewById(R.id.reenterPasswordEditText);
        Button submitButton = view.findViewById(R.id.submitButton);

        submitButton.setOnClickListener(v -> {
            String password = passwordEditText.getText().toString();
            String reenterPassword = reenterPasswordEditText.getText().toString();

            Log.d(TAG, "Submit button clicked. Password: " + password + ", Re-enter Password: " + reenterPassword);

            if (password.equals(reenterPassword)) {
                Log.d(TAG, "Passwords match, calling forgotpass()");
                forgotpass(password, reenterPassword);
            } else {
                Log.d(TAG, "Passwords do not match.");
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private String getTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("jwttoken", null);
        Log.d(TAG, "Token retrieved from SharedPreferences: " + token);
        return token;
    }

    private void forgotpass(String password, String reenterPassword) {
        String token = getTokenFromSharedPreferences();

        if (token == null || token.isEmpty()) {
            Log.d(TAG, "Token is unavailable.");
            Toast.makeText(getContext(), "Token is unavailable", Toast.LENGTH_SHORT).show();
            return;
        }

        ForgotPassword forgotPassword = new ForgotPassword(password, reenterPassword);
        Log.d(TAG, "Making API call to update password with token: " + token);

        Call<ApiResponse> call = apiService.forgotpassword("Bearer " + token, forgotPassword);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()&&response.body() !=null) {

                    if (response.body() != null) {
                        ApiResponse apiResponse = response.body();
                        Toast.makeText(getContext(), "Password updated successfully!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), "Empty response from the server", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    if (response.code() == 400) {
                        Toast.makeText(getContext(), "Bad request: Failed to update password", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 504) {
                        Toast.makeText(getContext(), "Gateway timeout: Failed to update password", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Unable to process request: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d(TAG, "API call failed. Error: " + t.getMessage());
                Toast.makeText(getContext(), "Request timeout or network failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
