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

public class ForumYorumAdapter extends RecyclerView.Adapter<ForumYorumAdapter.ViewHolder>
{
    Context context;
    List<MesajModel> list;
    Activity activity;
    String userName;


    public ForumYorumAdapter(Context context, List<MesajModel> list, Activity activity, String userName)
    {
        this.context = context;
        this.list = list;
        this.activity = activity;
        this.userName = userName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.yorum, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
       holder.forumYorum.setText(list.get(position).getText());
        holder.forumYorumTarih.setText(list.get(position).getTarih());
        holder.forumYorumYapanKisi.setText(list.get(position).getFrom());


    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView forumYorum;
        TextView forumYorumTarih;
        TextView forumYorumYapanKisi;




        public ViewHolder(@NonNull View itemView)
        {

            super(itemView);
            forumYorum = itemView.findViewById(R.id.forumYorum);
            forumYorumTarih = itemView.findViewById(R.id.forumYorumTarih);
            forumYorumYapanKisi = itemView.findViewById(R.id.forumYorumYapanKisi);


        }
    }
}
