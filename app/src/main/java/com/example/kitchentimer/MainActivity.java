package com.example.kitchentimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

//https://www.themealdb.com/api.php
//https://www.zedge.net/ringtone/d23692ee-90af-4a9d-a7e3-d8a716a547e3
//https://square.github.io/picasso/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button) findViewById(R.id.btn_meal)).setOnClickListener(v -> {
            startActivity(new Intent(this, MealActivity.class));
        });

        ((FloatingActionButton) findViewById(R.id.btn_new)).setOnClickListener(v -> {
            KTimer kTimer = new KTimer(View.inflate(this, R.layout.timer_layout, null));
            ((LinearLayout) findViewById(R.id.ll_timers)).addView(kTimer.view);
        });
    }
}