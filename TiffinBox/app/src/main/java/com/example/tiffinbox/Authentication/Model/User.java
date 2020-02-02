package com.example.tiffinbox.Authentication.Model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String name;
    public String mobile;
    public String email;
    public String password;
    public String address;
    public String userType;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String mobile, String email, String password, String address, String userType) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.address = address;
        this.userType = userType;
    }



}
