package com.example.hw.robohand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.hw.robohand.R;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import in.championswimmer.sfg.lib.SimpleFingerGestures;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";

    private static final int REQUST_CODE_BT = 2;

    // 버튼 객체 생성
    /*
    private Button button1;
    private Button button2;
    private Button button3;
    */
    private Button btOn;
    private Button btOff;
    private Button connect;
    private Button change;
    private LinearLayout gestureLayout;
    private BluetoothSPP bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        button1 = (Button) findViewById(R.id.Button1);
        button2 = (Button) findViewById(R.id.Button2);
        button3 = (Button) findViewById(R.id.Button3);
        */

        btOn = (Button)findViewById(R.id.bt_on);
        btOff = (Button)findViewById(R.id.bt_off);
        connect = (Button)findViewById(R.id.connect);
        change = (Button)findViewById(R.id.change);
        gestureLayout = (LinearLayout)findViewById(R.id.gestureLayout);
        bt = new BluetoothSPP(this);

        // 제스처 이벤트를 입력받을 레이아웃 객체 생성
        final LinearLayout gestureListen = (LinearLayout) findViewById(R.id.layout1);

        // 블루투스 서비스를 이용하기 위해서 bt object setup
        bt.setupService();
        // FIXME 스마트폰에서 블루투스가 켜진 상태에서만 어플 진입 가능 << 수정 바람
        bt.startService(BluetoothState.DEVICE_OTHER);

        // connect 버튼의 onClick 메소드 오버라이딩
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
            }
        });

        // btOn 버튼의 onClick 메소드 오버라이딩
        btOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 스마트폰 블루투스가 꺼져있을 때 블루투스를 켜기 위한 팝업 제공
            }
        });

        // btOff 버튼의 onClick 메소드 오버라이딩
        btOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt.stopService();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(this, SubActivity.class);
                //startActivity(intent);
            }
        });

        /*
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bt.isServiceAvailable()) {
                    bt.send("1",true);
                }
                else {
                    Toast.makeText(MainActivity.this,"not connected",Toast.LENGTH_SHORT).show();
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(bt.isServiceAvailable()) {
                    bt.send("2",true);
                }
                else {
                    Toast.makeText(MainActivity.this,"not connected",Toast.LENGTH_SHORT).show();
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bt.isServiceAvailable()) {
                    bt.send("3",true);
                }
                else {
                    Toast.makeText(MainActivity.this,"not connected",Toast.LENGTH_SHORT).show();
                }
            }
        });
        */

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

        // 김다훈이 찾은 제스처 오프소스 객체를 생성
        SimpleFingerGestures mySfg = new SimpleFingerGestures();
        // 제스처 이벤트를 리스너에게 전달해주는 메소드, boolean type 파라미터로 켜고 끌 수 있다.
        mySfg.setConsumeTouchEvents(true);
        // 제스처 객체의 리스너를 오버라이딩하여 재구성
        mySfg.setOnFingerGestureListener(new SimpleFingerGestures.OnFingerGestureListener() {
            @Override
            public boolean onSwipeUp(int fingers, long gestureDuration, double gestureDistance) {
                if(bt.isServiceAvailable()) {
                    bt.send("1",true);
                    Toast.makeText(getApplicationContext(), "swiped " + fingers + " up\n You sended 1", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this,"not connected",Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onSwipeDown(int fingers, long gestureDuration, double gestureDistance) {
                if(bt.isServiceAvailable()) {
                    bt.send("2",true);
                    Toast.makeText(getApplicationContext(), "swiped " + fingers + " down\n You sended 2", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this,"not connected",Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onSwipeLeft(int fingers, long gestureDuration, double gestureDistance) {
                if(bt.isServiceAvailable()) {
                    bt.send("3",true);
                    Toast.makeText(getApplicationContext(), "swiped " + fingers + " left\n You sended 3", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this,"not connected",Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onSwipeRight(int fingers, long gestureDuration, double gestureDistance) {
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
        // 위에서 선언한 레이아웃에 리스너를 부착
        gestureListen.setOnTouchListener(mySfg);
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