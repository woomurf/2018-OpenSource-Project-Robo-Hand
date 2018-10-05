package com.example.hw.robohand;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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

    private static final String TAG = "BT activity";

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

        mDevicesListView.setAdapter(mBTArrayAdapter);

        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        mBTArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

        if (btService == null) {
            btService = new BluetoothService();
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
                if (mBTAdapter != null) {
                    bluetoothOn();
                }
                else{
                    Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않는 기기입니다.", Toast.LENGTH_LONG).show();

                }
            }
        });


        // 블루투스 끄기
        mBtOff.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothOff();

            }
        });

        mListPairedDeviceBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listPairedDevices(view);
            }
        });

        mDiscover.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                discover(view);
            }
        });

        mDevicesListView.setOnItemClickListener(mDeviceClickListener);
    }


    public void bluetoothOn(){
        Log.i(TAG,"Check the enabled Bluetooth");

        if(mBTAdapter.isEnabled()){
            Log.d(TAG,"Bluetooth Enable now");
        }
        else{
            Log.d(TAG,"Bluetooth Enable Request");

            Intent iEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(iEnable,REQUEST_CODE_BT);  // bluetooth 허가 팝업 창을 띄운다.

        }
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

    public void bluetoothOff(){
        mBTAdapter.disable(); // turn off
    }


    // 새로운 device 를 찾는 메소드
    private void discover(View view){
        // Check if the device is already discovering
        if(mBTAdapter.isDiscovering()){
            mBTAdapter.cancelDiscovery();
            Toast.makeText(getApplicationContext(),"Discovery stopped",Toast.LENGTH_SHORT).show();
        }
        else{
            if(mBTAdapter.isEnabled()) {
                mBTArrayAdapter.clear(); // clear items
                mBTAdapter.startDiscovery();
                Toast.makeText(getApplicationContext(), "Discovery started", Toast.LENGTH_SHORT).show();
                registerReceiver(blReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
                // adapter가 startDiscovery()를 사용하면 broadcastReceiver를 통해 전해줘야 하나봄.
            }
            else{
                Toast.makeText(getApplicationContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 방송 수신자. 이 프로그램에서는 device list에 device name과 address를 추가해주는 역할을 한다.(새로 찾은 디바이스)
    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // add the name to the list
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                mBTArrayAdapter.notifyDataSetChanged();
            }
        }
    };



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

            Intent btIntent = new Intent(getApplicationContext(),MainActivity.class);
            btIntent.putExtra("btService",btService);
            startActivity(btIntent);
        }
    };
}
