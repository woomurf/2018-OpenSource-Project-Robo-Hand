package com.example.hw.robohand;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.util.Log;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;
import 	java.lang.reflect.Method;

public class BluetoothService implements Serializable{

    private static final String TAG = "BluetoothService";

    // INTENT CODE SETTING
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private final static int MESSAGE_READ = 2;
    private static final int REQUEST_CODE_BT = 3;

    private BluetoothAdapter btAdapter;
    private BluetoothSocket mBTSocket;

    // Serial port Service UUID
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private ConnectedThread mConnectedThread;


    public BluetoothService(){

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




    public boolean getConnectStatus(){
        if(mConnectedThread != null){
            return true;
        }
        else{
            return false;
        }
    }

    public void sendMessage(String msg){
        if(mConnectedThread != null){
            mConnectedThread.write(msg);
        }
    }

    // bluetooth socket 만들기
    // socket은 안드로이드와 통신 대상 사이에서 통로(?), 파이프(?) 역할을 하는 중요한 객체이다.

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, MY_UUID);
        } catch (Exception e) {
            Log.e(TAG, "Could not create Insecure RFComm Connection",e);
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }


    // BT activity 에서 device address를 받아와서 직접 연결하는 메소드.
    public void connectDevice(String deviceAddress){

        final String address = deviceAddress;

        new Thread()
        {
            public void run() {
                boolean fail = false;

                BluetoothDevice device = btAdapter.getRemoteDevice(address);

                try {
                    mBTSocket = createBluetoothSocket(device);
                } catch (IOException e) {
                    fail = true;

                }
                // Establish the Bluetooth socket connection.
                try {
                    mBTSocket.connect();
                } catch (IOException e) {
                    try {
                        fail = true;
                        mBTSocket.close();
                    } catch (IOException e2) {
                        //insert code to deal with this

                    }
                }
                if(fail == false) {
                    mConnectedThread = new ConnectedThread(mBTSocket);
                    mConnectedThread.start();
                }
            }
        }.start();
    }



    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.available();
                    if(bytes != 0) {
                        buffer = new byte[1024];
                        SystemClock.sleep(100); //pause and wait for rest of data. Adjust this depending on your sending speed.
                        bytes = mmInStream.available(); // how many bytes are ready to be read?
                        bytes = mmInStream.read(buffer, 0, bytes); // record how many bytes we actually read
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }




}
