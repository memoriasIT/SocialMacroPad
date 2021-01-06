package com.example.socialmacropad.models;

public class Action {
    private String action;
    private String actionname;
    private String color;

    // ESTO ESTÄ BIEN ES PARA FIREBASE
    public Action(){}

    public Action(String Action, String actionname, String color){
        super();
        this.action = Action;
        this.actionname = actionname;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionname() {
        return actionname;
    }

    public void setActionname(String actionname) {
        this.actionname = actionname;
    }
}
