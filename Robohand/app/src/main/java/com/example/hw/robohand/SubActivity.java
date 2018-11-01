package com.example.hw.robohand;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SubActivity extends AppCompatActivity {

    private Button change_Sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);

        change_Sub = (Button)findViewById(R.id.change1);

        change_Sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubActivity.this.finish();
            }
        });
    }
}