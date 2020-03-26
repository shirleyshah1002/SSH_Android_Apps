package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import com.jcraft.jsch.*;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.*;
import android.view.View;
import android.widget.*;
import android.app.Activity;

import java.io.*;
import java.util.Properties;


public class MainActivity extends AppCompatActivity {

    //File file

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();

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
    /*
    This method is invoked when I click a button to connect to a certain Raspberry Pi. I have set up public key authentication between the pi and my machine.
     */
    protected void onClickOne () throws JSchException {
    //set up my different intents for the two different outcomes
        Intent intent = new Intent(this, ButtonClickedActivity.class);
        Intent intentThrown = new Intent(this, ExceptionThrown.class);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");





        String user = "guest";
        String host = "192.168.2.57";
        //create new JsCh object
        JSch current = new JSch();
//        System.out.println(new File("file:\\src\\.ssh\\id_rsa").exists());
        //current.addIdentity("C:\\Users\\shirl\\.ssh\\id_rsa");



        //current.setKnownHosts();
        Session session = current.getSession(user, host);
        session.setConfig(config);
        session.setPassword("guest");



        try {



            session.connect();
            ChannelExec channelSsh = (ChannelExec)session.openChannel("exec");

            startActivity(intent);

            //simple command to test
            channelSsh.setCommand("ls");
            channelSsh.connect(5000);
            channelSsh.disconnect();

        }
        catch(Exception e) {

            startActivity(intentThrown);
            e.printStackTrace();

        }

    }


//    protected String getPrivateKey() {
//        InputStream inputStream = getAssets().open("id_rsa");
//    }
}


