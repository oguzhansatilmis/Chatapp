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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context context;
    List<String> kullaniciIDListe,kullaniciAdiListe;
    Activity activity;
    String kullaniciName;
    public UserAdapter(Context context, List<String> kullaniciAdiListe,List<String> kullaniciIDListe, Activity activity, String kullaniciName)
    {
        this.context = context;
        this.kullaniciIDListe = kullaniciIDListe;
        this.kullaniciAdiListe = kullaniciAdiListe;
        this.activity = activity;
        this.kullaniciName = kullaniciName;
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
        holder.user_text_name.setText(kullaniciAdiListe.get(position).toString());
        holder.user_text_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,profilGoster.class);
                intent.putExtra("digerKullaniciID",kullaniciIDListe.get(position));
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return kullaniciAdiListe.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder
    {
        TextView user_text_name;
        RelativeLayout kullaniciGoster;
        public ViewHolder(@NonNull View itemView)
        {

            super(itemView);
            user_text_name = itemView.findViewById(R.id.user_text_name);
            kullaniciGoster = itemView.findViewById(R.id.kullaniciGoster);

        }
    }

}
