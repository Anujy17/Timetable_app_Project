package com.example.assignment.Model;

import com.example.assignment.TaskActivity;
import com.google.firebase.firestore.Exclude;

import javax.annotation.Nullable;

public class TaskId {
    @Exclude
    public String TaskId;
    public <T extends TaskId> T withId(@Nullable final String id){
        this.TaskId=id;
        return (T) this;
    }
}
