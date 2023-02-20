package com.example.checklist;

import androidx.appcompat.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class change_page extends AppCompatActivity {
    private final String[] availabel_names = {
            "biscuits", "broccoli", "cheese", "coffee","curd",
            "dough","milk", "pancakes","sourcream","tea"
    };
    DBHelper dbHelper;
    int id;
    EditText name_bar, count_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_page);
        //get id
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        //set Views
        name_bar = findViewById(R.id.editProductName);
        count_bar = findViewById(R.id.editProductCount);
        //set data base
        dbHelper = new DBHelper(this);
    }

    public void save(View v){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //get data
        String product_name = String.valueOf(name_bar.getText()).toLowerCase();
        int product_count = 0;
        try {
            product_count = Integer.parseInt(String.valueOf(count_bar.getText()));
        }catch (Exception e){
            clear();
            return;
        }

        if(not_available(product_name)){
            clear();
            return;
        }

        if(product_count<=0){
            clear();
            dbHelper.removeProduct(db, id);
            go_back();
        }
        //change data
        dbHelper.removeProduct(db, id);
        if(dbHelper.exist(db,product_name)) {
            dbHelper.changeRow(db, product_name, product_count);
        }else{
            dbHelper.addProduct(db,product_name,product_count);
        }
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

    public void remove(View v){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //clear
        clear();
        //removing
        dbHelper.removeProduct(db, id);
        //intent
        go_back();
    }

    private void go_back(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void clear(){
        name_bar.setText("");count_bar.setText("1");
    }

    private boolean not_available(String name){
        for (String availabel_name:availabel_names) {
            if(name.equals(availabel_name))
                return false;
        }
        return true;
    }
}