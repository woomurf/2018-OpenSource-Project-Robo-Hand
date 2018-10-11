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
import com.example.hw.robohand.BTservice.BTbinder;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";

    private static final int REQUEST_CODE_BT = 2;


    private Button button1;
    private Button button2;
    private Button button3;
    private ImageButton btSet;

    private BTservice mBTservice;
    private String address = "";
    private BluetoothAdapter btAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button) findViewById(R.id.Button1);
        button2 = (Button) findViewById(R.id.Button2);
        button3 = (Button) findViewById(R.id.Button3);
        btSet = (ImageButton) findViewById(R.id.BTsetting);

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mBTservice == null){
            Intent intent = new Intent(MainActivity.this, BTservice.class);
            bindService(intent,conn,Context.BIND_AUTO_CREATE);
        }

        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBTservice == null) {
                    Toast.makeText(getBaseContext(), "Bluetooth not connect", Toast.LENGTH_SHORT).show();
                } else if (mBTservice.getConnectStatus()) {
                    mBTservice.sendMessage("1");
                    Toast.makeText(getBaseContext(), "send 1", Toast.LENGTH_SHORT).show();

                }


            }
        });

        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBTservice == null) {
                    Toast.makeText(getBaseContext(), "Bluetooth not connect", Toast.LENGTH_SHORT).show();
                } else if (mBTservice.getConnectStatus()) {
                    mBTservice.sendMessage("2");
                    Toast.makeText(getBaseContext(), "send 2", Toast.LENGTH_SHORT).show();

                }

            }
        });

        button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBTservice == null) {
                    Toast.makeText(getBaseContext(), "Bluetooth not connect", Toast.LENGTH_SHORT).show();
                } else if (mBTservice.getConnectStatus()) {
                    mBTservice.sendMessage("3");
                    Toast.makeText(getBaseContext(), "send 3", Toast.LENGTH_SHORT).show();

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


    boolean isService = false; // 서비스 중인 확인용

    ServiceConnection conn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
// 서비스와 연결되었을 때 호출되는 메서드
// 서비스 객체를 전역변수로 저장
            BTbinder mb = (BTbinder) service;
            mBTservice = mb.getService(); // 서비스가 제공하는 메소드 호출하여
// 서비스쪽 객체를 전달받을수 있슴
            isService = true;
        }

        public void onServiceDisconnected(ComponentName name) {
// 서비스와 연결이 끊겼을 때 호출되는 메서드
            isService = false;
        }
    };

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            if (requestCode == REQUEST_CODE_BT) {
                if (resultCode == Activity.RESULT_OK) {
                    address = data.getStringExtra("address");
                    Intent intent = new Intent(MainActivity.this, BTservice.class);
                    intent.putExtra("address", address);
                    bindService(intent, conn, Context.BIND_AUTO_CREATE);

                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
                }
            }
        }//onActivityResult
}
