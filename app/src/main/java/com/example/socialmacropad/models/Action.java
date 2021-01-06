package com.example.socialmacropad.models;

public class Action {
    private String Action;
    private String ActionName;
    private String color;

    // ESTO ESTÄ BIEN ES PARA FIREBASE
    public Action(){}

    public Action(String Action, String ActionName, String color){
        super();
        this.Action = Action;
        this.ActionName = ActionName;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }

    public String getActionName() {
        return ActionName;
    }

    public void setActionName(String actionName) {
        ActionName = actionName;
    }
}
