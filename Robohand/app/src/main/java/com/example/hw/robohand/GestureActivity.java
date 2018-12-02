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

    private dataString dSet;

    BluetoothSPP GESTURE_BT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);
        back = (Button)findViewById(R.id.gesture_back);
        settings = (Button)findViewById(R.id.gesture_settings);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GestureActivity.this.finish();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestureActivity.this, settings_Gesture.class);
                startActivityForResult(intent, settings_Gesture.FINISH);
            }
        });

        dSet = new dataString();

        gestureLayout = (LinearLayout)findViewById(R.id.gestureDraw);

        GESTURE_BT = new BluetoothSPP(GestureActivity.this);
        GESTURE_BT.getBluetoothAdapter();
        GESTURE_BT.setupService();

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
                GESTURE_BT.send(dSet.getData(fingers, "SWIPE UP"), true);
                Toast.makeText(getApplicationContext(), "swiped " + fingers + " up", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onSwipeDown(int fingers, long gestureDuration, double gestureDistance) {
                GESTURE_BT.send(dSet.getData(fingers, "SWIPE DOWN"), true);
                Toast.makeText(getApplicationContext(), "swiped " + fingers + " down", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onSwipeLeft(int fingers, long gestureDuration, double gestureDistance) {
                GESTURE_BT.send(dSet.getData(fingers, "SWIPE RIGHT"), true);
                Toast.makeText(getApplicationContext(), "swiped " + fingers + " left", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onSwipeRight(int fingers, long gestureDuration, double gestureDistance) {
                GESTURE_BT.send(dSet.getData(fingers, "SWIPE LEFT"), true);
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
    /**
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == settings_Gesture.FINISH) {
            if(resultCode == Activity.RESULT_OK) {
                FIRST_BT.connect(data);
                String address = data.getExtras().getString(BluetoothState.EXTRA_DEVICE_ADDRESS);
                TO_MAIN = new Intent(BluetoothSetting.this, MainActivity.class);
                TO_MAIN.putExtra("address", address);
                CHECK_CONNECT = true;
            }
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                FIRST_BT.setupService();
                FIRST_BT.startService(BluetoothState.DEVICE_ANDROID);
            }
        }
    }
    */
}