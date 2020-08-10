package com.example.RaspberryPiController;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class EditPi extends AppCompatActivity {
    EditText currentPi;
    EditText ip;
    EditText user;
    EditText password;
    Button delete;
    Button edit;
    ImageButton home;
    DatabaseHelper db;
    private String selectedName;
    private String selectedIP;
    private String selectedUser;
    private String selectedPass;
    private int selectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pi);
        home = (ImageButton) findViewById(R.id.hme);
        db = new DatabaseHelper(this);
        edit = (Button) findViewById(R.id.edit);
        currentPi = (EditText) findViewById(R.id.currentPi);
        ip = (EditText) findViewById(R.id.ip);
        user = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.password);
        delete = (Button) findViewById(R.id.delete);
        selectedId = getIntent().getIntExtra("id", -1);
        selectedName = getIntent().getStringExtra("name");
        selectedIP = getIntent().getStringExtra("ipAddress");
        selectedUser = getIntent().getStringExtra("user");
        selectedPass = getIntent().getStringExtra("pass");
        currentPi.setText(selectedName);
        ip.setText(selectedIP);
        user.setText(selectedUser);
        password.setText(selectedPass);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), PiListReal.class);
                db.delete(selectedId, selectedName);
                startActivity(intent);


            }
        });
    }

    public void goHome(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void update(View v) {
        Intent intent = new Intent(this, PiListReal.class);
        if (currentPi.getText().equals(null) || ip.getText().equals(null) || user.getText().equals(null) || password.getText().equals(null)) {
            toastMessage("Please Fill out all fields");
        }

       db.updateData(selectedId, currentPi.getText().toString(),
               user.getText().toString(), ip.getText().toString(),
               password.getText().toString());
        startActivity(intent);
    }

    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT);
    }
}
