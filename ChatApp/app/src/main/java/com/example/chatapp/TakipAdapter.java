package com.example.chatapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TakipAdapter extends RecyclerView.Adapter<TakipAdapter.ViewHolder> {
    Context context;
    List<String> list,takipListe;
    Activity activity;
    String kullaniciName;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference,reference2,reference3;
    public TakipAdapter(Context context, List<String> list, Activity activity, String kullaniciName)
    {
        this.context = context;
        this.list = list;
        this.activity = activity;
        this.kullaniciName = kullaniciName;
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        reference2 = firebaseDatabase.getReference();
        reference3 = firebaseDatabase.getReference();
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.takip,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
       holder.textView.setText(list.get(position) + " seni takip etmek istiyor");
       holder.takipOnayla.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View v)
           {
               String key  = reference.child("Takip").child("takipListesi").child("takipci").push().getKey();
               reference.child("Takip").child("takipListesi").child(kullaniciName).child("takipci").child(key).setValue(list.get(position)).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task)
                   {

                       reference.child("Takip").child("takipListesi").child(list.get(position)).child("takipEdilen").child(key).setValue(kullaniciName).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context,"Takip isteği kabul edildi.",Toast.LENGTH_SHORT).show();
                            }
                        });

                   }
               });

           }
       });

       reference2.child("Takip").child("takipListesi").child(kullaniciName).child("takipci").addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               takipListe = new ArrayList<>();
               String takipciListe = snapshot.getValue().toString();
              // takipListe.add(takipciListe);
               if(list.get(position).equals(takipciListe))
               {
                   holder.textView.setText(list.get(position) +" takip isteği kabul edildi");
                   holder.takipOnayla.setVisibility(View.INVISIBLE);
               }


           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot snapshot) {

           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        Button takipOnayla;

        public ViewHolder(@NonNull View itemView)
        {

            super(itemView);
            textView = itemView.findViewById(R.id.takipIstek);
            takipOnayla = itemView.findViewById(R.id.takipOnayla);


        }
    }

}
