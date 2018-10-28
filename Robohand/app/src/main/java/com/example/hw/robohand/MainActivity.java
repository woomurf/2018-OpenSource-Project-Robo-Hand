/*
    2018 KMU OPEN SOURCE PROJECT ROBOHAND
    HYEONWOONG WOO, DAHUN KIM, HANUL BAE

    BluetoothService 객체를 생성하여 Button activity, BTactivity 그리고 추가될 gesture activity 에서
    같은 객체를 사용할 수 있도록 한다.

    BluetoothService 객체는 bluetooth 연결, 기기 검색, 페어링된 기기와 연결, string 보내기 등의
    기능을 수행하는 객체이다.

    현재 registerReceiver, BroadcastReceiver 등에 대한 이해가 부족하여 BluetoothService의
    discover 메소드를 구현하는데 곤란함이 있음. - 2018 10 04

 */

package com.example.hw.robohand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import in.championswimmer.sfg.lib.SimpleFingerGestures;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";

    private static final int REQUEST_CODE_BT = 2;

    private SimpleFingerGestures mySfg = new SimpleFingerGestures();

    private Button button1;
    private Button button2;
    private Button button3;
    private Button btOn;
    private Button btOff;
    private Button connect;

    private BluetoothSPP bt;

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

        bt = new BluetoothSPP(this);
        bt.getBluetoothAdapter();
        bt.setupService();
        bt.startService(BluetoothState.DEVICE_OTHER);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
            }
        });

        btOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt.stopService();
            }
        });

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
    }

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