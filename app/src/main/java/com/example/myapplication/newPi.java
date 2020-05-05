package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.*;

public class newPi extends AppCompatActivity {
    EditText address;
    EditText name;
    EditText password;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pi);
        address = (EditText) findViewById(R.id.pi_IP);
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.piPass);
        submit = (Button) findViewById(R.id.add_pi);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = name.getText().toString();
                String addString = address.getText().toString();
                String passString = password.getText().toString();
                boolean check = checker(nameString, addString, passString);
                if (!check) {
                    Toast.makeText(newPi.this, "Please Enter the field in the correct format", Toast.LENGTH_SHORT).show();
                    address.setText("");
                    name.setText("");
                    password.setText("");

                } else {
                    new Pi(nameString, addString, passString);
                }


            }
        });
    }
    private boolean checker(String n, String a, String p) {
        if (n == null || a == null) {
            return false;
        }
        return checkIp(a);

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

}
