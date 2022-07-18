package com.example.carpark_records;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBhelper extends SQLiteOpenHelper  {
    public DBhelper(Context context) {
        super(context, "Records.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Parkrecords(vehicle TEXT primary key, plate TEXT, date INTERGER, time INTERGER, amount INTERGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("Drop table if exists Parkrecords");

    }
    public Boolean insertdata (String vehicle, String plate, String date, String time, String amount){

        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("vehicle", vehicle);
        contentValues.put("plate", plate);
        contentValues.put("date", date);
        contentValues.put("time", time);
        contentValues.put("amount", amount);

        long result= db.insert("Parkrecords",null,contentValues);
        if (result ==-1){
            return false;
        }else {
            return  true;
        }

    }

    public Boolean deletedata (String vehicle){

        SQLiteDatabase db =this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Parkrecords where name= ?", new String[]{vehicle});
        if (cursor.getCount()>0) {
            long result = db.delete("Parkrecords", "vehicle=?", new String[]{vehicle});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else {
            return false;
        }

    }


    public Cursor getdata (){

        SQLiteDatabase db =this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Parkrecords", null);
        return cursor;

    }

}
