package com.example.basicreminnder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class hAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    ArrayList<Timee>timees;

    public hAdapter(Context c,ArrayList<Timee> t){
        context=c;
        timees=t;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_view,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.task.setText(timees.get(i).getTask());
        myViewHolder.time.setText(timees.get(i).getChour()+":"+timees.get(i).getCtime());

    }

    @Override
    public int getItemCount() {
        return timees.size();
    }
}
class MyViewHolder extends RecyclerView.ViewHolder{
    TextView task,time;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        task=(TextView)itemView.findViewById(R.id.taskD);
        time=(TextView)itemView.findViewById(R.id.timeD);
    }
}
