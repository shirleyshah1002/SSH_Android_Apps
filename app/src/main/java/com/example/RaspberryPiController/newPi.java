package com.example.RaspberryPiController;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import static com.example.RaspberryPiController.Pi.getTracker;

import java.util.regex.*;

public class newPi extends AppCompatActivity {
    EditText address;
    EditText name;
    EditText password;
    EditText user;
    Button submit;
    Button piList;
    DatabaseHelper db;
    ImageButton hme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pi);
        hme = (ImageButton) findViewById(R.id.home);
        piList = (Button) findViewById(R.id.piList);
        db = new DatabaseHelper(this);
        piList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), PiListReal.class);
                startActivity(intent);
            }
        });
        address = (EditText) findViewById(R.id.pi_IP);
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.piPass);
        user = (EditText) findViewById(R.id.user);
        submit = (Button) findViewById(R.id.add_pi);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = name.getText().toString();
                String addString = address.getText().toString();
                String passString = password.getText().toString();
                String userString = user.getText().toString();
                boolean check = checker(nameString, addString, passString, userString);
                if (!check && checkInList(addString)) {
                    Toast.makeText(newPi.this, "Please Enter the field in the correct format or you already have this pi", Toast.LENGTH_SHORT).show();
                    address.setText("");
                    name.setText("");
                    password.setText("");


                } else {

                    boolean flag = db.addData(nameString, userString, addString, passString);
                    if (!flag) {
                        Toast.makeText(newPi.this, "Data has not been inserted", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(newPi.this, "Pi inserted successfully", Toast.LENGTH_SHORT).show();
                        new Pi(nameString, addString, passString);
                        address.setText("");
                        name.setText("");
                        user.setText("");
                        password.setText("");
                    }

                    RadioGroup temp = findViewById(R.id.piGroup);
                }


            }
        });
    }
    public void listAll() {
        piList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = db.getAllData();
                if (res.getCount() == 0) {
                    //show message
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()) {
                    buffer.append("Name" + res.getString(1) +"\n\n");
                }

            }
        });
    }
    private boolean checker(String n, String a, String p, String u) {
        if (n == null || a == null || u == null) {
            return false;
        }
        return checkIp(a);

    }
    private boolean checkInList(String ip) {
        for (String key: getTracker().keySet()) {
            Pi temp = getTracker().get(key);
            String current = temp.getIp();
            if (current.equals(ip)) {
                return true;
            }
        }
        return false;

    }
    private boolean checkIp(String ip) {
        String zeroTo255 = "(\\d{1,2}|(0|1)\\" + "d{2}|2[0-4]\\d|25[0-5])";
        String regex
                = zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255;

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(ip);
        System.out.println(m.matches());
        return m.matches();

    }
    public void goHome(View v) {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

}
