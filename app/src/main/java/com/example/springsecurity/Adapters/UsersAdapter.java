package com.example.springsecurity.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.springsecurity.Model.Users;
import com.example.springsecurity.R;

import java.util.ArrayList;

public class UsersAdapter extends ArrayAdapter<Users> {
    public UsersAdapter(Context context, ArrayList<Users> usersArrayList) {
        super(context, 0,usersArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Users user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_item, parent, false);
        }

        TextView userId = convertView.findViewById(R.id.userId);
        TextView userName = convertView.findViewById(R.id.userName);

        userId.setText("User ID: " + user.getId());
        userName.setText("Username: " + user.getUserName());

        return convertView;
    }
}
