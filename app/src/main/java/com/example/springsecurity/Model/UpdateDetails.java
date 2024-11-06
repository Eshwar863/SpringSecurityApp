package com.example.springsecurity.Model;

public class UpdateDetails {

    private String userName;
    private String email;
    private String DOB;
    private String gender;

    public UpdateDetails(String userName, String email, String DOB, String gender) {
        this.userName = userName;
        this.email = email;

        this.DOB = DOB;
        this.gender = gender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
