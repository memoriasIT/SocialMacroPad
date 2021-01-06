package com.example.socialmacropad.models;

public class Action {
    private String Action;
    private String ActionName;

    // ESTO ESTÄ BIEN ES PARA FIREBASE
    public Action(){}

    public Action(String Action, String ActionName){
        super();
        this.Action = Action;
        this.ActionName = ActionName;
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
