package com.example.hw.robohand;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.util.Log;
import android.os.Handler;

import java.util.UUID;

public class BluetoothService {

    private static final String TAG = "BluetoothService";

    // INTENT CODE SETTING
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_CODE_BT = 2;

    private BluetoothAdapter btAdapter;
    private Activity mActivity;
    private Handler mHandler;

    private static final UUID MY_UUID = UUID.fromString("00000003-0000-1000-8000-00905F9B34FB");

    // private ConnectedThread mConnectedThread;


    public BluetoothService(Activity act,Handler handler){

        mActivity = act;
        mHandler = handler;

        btAdapter = BluetoothAdapter.getDefaultAdapter();

    }


    // Check the device support bluetooth
    public boolean getDeviceState(){
        Log.d(TAG,"Check the Bluetooth support");

        if(btAdapter == null){
            Log.d(TAG,"Bluetooth is not available");

            return false;
        }
        else {
            Log.d(TAG,"Bluetooth is available");

            return true;
        }
    }


    // Check the device bluetooth status and request bluetooth
    public void enableBluetooth(){
        Log.i(TAG,"Check the enabled Bluetooth");

        if(btAdapter.isEnabled()){
            Log.d(TAG,"Bluetooth Enable now");
        }
        else{
            Log.d(TAG,"Bluetooth Enable Request");

            Intent iEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mActivity.startActivityForResult(iEnable,REQUEST_CODE_BT);
        }
    }




}
