package com.example.lousticsschool;

import android.app.Application;

import com.example.lousticsschool.db.User;

public class Globals extends Application {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
