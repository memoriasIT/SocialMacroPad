package com.example.socialmacropad.models;

import java.util.ArrayList;
import java.util.List;

public class GroupOfActivities {

    private String name;
    private String description;
    private String colour;
    private List<Activity> activities;

    public GroupOfActivities(){ }

    public GroupOfActivities(String name, String description, String colour){
        this.name = name;
        this.description = description;
        this.colour = colour;//ej: #FF0000
        activities = new ArrayList<Activity>();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Activity> getActivities(){ return activities; }

    public void addActivity(Activity activity){
        activities.add(activity);
    }

    public void deleteActivity(Activity activity){
        activities.remove(activity);
    }

    public void deleteGroup(){
        activities.clear();
    }

}
