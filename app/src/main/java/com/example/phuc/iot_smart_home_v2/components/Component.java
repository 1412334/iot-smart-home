package com.example.phuc.iot_smart_home_v2.components;

public class Component {
    private String id;
    private String icon;
    private int value;
    private String description;
    private int opacity;

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public Component(String id, String icon, int value, String description, int opacity) {
        this.id = id;
        this.icon = icon;
        this.value = value;
        this.description = description;
        this.opacity = opacity;

    }

    public Component() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
        return id + " - " + value + " - " + description + " - " + icon;
    }
}
