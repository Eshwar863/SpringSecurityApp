package com.example.springsecurity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.springsecurity.API.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class userdetails extends AppCompatActivity {
    Gson gson;
    Retrofit retrofit;
    private ApiService apiService;
    private ProgressBar progressbarsplashscreen;
    TextView userIdTextView,userNameTextView,emailTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_userdetails);

        gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://springsecurity-latest-om3m.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        apiService = retrofit.create(ApiService.class);

        int userId = getIntent().getIntExtra("userId", -1);
        String userName = getIntent().getStringExtra("userName");
        String email = getIntent().getStringExtra("email");

         userIdTextView = findViewById(R.id.userid);
         userNameTextView = findViewById(R.id.username);
        emailTextView = findViewById(R.id.email);
        ImageView view = findViewById(R.id.userimg);
        view.setImageResource(R.drawable.userlogin);
        userIdTextView.setText("userid :"+String.valueOf(userId));
        userNameTextView.setText(String.valueOf(userName));
        emailTextView.setText(String.valueOf(email));
    }
}