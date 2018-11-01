/*
   Bluetooth SPP를 이용하여 bluetooth 연결을 한다.
   Button Mode activity이다.

 */




package com.example.hw.robohand;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";

    private static final int REQUEST_ENABLE_BT = 2;


    private Button button1;
    private Button button2;
    private Button button3;
    private Button btOn;
    private Button btOff;
    private Button connect;

    private BluetoothSPP2 bt;

    private Button test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button) findViewById(R.id.Button1);
        button2 = (Button) findViewById(R.id.Button2);
        button3 = (Button) findViewById(R.id.Button3);


        btOn = (Button)findViewById(R.id.bt_on);
        btOff = (Button)findViewById(R.id.bt_off);
        connect = (Button)findViewById(R.id.connect);

        test = (Button)findViewById(R.id.test);

        bt = new BluetoothSPP2(MainActivity.this);
        bt.getBluetoothAdapter();
        bt.setupService();



        // bluetooth device와 연결한다.
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!bt.isBluetoothAvailable() || !bt.isBluetoothEnabled()){
                    Toast.makeText(MainActivity.this,"NEED TO TURN ON BLUETOOTH",Toast.LENGTH_SHORT).show();
                }
                else{
                    bt.startService(BluetoothState.DEVICE_OTHER);
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });


        // bluetooth 가 지원되는지 체크하고, bluetooth가 꺼져있다면 bluetooth를 킨다.
        btOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!bt.isBluetoothAvailable()){
                    Toast.makeText(MainActivity.this,"NOT SUPPORT BLUETOOTH",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(!bt.isBluetoothEnabled()){
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                        Toast.makeText(MainActivity.this,"BLUETOOTH TURN ON",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // BTSPP 객체를 끈다.
        btOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt.stopService();
            }
        });

        // 각 버튼의 숫자를 보낸다.
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bt.isServiceAvailable()){
                    bt.send("1",true);
                }
                else{
                    Toast.makeText(MainActivity.this,"not connected",Toast.LENGTH_SHORT).show();
                }
            }
        });

        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bt.isServiceAvailable()){
                    bt.send("2",true);
                }
                else{
                    Toast.makeText(MainActivity.this,"not connected",Toast.LENGTH_SHORT).show();
                }
            }
        });

        button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bt.isServiceAvailable()){
                    bt.send("3",true);
                }
                else{
                    Toast.makeText(MainActivity.this,"not connected",Toast.LENGTH_SHORT).show();
                }
            }
        });


        // bluetooth spp 객체가 device와의 연결 상태가 변할 때마다 메세지를 출력한다.
        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                // Do something when successfully connected
                Toast.makeText(MainActivity.this,"Connected",Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() {
                // Do something when connection was disconnected
                Toast.makeText(MainActivity.this,"Disconnected",Toast.LENGTH_SHORT).show();

            }

            public void onDeviceConnectionFailed() {
                // Do something when connection failed
                Toast.makeText(MainActivity.this,"connect fail",Toast.LENGTH_SHORT).show();

            }
        });

        test.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, second.class);
                in.putExtra("bt",bt);
                startActivity(in);

            }
        });

    }


    // connect 버튼을 누르고 device 를 선택했을 때, 정상적이라면 device 와 연결한다.
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);
                //setup();
            } else {
                // Do something if user doesn't choose any device (Pressed back)
            }
        }
    }


}