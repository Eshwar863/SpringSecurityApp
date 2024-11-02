package com.example.springsecurity.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.springsecurity.API.ApiService;
import com.example.springsecurity.Adapters.UsersAdapter;
import com.example.springsecurity.Model.Users;
import com.example.springsecurity.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersFragment extends Fragment {
    private ApiService apiService;
    private ListView listView;
    private ArrayList<Users> userList;
    private ProgressBar progressBar;

    public UsersFragment() {
        // Required empty public constructor
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        progressBar = view.findViewById(R.id.progressbarusersfragment);
        listView = view.findViewById(R.id.listView);
        userList = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://springsecurity-tbu2.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        apiService = retrofit.create(ApiService.class);
        progressBar.setVisibility(View.VISIBLE);
        users();
        return view;
    }
    private String getTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        //Toast.makeText(getContext(), sharedPreferences.getString("jwttoken", null), Toast.LENGTH_SHORT).show();
        return sharedPreferences.getString("jwttoken", null);
    }
    private void users() {
        String token = getTokenFromSharedPreferences();
        Call<List<Users>> call = apiService.AllUsers("Bearer " + token);
        call.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    progressBar.setVisibility(View.GONE);
                    Log.d("UsersFragment", response.body().toString());
                    userList.clear();
                    userList.addAll(response.body());
                    UsersAdapter adapter = new UsersAdapter(getContext(), userList);
                    listView.setAdapter(adapter);
                } else {
                    Log.w("UsersFragment", "Response not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("UsersFragment", "Request timed out", t);
                Toast.makeText(getContext(), "Request timed out", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
