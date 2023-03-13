package com.example.assignment;

public class User {
    String Title,start,end,Desc;
    public User(){

    }

    public User(String title, String start, String end, String desc) {
        this.Title = title;
        this.start = start;
        this.end = end;
        this.Desc = desc;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }
}
