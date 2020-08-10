package com.example.RaspberryPiController;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PiListReal extends AppCompatActivity {
    private ListView lw;
    DatabaseHelper db;
    private static final String TAG = "ListDataActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pi_list_real);
        lw = (ListView) findViewById(R.id.piList);
        db = new DatabaseHelper(this);
        getlist();

    }
    private void getlist() {
        Cursor temp = db.getAllData();
        ArrayList<String> listData = new ArrayList<String>();
        while(temp.moveToNext()) {
            listData.add(temp.getString(1));

        }
        ListAdapter adp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        lw.setAdapter(adp);
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), EditPi.class);
                String name = parent.getItemAtPosition(position).toString();
                Cursor data = db.getAllData();
                Cursor ID = db.getItemID(name);
               // Cursor dataTry = db.getRowData(name);
                int number = -1;
                String add = " ";
                String password = " ";
                String user = " ";
                while (data.moveToNext()) {
                    number = data.getInt(0);
                    user = data.getString(2);
                    add = data.getString(3);
                    password = data.getString(4);

                }
                intent.putExtra("id", number);
                intent.putExtra("name", name);
                intent.putExtra("user", user );
                intent.putExtra("ipAddress", add);
                intent.putExtra("pass", password);
                System.out.println(number);
                System.out.println(add);
                System.out.println(password);
                startActivity(intent);



            }
        });

    }


}
