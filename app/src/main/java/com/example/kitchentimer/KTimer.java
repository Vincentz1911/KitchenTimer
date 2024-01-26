package com.example.kitchentimer;

import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.core.content.ContextCompat;

public class KTimer extends Thread {

    //TODO Save state (remember the time for alarms)
    public View view;
    private String name;
    private int secondsLeft;

    private boolean isRunning, isFinished, isDeleted;
    private NumberPicker npSec, npMin, npHour;
    private Button btnStart, btnDelete;

    public KTimer(View view) {
        this.view = view;
        name = "Timer";
        init();
        start();

        btnStart.setOnClickListener(x -> {
            if (!isRunning) {
                btnStart.setText("Stop");
                secondsLeft = getSecondsLeft();
                isRunning = true;
            } else {
                btnStart.setText("Start");
                isRunning = false;
                view.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.border));
                if (isFinished) isFinished = false;
            }
        });

        btnDelete.setOnClickListener(v1 -> {
            isDeleted = true;
            ((ViewGroup) view.getParent()).removeView(view);
        });
    }

    private void init() {
        npSec = view.findViewById(R.id.np_seconds);
        npMin = view.findViewById(R.id.np_minutes);
        npHour = view.findViewById(R.id.np_hours);
        btnStart = view.findViewById(R.id.btn_start);
        btnDelete = view.findViewById(R.id.btn_delete);

        npSec.setMinValue(0);
        npSec.setMaxValue(59);
        npMin.setMinValue(0);
        npMin.setMaxValue(59);
        npHour.setMinValue(0);
        npHour.setMaxValue(24);
    }

    private void setTimer() {
        npHour.setValue(secondsLeft / 3600);
        npMin.setValue((secondsLeft % 3600) / 60);
        npSec.setValue(secondsLeft % 60);
    }

    public int getSecondsLeft() {
        return npHour.getValue() * 3600 + npMin.getValue() * 60 + npSec.getValue();
    }

    public void run() {
        while (!isDeleted) {
            if (isRunning) {
                //TODO Calculate time it ends in realtime instead of sleep(1000)
                //TODO ChangeColor when finished and still running
                if (secondsLeft <= 0 && !isFinished) {
                    isFinished = true;
                    view.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.finished_border));
                    MediaPlayer.create(view.getContext(), R.raw.alarm).start();
                }
                if (isFinished) secondsLeft++; else secondsLeft--;
                setTimer();

                try {Thread.sleep(1000);}
                catch (InterruptedException e) {throw new RuntimeException(e);}
            }
        }
    }
}