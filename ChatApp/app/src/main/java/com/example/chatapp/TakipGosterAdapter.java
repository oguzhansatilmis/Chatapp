package com.example.chatapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TakipGosterAdapter extends RecyclerView.Adapter<TakipGosterAdapter.ViewHolder> {
    Context context;
    List<String> takipListeleri;
    Activity activity;

    public TakipGosterAdapter(Context context, List<String> takipListeleri,Activity activity)
    {
        this.context = context;
        this.takipListeleri = takipListeleri;
        this.activity = activity;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.kullanici_goster,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.user_text_name.setText(takipListeleri.get(position).toString());


    }

    @Override
    public int getItemCount() {
        return takipListeleri.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder
    {
        TextView user_text_name;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            user_text_name = itemView.findViewById(R.id.user_text_name);

        }
    }

}