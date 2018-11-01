package com.example.hw.robohand;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class second extends AppCompatActivity {

    BluetoothSPP2 bt;

    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity2);

        Intent in = getIntent();
        bt = (BluetoothSPP2)in.getSerializableExtra("bt");
    }
}
