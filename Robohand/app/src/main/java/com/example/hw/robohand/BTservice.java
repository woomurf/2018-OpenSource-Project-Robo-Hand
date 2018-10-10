package com.example.hw.robohand;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.os.Bundle;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;
import java.lang.Thread;
import java.lang.String;

public class BTservice extends Service {

    private BluetoothAdapter mBluetoothAdapter;
    public static final String BT_DEVICE = "btdevice";
    public static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    public static final int STATE_NONE = 0; // we're doing nothing
    public static final int STATE_LISTEN = 1; // now listening for incoming
    // connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing
    // connection
    public static final int STATE_CONNECTED = 3; // now connected to a remote
    // device
    private ConnectThread mConnectThread;
    private static ConnectedThread mConnectedThread;
    // public mInHangler mHandler = new mInHangler(this);
    private static Handler mHandler = null;
    public static int mState = STATE_NONE;
    public static String deviceName;



    // Service object return
    class BTbinder extends Binder{
        BTservice getService(){
            return BTservice.this;
        }
    }


    IBinder mBinder = new BTbinder();


    public BTservice(){
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mBluetoothAdapter == null){
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if(intent == null){
            return Service.START_STICKY;
        }
        else{
            String address = intent.getStringExtra("address");
            connectToDevice(address);
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }





    // Check the device support bluetooth
    public boolean getDeviceState(){
        Log.d(BT_DEVICE,"Check the Bluetooth support");

        if(mBluetoothAdapter == null){
            Log.d(BT_DEVICE,"Bluetooth is not available");

            return false;
        }
        else {
            Log.d(BT_DEVICE,"Bluetooth is available");

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

    private synchronized void connectToDevice(String macAddress) {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(macAddress);
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    private void setState(int state) {
       BTservice.mState = state;
        if (mHandler != null) {
           // mHandler.obtainMessage(a.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
        }
    }

    public synchronized void stop() {
        setState(STATE_NONE);
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
        stopSelf();
    }

    @Override
    public boolean stopService(Intent name) {
        setState(STATE_NONE);
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        mBluetoothAdapter.cancelDiscovery();
        return super.stopService(name);
    }

    private void connectionFailed() {
        BTservice.this.stop();
  /*      Message msg = mHandler.obtainMessage(AbstractActivity.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(AbstractActivity.TOAST, getString(R.string.error_connect_failed));
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        */
    }

    private void connectionLost() {
        BTservice.this.stop();
      /*  Message msg = mHandler.obtainMessage(.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(AbstractActivity.TOAST, getString(R.string.error_connect_lost));
        msg.setData(bundle);
        mHandler.sendMessage(msg);*/
    }

    private static Object obj = new Object();


    private synchronized void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();

        // Message msg =
        // mHandler.obtainMessage(AbstractActivity.MESSAGE_DEVICE_NAME);
        // Bundle bundle = new Bundle();
        // bundle.putString(AbstractActivity.DEVICE_NAME, "p25");
        // msg.setData(bundle);
        // mHandler.sendMessage(msg);
        setState(STATE_CONNECTED);

    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            this.mmDevice = device;
            BluetoothSocket tmp = null;
            try {
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(SPP_UUID));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmSocket = tmp;
        }

        @Override
        public void run() {
            setName("ConnectThread");
            mBluetoothAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
            } catch (IOException e) {
                try {
                    mmSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                connectionFailed();
                return;

            }
            synchronized (BTservice.this) {
                mConnectThread = null;
            }
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("BTservice", "close() of connect socket failed", e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e("Bluetooth Service", "temp sockets not created", e);
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        @Override
        public void run() {
          //  Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (mState == STATE_CONNECTED) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);

                    // Send the obtained bytes to the UI Activity
             //       mHandler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                //    Log.e(TAG, "disconnected", e);
                    connectionLost();
                    BTservice.this.stop();
                    break;

                }
            }
        }


        private byte[] btBuff;


        public void write(String input) {
            byte[] buffer = input.getBytes();

            try {
                mmOutStream.write(buffer);

                // Share the sent message back to the UI Activity
              //  mHandler.obtainMessage(AbstractActivity.MESSAGE_WRITE, buffer.length, -1, buffer).sendToTarget();
            } catch (IOException e) {
                Log.e("BTservice", "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();

            } catch (IOException e) {
                Log.e("BTservice", "close() of connect socket failed", e);
            }
        }

    }

    @Override
    public void onDestroy() {
        stop();
        Log.d("BTservice", "Destroyed");
        super.onDestroy();
    }

    private void sendMsg(int flag) {
        Message msg = new Message();
        msg.what = flag;
        handler.sendMessage(msg);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {//
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 3:

                        break;

                    case 4:

                        break;
                    case 5:
                        break;

                    case -1:
                        break;
                }
            }
            super.handleMessage(msg);
        }

    };
}
