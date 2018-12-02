package com.example.hw.robohand;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class settings_Gesture extends AppCompatActivity {

    public static final int FINISH = 0;

    EditText SWIPE_UP[];
    String su[];

    Button back;
    Button save;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.settings_gesture);

        back = (Button)findViewById(R.id.settings_gesture_back);
        save = (Button)findViewById(R.id.settings_gesture_save);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        SWIPE_UP = new EditText[4];
        su = new String[4];

        SWIPE_UP[0] = (EditText)findViewById(R.id.su_edit_0);
        SWIPE_UP[0].setImeOptions(EditorInfo.IME_ACTION_DONE);
        SWIPE_UP[0].setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    su[0] = SWIPE_UP[0].getText().toString();
                    return true;
                }
                return false;
            }
        });
    }
}