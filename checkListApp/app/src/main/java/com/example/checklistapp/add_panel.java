package com.example.checklistapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class add_panel extends AppCompatActivity {
    DBHelper dbHelper;
    private final String[] availabel_names = {"milk","apple","banana"};
    EditText name_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_panel);
        //set name_bar
        name_bar = findViewById(R.id.editProductName);
        //set data base
        dbHelper = new DBHelper(this);
    }

    public void save(View v){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //get data
        String product_name = String.valueOf(name_bar.getText()).toLowerCase();
        int product_count = 1;

        if(not_available(product_name) && product_count>0){
            clear();
            return;
        }
        //add product
        dbHelper.addProduct(db,product_name,product_count);
        //clear
        clear();
        //intent
        go_back();
    }

    public void cancel(View v){
        //clear
        clear();
        //intent
        go_back();
    }

    private void go_back(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void clear(){
        name_bar.setText("");
    }

    private boolean not_available(String name){
        for (String availabel_name:availabel_names) {
            if(name.equals(availabel_name))
                return false;
        }
        return true;
    }
}