package com.ms.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewTreeObserver;

import com.ms.module.view.turn.TurnView;

public class MainActivity extends AppCompatActivity {

    private TurnView turnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        turnView = findViewById(R.id.fastChargeView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startScanAnim();
    }

    private void startScanAnim() {
        try {
            turnView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (turnView.getViewTreeObserver().isAlive()) {
                        turnView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    turnView.startAnimation();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}