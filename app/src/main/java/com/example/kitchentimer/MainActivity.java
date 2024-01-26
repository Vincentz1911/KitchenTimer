package com.example.kitchentimer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button) findViewById(R.id.btn_new)).setOnClickListener(v -> {
            KTimer kTimer = new KTimer(View.inflate(this, R.layout.timer_layout, null));
            ((LinearLayout) findViewById(R.id.ll_timers)).addView(kTimer.view);
        });
    }
}