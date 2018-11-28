package com.example.hw.robohand;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button ButtonControl;
    private Button GestureControl;
    private Button CameraControl;
    private Button Settings;

    Intent GET_MAC_ADDRESS;
    String MAC_ADDRESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButtonControl = (Button) findViewById(R.id.ButtonControl);
        GestureControl = (Button) findViewById(R.id.GestureControl);
        CameraControl = (Button) findViewById(R.id.CameraControl);
        Settings = (Button) findViewById(R.id.Settings);

        try {
            GET_MAC_ADDRESS = getIntent();
            MAC_ADDRESS = GET_MAC_ADDRESS.getStringExtra("address");
        } catch (Exception e) {
            Toast.makeText(this, "not connect", Toast.LENGTH_SHORT).show();
        }

        ButtonControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ButtonActivity.class);
                intent.putExtra("address", MAC_ADDRESS);
                startActivity(intent);
            }
        });

        GestureControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GestureActivity.class);
                intent.putExtra("address", MAC_ADDRESS);
                startActivity(intent);
            }
        });

        CameraControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                intent.putExtra("address", MAC_ADDRESS);
                startActivity(intent);
            }
        });

        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}