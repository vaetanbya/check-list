package com.example.checklistapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper  extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "listDB";
    public static final String TABLE_NAME = "shop_list";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_COUNT = "count";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(" + KEY_ID
                + " integer primary key," + KEY_NAME + " text," + KEY_COUNT + " integer" + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);

        onCreate(db);

    }

    public void addProduct(SQLiteDatabase db,String name, int count){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_NAME, name);
        contentValues.put(DBHelper.KEY_COUNT, count);

        db.insert(DBHelper.TABLE_NAME, null, contentValues);
    }

    public void removeProduct(SQLiteDatabase db,String name){
        db.delete(DATABASE_NAME, KEY_NAME + "=?", new String[]{name});
    }

    public void removeProduct(SQLiteDatabase db,int id){
        db.delete(DATABASE_NAME, KEY_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void changeRow(SQLiteDatabase db,int id, String product_name,int product_count){
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME,product_name); //These Fields should be your String values of actual column names
        cv.put(KEY_COUNT,product_count);
        db.update(TABLE_NAME, cv, "_id = ?", new String[]{String.valueOf(id)});
    }

    public void changeRow(SQLiteDatabase db,String product_name,int product_count){
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME,product_name); //These Fields should be your String values of actual column names
        cv.put(KEY_COUNT,product_count);
        db.update(TABLE_NAME, cv, "name = ?", new String[]{String.valueOf(product_name)});
    }

    public boolean exist(SQLiteDatabase db,String name){
        String Query = "Select * from " + TABLE_NAME + " where " + KEY_NAME + " = " + name;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public ArrayList<goodExample> getAllRows(SQLiteDatabase db){
        ArrayList<goodExample> list = new ArrayList<goodExample>();
        Cursor  cursor = db.rawQuery("select * from "+TABLE_NAME,null);;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                @SuppressLint("Range") int count = cursor.getInt(cursor.getColumnIndex(KEY_COUNT));

                list.add(new goodExample(id, name, count));

                cursor.moveToNext();
            }
        }
        return list;
    }
}
