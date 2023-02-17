package com.example.checklistapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
        db.delete(TABLE_NAME, KEY_NAME + "=?", new String[]{name});
    }

    public void removeProduct(SQLiteDatabase db,int id){
        db.delete(TABLE_NAME, KEY_ID + "=?", new String[]{String.valueOf(id)});
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
        @SuppressLint("Recycle") Cursor  cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + KEY_NAME + " like \"" + name + "\"",null);
        return cursor.getCount()>0;
    }

    public void deleteAll(SQLiteDatabase db,String name){
        @SuppressLint("Recycle") Cursor  cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + KEY_NAME + " like \"" + name + "\"",null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                removeProduct(db,id);
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    public ArrayList<goodExample> getAllRows(SQLiteDatabase db){
        ArrayList<goodExample> list = new ArrayList<>();
        @SuppressLint("Recycle") Cursor  cursor = db.rawQuery("select * from "+TABLE_NAME,null);
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
