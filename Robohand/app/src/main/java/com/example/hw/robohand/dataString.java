package com.example.hw.robohand;

public class dataString {
    private String[] data;
    public dataString() {
        data = new String[12];
        data[0] = "0";
        data[1] = "1";
        data[2] = "2";
        data[3] = "3";
        data[4] = "4";
        data[5] = "5";
        data[6] = "6";
        data[7] = "7";
        data[8] = "8";
        data[9] = "9";
        data[10] = "a";
        data[11] = "b";
    }
    public String getData(int idx) {
        return data[idx];
    }
    public String getData(int idx, String id) {
        if(idx > 3) return "-1";
        if(id == "SWIPE UP") return data[idx];
        else if(id == "SWIPE DOWN") return data[idx + 3];
        else if(id == "SWIPE RIGHT") return data[idx + 6];
        else if(id == "SWIPE LEFT") return data[idx + 9];
        return "-1";
    }
}
