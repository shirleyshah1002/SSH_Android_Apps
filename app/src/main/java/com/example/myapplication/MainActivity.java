package com.example.myapplication;


import com.jcraft.jsch.*;
import androidx.appcompat.app.AppCompatActivity;

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
    TextView txt;
    Button sendCommand; //ch

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();
        txt =(TextView) findViewById(R.id.cmdOut);
        //txt.setText("wow please work");

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
        Intent intent = new Intent(getBaseContext(), ButtonClickedActivity.class);
        Intent intentThrown = new Intent(getBaseContext(), ExceptionThrown.class);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");

        String user = "guest";
        String host = "192.168.2.57";
        //create new JsCh object
        JSch current = new JSch();

        Session session = current.getSession(user, host);
        session.setConfig(config);
        session.setPassword("guest");




        try {

            session.connect(5000);
            ChannelExec channelSsh = (ChannelExec)session.openChannel("exec");
            System.out.print("Channel Made");
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            channelSsh.setOutputStream(baos);
            channelSsh.setCommand("ls -a");
            channelSsh.connect(5000);
            try{ Thread.sleep(1000); } catch(Exception ee) {}
            String result = new String(baos.toByteArray());

            System.out.println("CommandSent");

            System.out.println("byteCreated");

            channelSsh.disconnect();

            System.out.println(result);
            intent.putExtra("results", result);
            startActivity(intent);



        }
        catch(Exception e) {

            startActivity(intentThrown);
            System.out.println("The connection failed");
            e.printStackTrace();
        }
        //txt.setText("Please Work");

    }


    protected String getPrivateKey() throws IOException {
        InputStream inputStream = getAssets().open("id_rsa");
        return "hello";

    }
}


