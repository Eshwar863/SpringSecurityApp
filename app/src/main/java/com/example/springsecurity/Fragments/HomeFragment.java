package com.example.springsecurity.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.springsecurity.AccountDetails;
import com.example.springsecurity.R;
import com.example.springsecurity.userdetails;

import java.util.Calendar;

public class HomeFragment extends Fragment {
    private TextView greetingTextView, jwttoken;
    ImageView accdetails;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        greetingTextView = view.findViewById(R.id.textView3);
        jwttoken = view.findViewById(R.id.displaytoken);
        accdetails = view.findViewById(R.id.accountButton);
        SharedPreferences preferences = requireActivity().getSharedPreferences("Login", MODE_PRIVATE);
        String token = preferences.getString("jwttoken", "jwttoken");
        String username = preferences.getString("username","username");
        setGreetingMessage(username);
        jwttoken.setText(token);
        accdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountDetails.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void setGreetingMessage(String username) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        String greeting;
        if (hour >= 5 && hour < 12) {
            greeting = "Good Morning";
        } else if (hour >= 12 && hour < 17) {
            greeting = "Good Afternoon";
        } else if (hour >= 17 && hour < 21) {
            greeting = "Good Evening";
        } else {
            greeting = "Good Night";
        }


        greetingTextView.setText(greeting);
    }
}
