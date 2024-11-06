package com.example.springsecurity.Model;

public class ForgotPassword {
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReenterpassword() {
        return reenterpassword;
    }

    public void setReenterpassword(String reenterpassword) {
        this.reenterpassword = reenterpassword;
    }

    public ForgotPassword(String password, String reenterpassword) {
        this.password = password;
        this.reenterpassword = reenterpassword;
    }

    private String password;
    private String reenterpassword;

}
