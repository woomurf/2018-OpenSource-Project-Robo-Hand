package com.example.hw.robohand;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

public class ButtonActivity extends AppCompatActivity {

    private Button back;
    private Button settings;
    private TextView connectionCheck;
    private Button button[];
    private dataString dSet;

    BluetoothSPP BUTTON_BT;

    Intent GET_MAC_ADDRESS;
    String MAC_ADDRESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);

        back = (Button)findViewById(R.id.button_back);

        connectionCheck = (TextView)findViewById(R.id.connectionCheck);

        dSet = new dataString();

        button = new Button[12];
        button[0] = (Button)findViewById(R.id.button1);
        button[1] = (Button)findViewById(R.id.button2);
        button[2] = (Button)findViewById(R.id.button3);
        button[3] = (Button)findViewById(R.id.button4);
        button[4] = (Button)findViewById(R.id.button5);
        button[5] = (Button)findViewById(R.id.button6);
        button[6] = (Button)findViewById(R.id.button7);
        button[7] = (Button)findViewById(R.id.button8);
        button[8] = (Button)findViewById(R.id.button9);
        button[9] = (Button)findViewById(R.id.button10);
        button[10] = (Button)findViewById(R.id.button11);
        button[11] = (Button)findViewById(R.id.button12);

        BUTTON_BT = new BluetoothSPP(ButtonActivity.this);
        BUTTON_BT.getBluetoothAdapter();
        BUTTON_BT.setupService();

        try {
            GET_MAC_ADDRESS = getIntent();
            MAC_ADDRESS = GET_MAC_ADDRESS.getStringExtra("address");
            BUTTON_BT.startService(BluetoothState.DEVICE_OTHER);
            BUTTON_BT.connect(MAC_ADDRESS);
        } catch (Exception e) {
            Toast.makeText(this, "BLUETOOTH DEVICE CONNECTION ERROR", Toast.LENGTH_SHORT).show();
            finish();
        }

        for(int idx = 0; idx < 12; idx++) {
            button[idx].setOnClickListener(new ButtonClickListener(idx) {
                @Override
                public void onClick(View v) {
                    BUTTON_BT.send(dSet.getData(index), true);
                }
            });
        }
        connectionCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BUTTON_BT.isBluetoothEnabled() && BUTTON_BT.isBluetoothAvailable()) {
                    connectionCheck.setText("GOOD");
                }
                else
                    connectionCheck.setText("MISS");
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}