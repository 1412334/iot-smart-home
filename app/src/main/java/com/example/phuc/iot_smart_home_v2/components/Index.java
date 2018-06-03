package com.example.phuc.iot_smart_home_v2.components;

public class Index {
    private String date;
    private int value;

    public Index(String date, int value) {
        this.date = date;
        this.value = value;
    }

    public Index() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
