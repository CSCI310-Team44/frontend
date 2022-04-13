package com.example.frontend;

public class FakeSingleton {

    static String userid;
    public static void setUserid(String userid) {
        FakeSingleton.userid = userid;
    }

    public static String getUserid() {
        return userid;
    }
}
