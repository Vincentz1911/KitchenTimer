package com.example.kitchentimer;

import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;

import androidx.core.content.ContextCompat;

import java.time.Duration;
import java.time.LocalDateTime;

public class KTimer extends Thread {

    //TODO Save state (remember the time for alarms)
    public View view;
    private String name;
    private int secondsLeft;
    private int alarm;
    private LocalDateTime alarmTime;


    private MediaPlayer mp;
    private boolean isRunning, isFinished, isDeleted;
    private NumberPicker npSec, npMin, npHour;
    private Spinner spAlarm;
    private ImageButton btnStart, btnDelete;

    public KTimer(View view) {
        this.view = view;
        mp = new MediaPlayer();
        name = "Timer";
        init();
        start();

        btnStart.setOnClickListener(x -> {
            if (!isRunning) {
                btnStart.setImageResource(R.drawable.baseline_pause_24);
                secondsLeft = getSecondsLeft();
                isRunning = true;
            } else {
                btnStart.setImageResource(R.drawable.baseline_play_arrow_24);
                isRunning = false;
                view.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.border));
                if (isFinished) isFinished = false;
            }
        });

        btnDelete.setOnClickListener(v1 -> {
            isDeleted = true;
            ((ViewGroup) view.getParent()).removeView(view);
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                view.getContext(),
                R.array.alarms,
                android.R.layout.simple_spinner_item
        );
        spAlarm.setAdapter(adapter);
        spAlarm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                alarm = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    void playAlarm() {
        int sound;
        view.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.finished_border));

        switch (alarm) {
            case 0:
                sound = R.raw.breeze;
                break;
            case 1:
                sound = R.raw.ripple;
                break;
            case 2:
                sound = R.raw.tiktok;
                break;
            case 3:
                sound = R.raw.time;
                break;
            default:
                sound = R.raw.ripple;
                break;
        }
        mp.create(view.getContext(), sound).start();
    }

    private void init() {
        npSec = view.findViewById(R.id.np_seconds);
        npMin = view.findViewById(R.id.np_minutes);
        npHour = view.findViewById(R.id.np_hours);
        btnStart = view.findViewById(R.id.btn_start);
        btnDelete = view.findViewById(R.id.btn_delete);
        spAlarm = view.findViewById(R.id.sp_alarm);

        npSec.setMinValue(0);
        npSec.setMaxValue(59);
        npMin.setMinValue(0);
        npMin.setMaxValue(59);
        npHour.setMinValue(0);
        npHour.setMaxValue(24);
    }

    private void setTimer() {
        secondsLeft = (int) Duration.between(LocalDateTime.now(), alarmTime).toMillis() / 1000;

        npHour.setValue(secondsLeft / 3600);
        npMin.setValue((secondsLeft % 3600) / 60);
        npSec.setValue(secondsLeft % 60);
    }

    public int getSecondsLeft() {
        int i = npHour.getValue() * 3600 + npMin.getValue() * 60 + npSec.getValue();
        alarmTime = LocalDateTime.now().plusSeconds(i);
        return npHour.getValue() * 3600 + npMin.getValue() * 60 + npSec.getValue();
    }

    public void run() {
        while (!isDeleted) {
            if (isRunning) {
                //TODO Calculate time it ends in realtime instead of sleep(1000)
                //TODO ChangeColor when finished and still running
                if (secondsLeft <= 0 && !isFinished) {
                    isFinished = true;
                    playAlarm();
                }
                if (isFinished) secondsLeft++;
                else secondsLeft--;
                setTimer();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}