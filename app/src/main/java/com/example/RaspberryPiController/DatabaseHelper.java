package com.example.RaspberryPiController;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import static java.sql.Types.INTEGER;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String name = "PICollection";
    private static final String COL_one = "_id";
    private static final String COL_two = "Name";
    private static final String COL_three = "User";
    private static final String COL_four = "IP";
    private static final String COL_five = "Password";


    public DatabaseHelper(@Nullable Context context) {
        super(context, name, null, 2);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String create_table = "CREATE TABLE " + name
                + " ("+ COL_one +" integer primary key autoincrement, "
                + COL_two + " text, "
                + COL_three + " text, "
                + COL_four + " text,"
                + COL_five + " text" + ")";
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + name) ;
        onCreate(db);
    }
    public boolean addData(String n, String u, String a, String p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_two, n);
        cv.put(COL_three, u);
        cv.put(COL_four, a);
        cv.put(COL_five, p);
        Log.d(TAG, "addData: Adding " + n + " to " + name);
        long results = db.insert(name, null, cv);
        if (results == -1) {
            return false;
        } else {
            return true;
        }

    }
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + name, null );
        return res;
    }
    public Cursor getItemID(String n) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT _id FROM " + name + " WHERE " + COL_two + " = '" + n + "'" ;
        Cursor res = db.rawQuery(query, null);
        return res;
    }
    public Cursor getAddress(String n) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL_four + " FROM " + name + " WHERE " + COL_two + " = '" + n + "'" ;
        Cursor res = db.rawQuery(query, null);
        return res;
    }
    public Cursor getPass(String n) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL_five + " FROM " + name + " WHERE " + COL_two + " = '" + n + "'" ;
        Cursor res = db.rawQuery(query, null);
        return res;
    }
    public Cursor getUser(String n) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL_three + " FROM " + name + " WHERE " + COL_two + " = '" + n + "'" ;
        Cursor res = db.rawQuery(query, null);
        return res;
    }
    public Cursor getRowData(String n) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] col = new String[]{COL_one, COL_three, COL_four, COL_five};
        String selection = COL_two + " =?";
        String[] selectionArgs = new String[]{n};
        Cursor data = db.query(name, col, selection, selectionArgs,  null, null, null );
        return data;
    }
    public void delete(int id, String n) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + name + " WHERE " + COL_one + " = '" + id + "'" + " AND " + COL_two + " = '"  + n + "'";
        db.execSQL(query);
    }
    public void updateName(int id, String newName, String oldName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + name +
                " SET " + COL_two + " = '" + newName + "' WHERE " + COL_one + " = '" +
                id + "'" + " AND " + COL_two + " = '" + oldName + "'";
        db.execSQL(query);
    }
    public void updateIP(int id, String oldID, String newID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + name +
                " SET " + COL_four + " = '" + newID + "' WHERE " + COL_one + " = '" +
                id + "'" + " AND " + COL_four + " = '" + oldID + "'";
        db.execSQL(query);
    }
    public void updateUser(int id, String oldUser, String newUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + name +
                " SET " + COL_three + " = '" + newUser + "' WHERE " + COL_one + " = '" +
                id + "'" + " AND " + COL_three + " = '" + oldUser + "'";
        db.execSQL(query);
    }
    public void updatePass(int id, String oldPass, String newPass) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + name +
                " SET " + COL_five + " = '" + newPass + "' WHERE " + COL_one + " = '" +
                id + "'" + " AND " + COL_five + " = '" + oldPass + "'";
        db.execSQL(query);
    }
    public boolean updateData(int id, String n, String user, String ip, String pass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_two, n);
        cv.put(COL_three, user);
        cv.put(COL_four, ip);
        cv.put(COL_five, pass);
        db.update(name, cv, "_id = ?", new String[]{Integer.toString(id)} );
        return true;

    }
    //public
}
