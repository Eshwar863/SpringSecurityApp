package com.example.springsecurity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class userdetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_userdetails);

        int userId = getIntent().getIntExtra("userId", -1);
        String userName = getIntent().getStringExtra("userName");

        TextView userIdTextView = findViewById(R.id.userid);
        TextView userNameTextView = findViewById(R.id.username);
        ImageView view = findViewById(R.id.userimg);
        view.setImageResource(R.drawable.userlogin);
        userIdTextView.setText("userid :"+String.valueOf(userId));
        userNameTextView.setText(String.valueOf(userName));
    }
}