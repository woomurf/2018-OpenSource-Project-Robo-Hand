package com.example.hw.robohand;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

import static app.akexorcist.bluetotohspp.library.BluetoothState.REQUEST_ENABLE_BT;


public class bluetoothsetting extends AppCompatActivity {

    TextView txt_btStatus, txt_connectStatus, txt_deviceName;
    TextView edit_btStatus, edit_connectStatus, edit_deviceName;

    Button btn_Button, btn_gesture, btn_camera;
    Button btn_on, btn_connect, btn_disconnect;

    Button test;

    BluetoothSPP bt;

    Intent toButton,toGesture,toCamera;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        //변수 선언하기
        txt_btStatus = (TextView) findViewById(R.id.txt_btStatus);
        txt_connectStatus = (TextView) findViewById(R.id.txt_connectStatus);
        txt_deviceName = (TextView) findViewById(R.id.txt_deviceName);

        edit_btStatus = (TextView) findViewById(R.id.edit_btStatus);
        edit_connectStatus = (TextView) findViewById(R.id.edit_connectStatus);
        edit_deviceName = (TextView) findViewById(R.id.edit_deviceName);

        btn_Button = (Button) findViewById(R.id.btn_buttonMode);
        btn_gesture = (Button) findViewById(R.id.btn_gestureMode);
        btn_camera = (Button) findViewById(R.id.btn_cameraMode);

        btn_on = (Button)findViewById(R.id.btn_on);
        btn_connect = (Button)findViewById(R.id.btn_connect);
        btn_disconnect = (Button)findViewById(R.id.btn_disconnect);

        test = (Button)findViewById(R.id.btn_test);

        bt = new BluetoothSPP(bluetoothsetting.this);
        bt.getBluetoothAdapter();
        bt.setupService();

        // bluetooth가 연결되면 Toast 메세지를 띄워준다.
        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                // Do something when successfully connected
                Toast.makeText(bluetoothsetting.this,"Connected",Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() {
                // Do something when connection was disconnected
                Toast.makeText(bluetoothsetting.this,"Disconnected",Toast.LENGTH_SHORT).show();

            }

            public void onDeviceConnectionFailed() {
                // Do something when connection failed
                Toast.makeText(bluetoothsetting.this,"connect fail",Toast.LENGTH_SHORT).show();

        }
    });


}

    private void setState(){
        int state = bt.getServiceState();
        switch (state){
            case -1:
                edit_connectStatus.setText("Null");
                break;
            case 0:
                edit_connectStatus.setText("None");
                break;
            case 1:
                edit_connectStatus.setText("connecting");
                break;
            case 2:
                edit_connectStatus.setText("connecting");
                break;
            case 3:
                edit_connectStatus.setText("connected");
                break;
        }

        boolean available = bt.isBluetoothAvailable();
        boolean enable = bt.isBluetoothEnabled();

        if(!available){
            edit_btStatus.setText("do not support bluetooth in this device");
        }
        else if(!enable){
            edit_btStatus.setText("not on bluetooth");
        }
        else {
            edit_btStatus.setText("on bluetooth");
        }
    }

    private View.OnClickListener clickEvent = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()){
                // 버튼을 누르면 각 함수 실행!
                case R.id.btn_on:
                    BluetoothON();
                    setState();
                    break;

                case R.id.btn_connect:
                    connect();
                    setState();
                    break;

                case R.id.btn_disconnect:
                    disconnect();
                    setState();
                    break;

                // 버튼을 누르면 해당 화면으로 이동!
                case R.id.btn_buttonMode:
                    startActivity(toButton);
                    break;

                case R.id.btn_gestureMode:
                    startActivity(toGesture);
                    break;

                case R.id.btn_cameraMode:
                    startActivity(toCamera);
                    break;
                case R.id.btn_test:
                    try {
                        bt.send("1", true);
                    }
                    catch (Exception e){
                        Toast.makeText(bluetoothsetting.this, "send error", Toast.LENGTH_SHORT).show();
                    }
            }
        }
    };



    // bluetooth가 활성화 되어 있지 않을 경우 bluetooth를 킨다!
    private void BluetoothON(){
        if(!bt.isBluetoothAvailable()){
            Toast.makeText(bluetoothsetting.this,"this device does not support bluetooth",Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            if(!bt.isBluetoothEnabled()){
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent,REQUEST_ENABLE_BT);
                Toast.makeText(bluetoothsetting.this,"bluetooth turn on",Toast.LENGTH_SHORT).show();
            }
        }
    }

    // bluetooth가 활성화 되어 있을 경우 devideList화면으로 넘어가 연결할 디바이스를 선택한다!
    private void connect(){
        if(!bt.isBluetoothEnabled() || !bt.isBluetoothAvailable()){
            Toast.makeText(bluetoothsetting.this,"plesase bluetooth turn on",Toast.LENGTH_SHORT).show();
        }
        else{
            bt.startService(BluetoothState.DEVICE_OTHER);
            Intent intent = new Intent(getApplicationContext(), DeviceList.class);
            startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
        }
    }

    //bluetoothSPP 객체를 종료한다. 디바이스와의 연결이 끊긴다.
    private void disconnect(){
        if(bt.getServiceState() != -1){
            bt.stopService();
        }
    }


    //deviceList 화면에서 디바이스를 선택하면 resultCode를 보내고, 이를 받아 connect(data)함수를 실행한다!
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK)
                bt.connect(data);
                setState();
                toButton = new Intent(bluetoothsetting.this,MainActivity.class);
                toButton.putExtras(data);
                /*
                toGesture = new Intent(bluetoothsetting.this,gesture.class);
                toGesture.putExtras(data);
                toCamera = new Intent(bluetoothsetting.this,camera.class);
                toCamera.putExtras(data);
                */

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