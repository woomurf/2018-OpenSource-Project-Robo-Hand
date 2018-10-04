package com.example.hw.robohand;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Set;

public class BTactivity extends Activity {

    private static final int REQUEST_CODE_BT = 2;

    private Button mBack;
    private Button mBtOn;
    private Button mBtOff;
    private Button mDiscover;
    private Button mListPairedDevice;
    private ListView mDevicesListView;

    BluetoothService btService = null;

    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btactivity);

        mBack = (Button) findViewById(R.id.btn_back);
        mBtOn = (Button) findViewById(R.id.btOn);
        mBtOff = (Button) findViewById(R.id.btOff);
        mDiscover = (Button) findViewById(R.id.discover);
        mListPairedDevice = (Button) findViewById(R.id.ListpairedDevice);

        if (btService == null) {
            btService = new BluetoothService(this, mHandler);
        }

        mBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "이전 액티비티로 돌아갑니다.", Toast.LENGTH_LONG).show();

                finish();
            }
        });


        // 블루투스 켜기
        mBtOn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!btService.getDeviceState()) {
                    btService.bluetoothOn();
                }
            }
        });


        // 블루투스 끄기
        mBtOff.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                btService.bluetoothOff();

            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent Data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_CODE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {


            } else {
                Toast.makeText(getApplicationContext(), "블루투스 연결 거부", Toast.LENGTH_LONG).show();

            }
        }

    }
}

