package com.example.robotto_opcua;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Activity1 extends AppCompatActivity {
    public TextView text_view;
    Button Stop_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);

        text_view = findViewById(R.id.text_view);
        Stop_btn = findViewById(R.id.button_stop);

        Intent intent = getIntent();
        String text = intent.getStringExtra(MainActivity.SIM_RESULT);
        text_view.setText(text);

        Stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.Run = false;
            }
        });
    }

    public void connect(View view){
        MainActivity.Run = true;
        finish();
        //new MainActivity.ConnectionAsyncTask().execute();
    }
}
