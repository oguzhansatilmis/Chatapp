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

public class MesajAdapter extends RecyclerView.Adapter<MesajAdapter.ViewHolder> {
    Context context;
    List<MesajModel> list;
    Activity activity;
    String userName;


    boolean state ;  // yana yatmalar
    int viewSend =1,view_receied = 2;  // mesaj alan - gonderen

    public MesajAdapter(Context context, List<MesajModel> list, Activity activity, String userName) {
        this.context = context;
        this.list = list;
        this.activity = activity;
        this.userName = userName;
        this.state = false;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ; //= LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false); // ?????
        // layoout tasarımı bu bölümde - user adapterdan tasarladıgımız layout buraya cagırdık.
        if(viewType == viewSend){

             view = LayoutInflater.from(context).inflate(R.layout.send, parent, false);
            return new ViewHolder(view);
        }
        else{
            view = LayoutInflater.from(context).inflate(R.layout.gonderen, parent, false);
            return new ViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textView.setText(list.get(position).getText().toString()); // mesaj içerigi doner
        holder.saat.setText(list.get(position).getTarih());
    }

    @Override
    public int getItemCount() {
        return list.size();  // listenin size
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        TextView saat;


        public ViewHolder(@NonNull View itemView) {


            super(itemView);
            if(state == true){
                textView = itemView.findViewById(R.id.sendTextView); //
                saat = itemView.findViewById(R.id.tarih);
            }
            else {
                textView = itemView.findViewById(R.id.gonderenTextView); //
                saat = itemView.findViewById(R.id.tarih);
            }


        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if(list.get(position).getFrom().equals(userName)){
                state = true;
                return viewSend;
        }
        else
        {
            state = false;
            return  view_receied;
        }
    }
}
