package com.example.hw.robohand;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

import static app.akexorcist.bluetotohspp.library.BluetoothState.REQUEST_ENABLE_BT;

public class BluetoothSetting extends AppCompatActivity {
    Button START;
    BluetoothSPP FIRST_BT;
    boolean CHECK_CONNECT = false;
    Intent TO_MAIN;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_first);

        START = (Button)findViewById(R.id.START);

        FIRST_BT = new BluetoothSPP(BluetoothSetting.this);
        FIRST_BT.getBluetoothAdapter();
        FIRST_BT.setupService();

        START.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CHECK_CONNECT == false) {
                    if(!FIRST_BT.isBluetoothEnabled() || !FIRST_BT.isBluetoothAvailable()) {
                        BluetoothON();
                        return;
                    } else {
                        connect();
                        return;
                    }
                } else {
                    startActivity(TO_MAIN);
                    finish();
                }
            }
        });
    }
    private void BluetoothON() {
        if(!FIRST_BT.isBluetoothAvailable()) {
            return;
        } else {
            if(!FIRST_BT.isBluetoothEnabled()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLE_BT);
            }
        }
    }
    private void connect() {
        FIRST_BT.startService(BluetoothState.DEVICE_OTHER);
        Intent intent = new Intent(getApplicationContext(), DeviceList.class);
        startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
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
}