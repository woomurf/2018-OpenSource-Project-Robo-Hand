package com.example.hw.robohand;

import android.view.View;

public abstract class ButtonClickListener implements View.OnClickListener {

    protected int index;

    public ButtonClickListener(int index) {
        this.index = index;
    }
}
