package com.example.hw.robohand;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
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
import android.widget.AdapterView;
import android.widget.TextView;


import java.util.Set;

public class BTactivity extends Activity {

    private static final int REQUEST_CODE_BT = 2;

    private Button mBack;
    private Button mBtOn;
    private Button mBtOff;
    private Button mDiscover;
    private Button mListPairedDeviceBtn;
    private ListView mDevicesListView;
    private BluetoothAdapter mBTAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    private ArrayAdapter<String> mBTArrayAdapter;

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
        mListPairedDeviceBtn = (Button) findViewById(R.id.ListpairedDevice);
        mDevicesListView = (ListView)findViewById(R.id.deviceList);

        mBTAdapter = BluetoothAdapter.getDefaultAdapter();

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

        mListPairedDeviceBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listPairedDevices(view);
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



    // 기존에 등록되어 있는 디바이스들을 표시해주는 메소드. 
    private void listPairedDevices(View view) {
        mPairedDevices = mBTAdapter.getBondedDevices();
        if (mBTAdapter.isEnabled()) {
            // put it's one to the adapter
            for (BluetoothDevice device : mPairedDevices)
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());

            Toast.makeText(getApplicationContext(), "Show Paired Devices", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getApplicationContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
    }



    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            if (!mBTAdapter.isEnabled()) {
                Toast.makeText(getBaseContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
                return;
            }


            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            final String address = info.substring(info.length() - 17);


            // btService를 이용해 device address를 보내고 연결한다. 여기서는 device address를 보내기만 한다.
            btService.connectDevice(address);



        }
    };
}
