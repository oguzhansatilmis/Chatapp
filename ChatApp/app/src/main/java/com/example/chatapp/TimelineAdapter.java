package com.example.chatapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder>
{
    Context context;
    List<TimelineModel> list;
    Activity activity;
    String userName;



    public TimelineAdapter(Context context, List<TimelineModel> list, Activity activity, String userName)
    {
        this.context = context;
        this.list = list;
        this.activity = activity;
        this.userName = userName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.gonderi,parent,false);
        //view = LayoutInflater.from(context).inflate(R.layout.gonderi,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineAdapter.ViewHolder holder, int position)
    {

        holder.textView.setText(list.get(position).getText());
        holder.gonderiTarih.setText(list.get(position).getTarih());
        holder.gonderiKullaniciId.setText(list.get(position).getFrom());
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
       TextView textView;
       TextView gonderiTarih;
       TextView gonderiKullaniciId;


        public ViewHolder(@NonNull View itemView)
        {

            super(itemView);
            textView = itemView.findViewById(R.id.gonderxmlText);
            gonderiTarih = itemView.findViewById(R.id.gonderiTarih);
            gonderiKullaniciId = itemView.findViewById(R.id.gonderiKullaniciId);

        }
    }
}
