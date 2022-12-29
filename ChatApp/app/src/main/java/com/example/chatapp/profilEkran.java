package com.example.chatapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class profilEkran extends AppCompatActivity {

    Button buttoon,buttoon2;
    TextView texttext;
    RelativeLayout relativ;
    int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_ekran);

        buttoon = findViewById(R.id.buttoon);
        buttoon2 = findViewById(R.id.buttoon2);
        texttext = findViewById(R.id.texttext);
        relativ = findViewById(R.id.relativ);
        relativ.setVisibility(View.INVISIBLE);

        buttoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttoon.setVisibility(View.INVISIBLE);
                texttext.setText("kabul edildi");
                relativ.setVisibility(View.VISIBLE);

            }
        });

    }

}