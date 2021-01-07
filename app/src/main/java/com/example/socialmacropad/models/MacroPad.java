package com.example.socialmacropad.models;

public class MacroPad {


    private String creatorUser;
    private String creatorID;

    private String name;
    private String description;
    private String color;

    private String padId;

    private Action action1;
    private Action action2;
    private Action action3;
    private Action action4;
    private Action action5;
    private Action action6;

//    ESTO ES CORRECTO! FIREBASE LO NECESITA
    public MacroPad(){ }


    // Cuando se use un adaptador se utiliza este
    public MacroPad(String creatorUser, String creatorID, String padId, String name, String description, String color, Action action1, Action action2, Action action3, Action action4, Action action5, Action action6) {
        this.creatorUser = creatorUser;
        this.creatorID = creatorID;
        this.padId = padId;
        this.name = name;
        this.color = color;
        this.description = description;
        this.action1 = action1;
        this.action2 = action2;
        this.action3 = action3;
        this.action4 = action4;
        this.action5 = action5;
        this.action6 = action6;
    }

    public String getPadId() {
        return padId;
    }

    public void setPadId(String padId) {
        this.padId = padId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatorUser() {
        return creatorUser;
    }

    public void setCreatorUser(String creatorUser) {
        this.creatorUser = creatorUser;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public Action getAction1() {
        return action1;
    }

    public void setAction1(Action action1) {
        this.action1 = action1;
    }

    public Action getAction2() {
        return action2;
    }

    public void setAction2(Action action2) {
        this.action2 = action2;
    }

    public Action getAction3() {
        return action3;
    }

    public void setAction3(Action action3) {
        this.action3 = action3;
    }

    public Action getAction4() {
        return action4;
    }

    public void setAction4(Action action4) {
        this.action4 = action4;
    }

    public Action getAction5() {
        return action5;
    }

    public void setAction5(Action action5) {
        this.action5 = action5;
    }

    public Action getAction6() {
        return action6;
    }

    public void setAction6(Action action6) {
        this.action6 = action6;
    }
}
