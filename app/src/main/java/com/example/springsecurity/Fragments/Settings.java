package com.example.springsecurity.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.springsecurity.API.ApiService;
import com.example.springsecurity.LoginActivity;
import com.example.springsecurity.Model.ForgotPassword;
import com.example.springsecurity.Model.Users;
import com.example.springsecurity.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Settings extends Fragment {
        private ApiService apiService;
        private Button LogoutButton;
        private TextView userNametf,emailtf;
        private TextView changePasswordTextView;
        private ImageView profilepic;

        public Settings() {
            // Required empty public constructor
        }
    public void onAboutClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Eshwar863/SpringSecurityApp"));
        startActivity(intent);
    }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_settings, container, false);

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://finalssecurity1-v1-0.onrender.com/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            apiService = retrofit.create(ApiService.class);
            changePasswordTextView = view.findViewById(R.id.changePasswordTextView);
            LogoutButton = view.findViewById(R.id.logoutButton);
            userNametf = view.findViewById(R.id.usernameTextView);
            emailtf = view.findViewById(R.id.emailTextView);

            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", null);
            Toast.makeText(getContext(), username, Toast.LENGTH_SHORT).show();

            changePasswordTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PasswordBottomSheetDialog passwordBottomSheetDialog = new PasswordBottomSheetDialog();
                    passwordBottomSheetDialog.show(getParentFragmentManager(), "PasswordBottomSheetDialog");


                }
            });

            LogoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences preferences = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove("jwttoken");
                    editor.remove("username");
                    editor.apply();

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                }
            });

            getUser(username);
            return view;
        }

        private String getTokenFromSharedPreferences() {
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
            return sharedPreferences.getString("jwttoken", null);
        }

        public void getUser(String userName) {
            String token = getTokenFromSharedPreferences();
            Call<Users> call = apiService.getUser("Bearer " +token, userName);
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse( Call<Users> call,  Response<Users> response) {
                    if (response.isSuccessful() && response.body()!=null) {
                        Users user = response.body();
                        if (user != null) {
                           userNametf.setText(user.getUserName());
                           emailtf.setText(user.getEmail());

                        } else {
                            Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Users> call, Throwable t) {
                    Toast.makeText(getContext(), "Failed to Load UserDetails", Toast.LENGTH_SHORT).show();
                }
            });
        }
//        private void forgotpass(String password,String reenterpassword){
//            String token = getTokenFromSharedPreferences();
//            ForgotPassword forgotPassword = new ForgotPassword(password,reenterpassword);
//            Call<String> call = apiService.forgotpassword("Bearer " +token,forgotPassword);
//            call.enqueue(new Callback<String>() {
//                @Override
//                public void onResponse(Call<String> call, Response<String> response) {
//                    if (response.isSuccessful()&&response.body()!=null){
//                        if(response.code()==400 || response.code()==504){
//                            Toast.makeText(getContext(),"Failed to Update Password", Toast.LENGTH_SHORT).show();
//                        }
//                        Toast.makeText(getContext(), response.body(), Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        Toast.makeText(getContext(), "Unable to Process", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<String> call, Throwable t) {
//                    Toast.makeText(getContext(), "Request Timeout", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
    }