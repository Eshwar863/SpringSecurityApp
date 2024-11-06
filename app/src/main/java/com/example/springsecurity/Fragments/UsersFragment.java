package com.example.springsecurity.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.springsecurity.API.ApiService;
import com.example.springsecurity.Adapters.UsersAdapter;
import com.example.springsecurity.Model.Users;
import com.example.springsecurity.R;
import com.example.springsecurity.userdetails;
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
    private UsersAdapter adapter;
    private Call<List<Users>> usersCall;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        progressBar = view.findViewById(R.id.progressbarusersfragment);
        listView = view.findViewById(R.id.listView);
        userList = new ArrayList<>();

       adapter = new UsersAdapter(requireContext(), userList);
        listView.setAdapter(adapter);

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://finalssecurity1-v1-0.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        apiService = retrofit.create(ApiService.class);


        fetchUsers();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Users selectedUser = adapter.getItem(position);


                Intent intent = new Intent(getActivity(), userdetails.class);


                intent.putExtra("userId", selectedUser.getId());
                intent.putExtra("userName", selectedUser.getUserName());
                intent.putExtra("email", selectedUser.getEmail());

                startActivity(intent);
                Toast.makeText(getContext(), selectedUser.getEmail(), Toast.LENGTH_SHORT).show();

            }
        });




        return view;
    }

    private String getTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        return sharedPreferences.getString("jwttoken", null);
    }

    private void fetchUsers() {
        String token = getTokenFromSharedPreferences();
        progressBar.setVisibility(View.VISIBLE);
        usersCall = apiService.AllUsers("Bearer " + token);

        usersCall.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                if (isAdded()) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(getContext(), "Users loaded successfully!", Toast.LENGTH_SHORT).show();

                        userList.clear();
                        userList.addAll(response.body());
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w("UsersFragment", "Response not successful: " + response.message());
                        Toast.makeText(getContext(), "Failed to load users", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                if (isAdded()) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("UsersFragment", "Request failed", t);
                    Toast.makeText(getContext(), "Request timed out", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (usersCall != null) {
            usersCall.cancel();
        }
    }
}
