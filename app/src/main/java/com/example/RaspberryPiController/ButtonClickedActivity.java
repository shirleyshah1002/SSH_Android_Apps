package com.example.RaspberryPiController;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jcraft.jsch.*;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ButtonClickedActivity extends AppCompatActivity {
    TextView text;
    DatabaseHelper db;
    Button commandSnd;
   // JSch current;
    Session session;
    String name;
    EditText cmdInput;
    String input;
    String ip;
    Button connect;
    Button disconnect;
    ArrayList<String> lastCommands;
//    Spinner spinner;
    Button lastCmds;
    //ChannelExec channelSSH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_page);
        lastCommands = new ArrayList<String>();
        lastCommands.add("Select One");
        //updateList(lastCommands);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        lastCmds = (Button) findViewById(R.id.lastCommands);
//
        if(name == null) {
            Log.d("NAME", "The name is null");
        }
        commandSnd = (Button) findViewById(R.id.sndCommand);
        text =  (TextView) findViewById(R.id.cmdOut);
        cmdInput = (EditText)findViewById(R.id.cmdInput);
        input = cmdInput.getText().toString();

        db = new DatabaseHelper(this);


        commandSnd = (Button) findViewById(R.id.sndCommand);
        text =  (TextView) findViewById(R.id.cmdOut);
        cmdInput = (EditText)findViewById(R.id.cmdInput);
        input = cmdInput.getText().toString();
//        connect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.print("Button has been Clicked");
//                new AsyncTask<Integer, Void, Void>() {
//                    protected  Void doInBackground(Integer... params) {
//
//                        try {
//                            connect(name);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        return null;
//                    }
//                }.execute(1);
//            }
//        });

        commandSnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Integer, Void, Void>() {
                    protected  Void doInBackground(Integer... params) {

                        try {
                            sendCommand(name, input);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(1);
            }
        });



    }
    public void changeText(View v) {

        Intent intent = getIntent();
        String a = intent.getExtras().getString("results");

        text.setText(a);
    }
//    public Session connect(String n) throws JSchException {
//        if (session == null) {
//            current = new JSch();
//            Properties config = new Properties();
//            config.put("StrictHostKeyChecking", "no");
//
//            String[] results = getDataStrings(n);
//            String user = results[0];
//            String host = results[1];
//            session = current.getSession(user, host);
//            session.setPassword(results[2]);
//            session.setConfig(config);
//            setVisible(commandSnd, View.VISIBLE);
//            setVisible(disconnect, View.VISIBLE);
//            setVisible(connect, View.INVISIBLE);
//            session.connect();
//            session.setServerAliveInterval(10000);
//        } else if (session.isConnected()) {
//            Toast.makeText(ButtonClickedActivity.this, "You already have a session connected", Toast.LENGTH_SHORT);
//        }
//        return session;
//
//    }
//    public void disconnect(Session a) {
//        if (a.isConnected()) {
//            setVisible(connect, View.VISIBLE);
//            setVisible(disconnect, View.INVISIBLE);
//            setVisible(commandSnd, View.INVISIBLE);
//        }
//        a.disconnect();
//
//        setVisible(connect, View.VISIBLE);
//        setVisible(disconnect, View.INVISIBLE);
//        setVisible(commandSnd, View.INVISIBLE);
//
//    }

    public String sendCommand(String n, String command) throws JSchException {
        String cmd = cmdInput.getText().toString();
        lastCommands.add(cmd);
        //updateList(lastCommands);



        if(cmd.isEmpty()) {
            setText("Please insert a valid Command");
            return "Insert Valid Command";
        }
        System.out.println(cmd);
        Log.d("Command", cmd);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");

        String[] results = getDataStrings(n);
        String user = results[0];
        String host = results[1];
        JSch current = new JSch();
        session = current.getSession(user, host);
        session.setPassword(results[2]);
        session.setConfig(config);

        String output;
        try {
            session.connect();

            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ChannelExec channelSSH = (ChannelExec) session.openChannel("exec");
            System.out.print("Channel Made");
            channelSSH.setOutputStream(baos);
            channelSSH.setErrStream(baos);
            channelSSH.setPty(true);
            channelSSH.setCommand(cmd);
            channelSSH.connect();
            try{ Thread.sleep(1000); } catch(Exception ee) {}
            output = new String(baos.toByteArray());
            setInputText("");
            System.out.println("CommandSent");
            System.out.println("byteCreated");
            System.out.println(output);
            setText(output);
            Log.d("Command Sent Check", output);
            channelSSH.disconnect();
            session.disconnect();
        }
        catch(Exception e) {

            System.out.println("The connection failed");
            setText("The connection has failed");

            e.printStackTrace();
            return "Please Check your Selected pi's properties";
        }
        return output;

    }

    public String[] getDataStrings(String n){
        String[] result = new String[3];
        Cursor ipAddres = db.getAddress(n);
        Cursor user  = db.getUser(n);
        Cursor pass = db.getPass(n);

        while(user.moveToNext()) {
            result[0] = user.getString(user.getColumnIndex("User"));
        }
        while (ipAddres.moveToNext()) {
            result[1] = ipAddres.getString(ipAddres.getColumnIndex("IP"));
        }
        while (pass.moveToNext()) {
            result[2] = pass.getString(pass.getColumnIndex("Password"));
        }
        for (int i = 0; i < result.length; i++) {
            System.out.println(result[i]);
        }

      return result;
    }

    public void setText(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                text.setText(message);
            }
        });


    }
    public void setInputText(final String input) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                cmdInput.setText(input);
            }
        });
    }
    public void setVisible(final Button b, final int v) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                b.setVisibility(v);
            }
        });

    }
    public void setToast(final String msg) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ButtonClickedActivity.this,msg, Toast.LENGTH_SHORT ).show();
            }
        });

    }
//    public void updateList(ArrayList<String> a) {
//        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, a);
//        Handler handler = new Handler(Looper.getMainLooper());
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });

    public String[] listToArray(ArrayList<String> a) {
        String [] current = new String[a.size()];
        for(int i = 0; i < current.length; i++) {
            current[i] = a.get(i);
        }
        return current;
        }
    public void lastCmdBox(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ButtonClickedActivity.this);
        builder.setTitle("Pick a command to Run again");
        final String [] list = listToArray(lastCommands);
        builder.setSingleChoiceItems(list, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cmdInput.setText(list[which]);
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dlg = builder.create();
        dlg.show();

    }
    public void goHome(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

