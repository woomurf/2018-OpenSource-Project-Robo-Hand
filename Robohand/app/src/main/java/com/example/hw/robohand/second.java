package com.example.hw.robohand;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class second extends AppCompatActivity {

    BluetoothSPP2 bt;
    int state;

    private Button check;

    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity2);

        Intent in = getIntent();
        bt = (BluetoothSPP2)in.getSerializableExtra("bt");
        state = bt.getServiceState();

        check = (Button)findViewById(R.id.check);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(second.this,state,Toast.LENGTH_SHORT).show();
            }
        });
    }


}
