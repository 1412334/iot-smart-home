package com.example.phuc.iot_smart_home_v2.components;

import java.io.Serializable;

public class User implements Serializable{
    private String userName;
    private String password;
    private String name;
    private String homeID;

    public User() {

    }

    public User(User b) {
        this.userName = b.getUserName();
        this.password = b.getPassword();
        this.name = b.getName();
        this.homeID = b.getHomeID();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getHomeID() {
        return homeID;
    }

    public void setHomeID(String homeID) {
        this.homeID = homeID;
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User(String userName, String password, String name, String homeID) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.homeID = homeID;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return userName + " - " + password;
    }
}
