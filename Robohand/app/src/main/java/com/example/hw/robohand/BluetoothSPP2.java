package com.example.hw.robohand;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.io.Serializable;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class BluetoothSPP2 extends Service{

    BluetoothSPP bt;
    IBinder mBinder = new MyBinder();


    class MyBinder extends Binder {
        BluetoothSPP2 getService() {
            return BluetoothSPP2.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent){
        return mBinder;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        bt = new BluetoothSPP(getApplicationContext());
        bt.getBluetoothAdapter();
        bt.setupService();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    public int onStartCommand(Intent intent,int flags, int startid){

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                // Do something when successfully connected
                Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() {
                // Do something when connection was disconnected
                Toast.makeText(getApplicationContext(),"Disconnected",Toast.LENGTH_SHORT).show();

            }

            public void onDeviceConnectionFailed() {
                // Do something when connection failed
                Toast.makeText(getApplicationContext(),"connect fail",Toast.LENGTH_SHORT).show();

            }
        });
        return super.onStartCommand(intent,flags,startid);
    }

    // starting service
    public void startBTSPP(){
        bt.startService(BluetoothState.DEVICE_OTHER);
    }

    //stop service
    public void stopBTSPP(){
        bt.stopService();
    }

    //sending msg
    public void sending(String msg){
        bt.send(msg,true);
    }

    public boolean isBluetoothEnabled(){

        return bt.isBluetoothEnabled();
    }

    public boolean isBluetoothAvailable(){

        return bt.isBluetoothAvailable();
    }

    public boolean isServiceAvailable(){
        return bt.isServiceAvailable();
    }

    public void connect(Intent data){
        bt.connect(data);

    }




}
