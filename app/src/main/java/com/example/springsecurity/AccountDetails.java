package com.example.springsecurity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.springsecurity.API.ApiService;
import com.example.springsecurity.Model.Users;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountDetails extends AppCompatActivity {
    private Button edituserdetails;
    private ApiService apiService;
    private EditText nameEditText, emailEditText, dobEditText;
    private Spinner genderSpinner;
    private ImageView profilepic;
    private Integer userid;
    private static final int PICK_IMAGE_REQUEST = 1; // Constant for image picker
    private String base64ImageString; // To store the base64 string of the image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_details);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://finalssecurity1-v1-0.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        apiService = retrofit.create(ApiService.class);

        edituserdetails = findViewById(R.id.updateButton);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        dobEditText = findViewById(R.id.dobEditText);
        genderSpinner = findViewById(R.id.genderSpinner);
        profilepic = findViewById(R.id.profileImageView);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        String userName = getUserNameFromPreferences();
        getUser(userName);

        // Set onClickListener for profile image to select image
        profilepic.setOnClickListener(v -> openImagePicker());

        // Edit or Save user details
        edituserdetails.setOnClickListener(v -> {
            if (nameEditText.isEnabled()) {
                // Save the updated user details
                String updatedName = nameEditText.getText().toString();
                String updatedEmail = emailEditText.getText().toString();
                String updatedDob = dobEditText.getText().toString();
                String updatedGender = genderSpinner.getSelectedItem().toString();

                updateUserDetails(updatedName, updatedEmail, updatedDob, updatedGender);
                profilepic.setEnabled(false);
                nameEditText.setEnabled(false);
                emailEditText.setEnabled(false);
                dobEditText.setEnabled(false);
                genderSpinner.setEnabled(false);
                edituserdetails.setText("Edit");
            } else {
                profilepic.setEnabled(true);
                nameEditText.setEnabled(true);
                emailEditText.setEnabled(true);
                dobEditText.setEnabled(true);
                genderSpinner.setEnabled(true);
                edituserdetails.setText("Save");
            }
        });
    }

    private String getTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        return sharedPreferences.getString("jwttoken", null);
    }

    private String getUserNameFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", null);
    }

    public void getUser(String userName) {
        String token = getTokenFromSharedPreferences();
        if (token == null || userName == null) {
            Toast.makeText(AccountDetails.this, "No token or username available", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<Users> call = apiService.getUser("Bearer " + token, userName);
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Users user = response.body();
                    if (user != null) {
                        userid = user.getId();
                        nameEditText.setText(user.getUserName());
                        emailEditText.setText(user.getEmail());
                        dobEditText.setText(user.getDOB());
                        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) genderSpinner.getAdapter();
                        int spinnerPosition = adapter.getPosition(user.getGender());
                        genderSpinner.setSelection(spinnerPosition);

                        // Decode and set the image if available
                        if (user.getProfilepic() != null) {
                            decodeAndSetImage(user.getProfilepic());
                        }
                    } else {
                        Toast.makeText(AccountDetails.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AccountDetails.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Toast.makeText(AccountDetails.this, "Failed to Load UserDetails", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserDetails(String name, String email, String dob, String gender) {
        String token = getTokenFromSharedPreferences();
        String userName = getUserNameFromPreferences();

        // Create a User object with the updated details and base64 image string
        Users updatedUser = new Users(userid, name, email, base64ImageString, dob, gender);

        // Check if the image is available; if not, set it as null (or handle it as you prefer)
        if (base64ImageString == null || base64ImageString.isEmpty()) {
            updatedUser.setProfilepic(null); // Assuming you have a setter method in the Users class
        } else {
            updatedUser.setProfilepic(base64ImageString);
        }

        // Make the API call to update user details
        Call<Users> call = apiService.update("Bearer " + token, updatedUser);
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(AccountDetails.this, "User details updated successfully", Toast.LENGTH_SHORT).show();
                    // You can refresh the user data or navigate to another activity if needed
                } else {
                    Toast.makeText(AccountDetails.this, "Failed to update details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Toast.makeText(AccountDetails.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Open image picker
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle image result and convert to Base64
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                // Convert the image to Base64 string
                base64ImageString = convertImageToBase64(imageUri);
                // Set the image to the ImageView
                profilepic.setImageURI(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(AccountDetails.this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Convert image URI to Base64 string
    private String convertImageToBase64(Uri imageUri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(imageUri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // Decode Base64 image and set it to the ImageView
    private void decodeAndSetImage(String base64ImageString) {
        try {
            byte[] decodedString = Base64.decode(base64ImageString, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profilepic.setImageBitmap(decodedBitmap);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to decode image", Toast.LENGTH_SHORT).show();
        }
    }
}
