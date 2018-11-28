package com.example.hw.robohand;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import in.championswimmer.sfg.lib.SimpleFingerGestures;

public class GestureActivity extends AppCompatActivity {

    private Button back;
    private Button settings;
    private LinearLayout gestureLayout;

    Intent GET_MAC_ADDRESS;
    String MAC_ADDRESS;

    BluetoothSPP GESTURE_BT;

    String SWIPE_UP[];
    String SWIPE_DOWN[];
    String SWIPE_LEFT[];
    String SWIPE_RIGHT[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);
        back = (Button)findViewById(R.id.gesture_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GestureActivity.this.finish();
            }
        });

        gestureLayout = (LinearLayout)findViewById(R.id.gestureDraw);

        GESTURE_BT = new BluetoothSPP(GestureActivity.this);
        //GESTURE_BT.getBluetoothAdapter();
        GESTURE_BT.setupService();

        SWIPE_UP = new String[5];
        SWIPE_UP[0] = "0";
        SWIPE_DOWN = new String[5];
        SWIPE_DOWN[0] = "1";
        SWIPE_LEFT = new String[5];
        SWIPE_LEFT[0] = "2";
        SWIPE_RIGHT = new String[5];
        SWIPE_RIGHT[0] = "3";

        try {
            GET_MAC_ADDRESS = getIntent();
            MAC_ADDRESS = GET_MAC_ADDRESS.getStringExtra("address");
            GESTURE_BT.startService(BluetoothState.DEVICE_OTHER);
            GESTURE_BT.connect(MAC_ADDRESS);
        } catch (Exception e) {
            Toast.makeText(this,"BLUETOOTH DEVICE CONNECTION ERROR",Toast.LENGTH_SHORT).show();
            finish();
        }

        SimpleFingerGestures mySfg = new SimpleFingerGestures();
        mySfg.setConsumeTouchEvents(true);
        mySfg.setOnFingerGestureListener(new SimpleFingerGestures.OnFingerGestureListener() {
            @Override
            public boolean onSwipeUp(int fingers, long gestureDuration, double gestureDistance) {
                GESTURE_BT.send(SWIPE_UP[fingers - 1], true);
                Toast.makeText(getApplicationContext(), "swiped " + fingers + " up", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onSwipeDown(int fingers, long gestureDuration, double gestureDistance) {
                GESTURE_BT.send(SWIPE_DOWN[fingers - 1], true);
                Toast.makeText(getApplicationContext(), "swiped " + fingers + " down", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onSwipeLeft(int fingers, long gestureDuration, double gestureDistance) {
                GESTURE_BT.send(SWIPE_LEFT[fingers - 1], true);
                Toast.makeText(getApplicationContext(), "swiped " + fingers + " left", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onSwipeRight(int fingers, long gestureDuration, double gestureDistance) {
                GESTURE_BT.send(SWIPE_RIGHT[fingers - 1], true);
                Toast.makeText(getApplicationContext(), "swiped " + fingers + " right", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onPinch(int fingers, long gestureDuration, double gestureDistance) {
                Toast.makeText(getApplicationContext(), "Pinch", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onUnpinch(int fingers, long gestureDuration, double gestureDistance) {
                Toast.makeText(getApplicationContext(), "unPinch", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onDoubleTap(int fingers) {
                Toast.makeText(getApplicationContext(), "double Tap", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        gestureLayout.setOnTouchListener(mySfg);
    }
}