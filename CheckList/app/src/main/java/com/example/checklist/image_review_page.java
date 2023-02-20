package com.example.checklist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.checklist.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.metadata.schema.ImageSize;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class image_review_page extends AppCompatActivity {
    private final String[] availabel_names = {
            "biscuits", "broccoli", "cheese", "coffee","curd", "dough","milk", "pancakes","sourcream","tea"
    };
    private final int imageSize = 224;
    DBHelper dbHelper;
    Bitmap img;
    ImageView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_review_page);
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
        String prediction;
        Bitmap image;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //predicting
        image = (Bitmap) getIntent().getParcelableExtra("preview");
        int dimension = Math.min(image.getWidth(),image.getHeight());
        image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
        prediction = predict(image);
        //removing
        dbHelper.removeProduct(db,prediction);
        //intent
        go_back();
    }

    public void cancel(View v){
        //intent
        go_back();
    }

    private void go_back(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private String predict(Bitmap image){
        Bitmap img = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4*imageSize*imageSize*3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize*imageSize];
            img.getPixels(intValues, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
            int pixel = 0;

            for(int i = 0;i<imageSize;i++){
                for(int j = 0;j<imageSize;j++){
                    int val = intValues[pixel++];
                    byteBuffer.putFloat(((val>>16) & 0xFF)*(1.f/1));
                    byteBuffer.putFloat(((val>>8) & 0xFF)*(1.f/1));
                    byteBuffer.putFloat((val & 0xFF)*(1.f/1));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidence = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConf = 0;
            for(int i = 0;i<confidence.length;i++){
                if(confidence[i]>maxConf){
                    maxConf = confidence[i];
                    maxPos = i;
                }
            }

            // Releases model resources if no longer used.
            model.close();

            return  availabel_names[maxPos];
        } catch (IOException e) {
            // TODO Handle the exception
        }
        return "";
    }
}