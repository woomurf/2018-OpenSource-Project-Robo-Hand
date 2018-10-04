package com.example.hw.robohand;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Main";

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_CODE_BT = 2;

    private Button button1;
    private Button button2;
    private Button button3;
    private ImageButton btSet;

    private BluetoothService btService = null;

    private final Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button)findViewById(R.id.Button1);
        button2 = (Button)findViewById(R.id.Button2);
        button3 = (Button)findViewById(R.id.Button3);
        btSet = (ImageButton)findViewById(R.id.BTsetting);


        if(btService == null){
            btService = new BluetoothService(this,mHandler);
        }

        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btService.getConnectStatus()){
                    btService.sendMessage("1");
                }
            }
        });

        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btService.getConnectStatus()){
                    btService.sendMessage("2");
                }
            }
        });

        button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btService.getConnectStatus()){
                    btService.sendMessage("3");
                }
            }
        });

        //액티비티 전환

        btSet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BTactivity.class);
                startActivity(intent);

            }
        });





    }



    @Override
    public void onClick(View view) {
        if(btService.getDeviceState()){
            btService.enableBluetooth();
        }
        else{
            finish();
        }
    }
}
