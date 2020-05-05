package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.jcraft.jsch.*;

public class ButtonClickedActivity extends AppCompatActivity {
    TextView text;
    Button commandSnd;
    JSch current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_page);
        commandSnd = (Button) findViewById(R.id.sndCommand);
        text =  (TextView) findViewById(R.id.cmdOut);


    }
    public void changeText(View v) {
        Intent intent = getIntent();
        String a = intent.getExtras().getString("results");

        text.setText(a);
    }

    public void sendCommand(String command) {



    }
}

