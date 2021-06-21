package com.example.tamagodowork.bottomNav.todoList;

public class Events {
    String EVENT, TIME, STARTDATE, ENDDATE, MONTH, YEAR, KEY;

    public Events(String EVENT, String TIME, String STARTDATE, String ENDDATE, String MONTH, String YEAR, String KEY) {
        this.EVENT = EVENT;
        this.TIME = TIME;
        this.STARTDATE = STARTDATE;
        this.ENDDATE = ENDDATE;
        this.MONTH = MONTH;
        this.YEAR = YEAR;
        this.KEY = KEY;
    }

    public String getEVENT() {
        return EVENT;
    }

    public void setEVENT(String EVENT) {
        this.EVENT = EVENT;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getSTARTDATE() {
        return STARTDATE;
    }

    public void setSTARTDATE(String STARTDATE) {
        this.STARTDATE = STARTDATE;
    }

    public String getENDDATE() { return ENDDATE; }

    public void setENDDATE(String ENDDATE) { this.ENDDATE = ENDDATE;}

    public String getMONTH() {
        return MONTH;
    }

    public void setMONTH(String MONTH) {
        this.MONTH = MONTH;
    }

    public String getYEAR() {
        return YEAR;
    }

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }

    public String getKEY() { return KEY; }
}
