package com.example.chatapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ViewHolder>
{
    Context context;
    List<MesajModel> list;
    Activity activity;
    String userName;


    public ForumAdapter(Context context, List<MesajModel> list, Activity activity, String userName)
    {
        this.context = context;
        this.list = list;
        this.activity = activity;
        this.userName = userName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.forumkonu, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
       holder.textView.setText(list.get(position).getText());
       holder.forumtarih.setText(list.get(position).getTarih().toString());
       holder.konuyuAcan.setText(list.get(position).getFrom());

       holder.textView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(activity,ForumYorumYap.class);
               intent.putExtra("KonuBaslik",list.get(position).getText());
           //    intent.putExtra("kullaniciID",list.get(position).getFrom());
               activity.startActivity(intent);


           }
       });

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
         TextView forumtarih;
        TextView konuyuAcan;



        public ViewHolder(@NonNull View itemView)
        {

            super(itemView);
            textView = itemView.findViewById(R.id.konubaslik);
            forumtarih = itemView.findViewById(R.id.forumtarih);
            konuyuAcan = itemView.findViewById(R.id.konuyuAcan);

        }
    }
}
