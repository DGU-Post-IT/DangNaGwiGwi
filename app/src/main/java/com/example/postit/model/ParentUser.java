package com.example.postit.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class ParentUser {
    String name;
    String email;
    ArrayList<String> follower;
    ArrayList<String> requested;
    String phone;
    Date birthdate;
    int sex;
    String fcm;

    public String getFcm() {
        return fcm;
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
    }

    public ParentUser() {
        this.sex =0;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ParentUser(String name, String email, Date birthdate, int sex, String phone) {
        this.name = name;
        this.email = email;
        this.birthdate = birthdate;
        this.sex = sex;
        this.follower = new ArrayList<>();
        this.requested = new ArrayList<>();
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getFollower() {
        return follower;
    }

    public void setFollower(ArrayList<String> follower) {
        this.follower = follower;
    }

    public ArrayList<String> getRequested() {
        return requested;
    }

    public void setRequested(ArrayList<String> requested) {
        this.requested = requested;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
