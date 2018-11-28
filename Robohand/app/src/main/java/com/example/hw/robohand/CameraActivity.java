package com.example.hw.robohand;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

public class CameraActivity extends AppCompatActivity {

    private Button back;
    private Button settings;

    private BluetoothSPP CAMERA_BT;

    Intent GET_MAC_ADDRESS;
    String MAC_ADDRESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        back = (Button)findViewById(R.id.camera_back);

        CAMERA_BT = new BluetoothSPP(CameraActivity.this);
        CAMERA_BT.setupService();

        try {
            GET_MAC_ADDRESS = getIntent();
            MAC_ADDRESS = GET_MAC_ADDRESS.getStringExtra("address");
            CAMERA_BT.startService(BluetoothState.DEVICE_OTHER);
            CAMERA_BT.connect(MAC_ADDRESS);
        } catch (Exception e) {
            Toast.makeText(this,"BLUETOOTH DEVICE CONNECTION ERROR",Toast.LENGTH_SHORT).show();
            finish();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraActivity.this.finish();
            }
        });
    }
}