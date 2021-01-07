package com.example.socialmacropad.models;

public class Activity {

    private String name;
    private String input;
    private String colour;

    public Activity(){ }

    public Activity(String name, String input, String colour){
        this.name = name;
        this.input = input;
        this.colour = colour;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String description) {
        this.input = input;
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

}
