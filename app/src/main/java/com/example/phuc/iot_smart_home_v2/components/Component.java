package com.example.phuc.iot_smart_home_v2.components;

public class Component {
    private String id;
    private String type;
    private int value;
    private String description;
    private int opacity;
    private String homeID;

    public int getOpacity() {
        return opacity;
    }

    public String getHomeID() {
        return homeID;
    }

    public void setHomeID(String homeID) {
        this.homeID = homeID;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public Component(String id, String type, int value, String description, int opacity, String homeID) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.description = description;
        this.opacity = opacity;
        this.homeID = homeID;
    }

    public Component() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return id + " - " + value + " - " + description + " - " + type;
    }
}
