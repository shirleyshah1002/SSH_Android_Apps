package com.example.RaspberryPiController;


import com.jcraft.jsch.*;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.*;
import android.view.View;
import static com.example.RaspberryPiController.Pi.getTracker;
import android.widget.*;

import java.io.*;

import java.util.Properties;


public class MainActivity extends AppCompatActivity {

    //File file
    TextView txt;
    Button sendCommand; //ch
    Button newPi;
    RadioGroup rg;
    Button refresh;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();

        db = new DatabaseHelper(this);
        rg = (RadioGroup) findViewById(R.id.piGroup);
        txt =(TextView) findViewById(R.id.cmdOut);
        refresh = (Button) findViewById(R.id.restart);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
        addRadioButton();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Intent intent = new Intent(getBaseContext(), ButtonClickedActivity.class);
                RadioButton rb = (RadioButton) findViewById(checkedId);
                String name = rb.getText().toString();
                intent.putExtra("name", name);
                startActivity(intent);



            }
        });
        //txt.setText("wow please work");
        final Button newPi = (Button) findViewById(R.id.add_pi);
        newPi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), newPi.class);
                startActivity(intent);
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
    protected void addRadioButton() {
        Cursor data = db.getAllData();
        while (data.moveToNext()) {
            RadioButton temp = new RadioButton(this);
            temp.setId(View.generateViewId());
            temp.setText(data.getString(1));
            rg.addView(temp);
        }
    }
    protected void refresh() {
        rg.removeAllViews();
        for (String key: getTracker().keySet()) {
            RadioButton temp = new RadioButton(this);
            temp.setId(View.generateViewId());
            temp.setText(key);
            rg.addView(temp);



        }

    }


    protected String getPrivateKey() throws IOException {
        InputStream inputStream = getAssets().open("id_rsa");
        return "hello";

    }
}


