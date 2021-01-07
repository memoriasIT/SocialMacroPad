package com.example.socialmacropad.models;

import java.util.ArrayList;
import java.util.List;

public class GroupOfActivities {

    private String name;
    private String description;
    private String colour;
    private List<Action> actions;

    public GroupOfActivities(){ }

    public GroupOfActivities(String name, String description, String colour){
        this.name = name;
        this.description = description;
        this.colour = colour;//ej: #FF0000
        actions = new ArrayList<Action>();
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

    public List<Action> getActivities(){ return actions; }

    public void addAction(Action action){
        actions.add(action);
    }

    public void deleteAction(Activity activity){
        actions.remove(activity);
    }

    public void deleteGroup(){
        actions.clear();
    }

}
