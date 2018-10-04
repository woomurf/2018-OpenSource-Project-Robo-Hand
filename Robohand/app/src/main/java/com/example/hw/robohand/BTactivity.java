package com.example.hw.robohand;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class BTactivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btactivity);

        Button button = (Button)findViewById(R.id.btn_back);

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "이전 액티비티로 돌아갑니다.", Toast.LENGTH_LONG).show();

                finish();
            }
        });
    }

}
