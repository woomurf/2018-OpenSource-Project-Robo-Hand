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


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";

    private static final int REQUEST_CODE_BT = 2;


    private Button button1;
    private Button button2;
    private Button button3;
    private ImageButton btSet;

    private BluetoothService btService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button)findViewById(R.id.Button1);
        button2 = (Button)findViewById(R.id.Button2);
        button3 = (Button)findViewById(R.id.Button3);
        btSet = (ImageButton)findViewById(R.id.BTsetting);


        if(btService == null){
            Intent getIntent = getIntent();
            btService = (BluetoothService) getIntent.getSerializableExtra("btService");
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
                startActivityForResult(intent,REQUEST_CODE_BT);

            }
        });





    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
            }
        }
    }//onActivityResult

}
