package com.example.checklistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class image_review_page extends AppCompatActivity {
    private final String[] availabel_names = {"milk","apple","banana"};
    DBHelper dbHelper;
    Interpreter tflite;
    Bitmap img;
    ImageView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_review_page);
        //load model
        try {
            tflite = new Interpreter(loadModelFile());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        //get image
        Intent intent = getIntent();
        img = (Bitmap) intent.getParcelableExtra("preview");
        //set image
        preview = findViewById(R.id.preview);
        preview.setImageBitmap(img);
        //set data base
        dbHelper = new DBHelper(this);
    }

    public void recognize(View v){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int prediction;
        //predicting
        prediction = predict();
        //removing
        if(prediction>0){dbHelper.removeProduct(db,availabel_names[prediction-1]);}
        System.out.println(doInference("1"));
        //intent
        go_back();
    }

    public void cancel(View v){
        //intent
        go_back();
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor=this.getAssets().openFd("degree.tflite");
        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startOffset=fileDescriptor.getStartOffset();
        long declareLength=fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declareLength);
    }

    private float doInference(String inputString) {
        float[] inputVal=new float[1];
        inputVal[0]=Float.parseFloat(inputString);
        float[][] output=new float[1][1];
        tflite.run(inputVal,output);
        return output[0][0];
    }

    private void go_back(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private int predict(){return 1;}
}