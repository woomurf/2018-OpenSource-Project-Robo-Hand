package com.example.hw.robohand;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothService {

    private static final String TAG = "BluetoothService";

    // INTENT CODE SETTING
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private final static int MESSAGE_READ = 2;
    private static final int REQUEST_CODE_BT = 3;

    private BluetoothAdapter btAdapter;
    private Activity mActivity;
    private Handler mHandler;

    // Serial port Service UUID
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private ConnectedThread mConnectedThread;


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
                        mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                .sendToTarget(); // Send the obtained bytes to the UI activity
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
