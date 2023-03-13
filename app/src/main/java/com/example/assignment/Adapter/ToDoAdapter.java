package com.example.assignment.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.AssignmentActivity;
import com.example.assignment.MainActivity;
import com.example.assignment.Model.ToDoModel;
import com.example.assignment.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Nullable;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {
    private List<ToDoModel> todoList;
    private AssignmentActivity activity;
    private FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    String uid;

    public ToDoAdapter(AssignmentActivity assignmentActivity, List<ToDoModel> todoList){
        this.todoList = todoList;
        activity = assignmentActivity;
    }
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.each_task,parent,false);
        firestore=FirebaseFirestore.getInstance();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
             ToDoModel toDoModel= todoList.get(position);
             holder.mCheckbox.setText(toDoModel.getTask());
             holder.mDueDateTv.setText("Due on "+toDoModel.getDue());
             holder.mCheckbox.setChecked(toBoolean(toDoModel.getStatus()));
             holder.mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                     uid= mAuth.getCurrentUser().getUid();
                     if(b){
                          firestore.collection("task").document(uid).collection("Task Data").document(toDoModel.TaskId).update("status",1);

                     }else{
                         firestore.collection("task").document(uid).collection("Task Data").document(toDoModel.TaskId).update("status",0);
                     }
                 }
             });
    }

    private boolean toBoolean(int status){
        return  status != 0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public  class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView mDueDateTv;
        CheckBox mCheckbox;
        public MyViewHolder(@Nullable View itemview){
            super(itemview);
            mDueDateTv = itemview.findViewById(R.id.due_date_tv);
            mCheckbox=itemview.findViewById(R.id.mcheckbox);
            mAuth = FirebaseAuth.getInstance();

        }
    }

}
