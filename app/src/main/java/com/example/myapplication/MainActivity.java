package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import com.jcraft.jsch.*;

import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.view.View;
import android.widget.*;
import android.app.Activity;

import java.io.*;
import java.util.Properties;


public class MainActivity extends AppCompatActivity {
    JSch current = new JSch();
    //File file

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button Pi_One = (Button) findViewById(R.id.Pi_1);
        Pi_One.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Button Pi_Two = (Button) findViewById(R.id.Pi2);
        Pi_Two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Integer, Void, Void>() {
                    protected  Void doInBackground(Integer... params) {
                        try {
                            onClickOne();;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(1);


            }
        });

    }
    protected void onClickOne () throws JSchException {
        Intent intent = new Intent(this, ButtonClickedActivity.class);
        Intent intentThrown = new Intent(this, ExceptionThrown.class);

        String user = "guest";
        String host = "192.168.2.57";
        try {
            Session session = current.getSession(user, host);
            Properties prop = new Properties();
//            prop.put("StrictHostKeyChecking", "no");
//            session.setConfig(prop);
//            System.out.println("it clicked");
            startActivity(intent);
            ChannelExec channelSsh = (ChannelExec)session.openChannel("exec");
            channelSsh.setCommand("ls");
            channelSsh.connect(5000);
//            Context context = getApplicationContext();
//            CharSequence text = "It Worked!";
//            int duration = Toast.LENGTH_SHORT;
//
//            Toast toast = Toast.makeText(context, text, duration);
//            toast.show();







        }
        catch(Exception e) {
            System.out.println("Exception thrown");
            startActivity(intentThrown);
            e.printStackTrace();
//            Context context = getApplicationContext();
//            CharSequence text = "Hello toast!";
//            int duration = Toast.LENGTH_SHORT;
//
//            Toast toast = Toast.makeText(context, text, duration);
//            toast.show();


        }

    }
}


