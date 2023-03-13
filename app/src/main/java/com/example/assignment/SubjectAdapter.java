package com.example.assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {
    Context context;
    ArrayList<User> userArrayList;
    public SubjectAdapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }
    @NonNull
    @Override
    public SubjectAdapter.SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new SubjectViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull SubjectAdapter.SubjectViewHolder holder,
                                 int position) {
        User user = userArrayList.get(position);
        holder.Title.setText(user.Title);
        holder.start.setText(user.start);
        holder.end.setText(user.end);
        holder.Desc.setText(user.Desc);

    }
    @Override
    public int getItemCount() {
        return userArrayList.size();
    }
    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView Title,start,end,Desc;
        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.subject_name);
            start = itemView.findViewById(R.id.start_time);
            end = itemView.findViewById(R.id.end_time);
            Desc = itemView.findViewById(R.id.description);
        }
    }
}

