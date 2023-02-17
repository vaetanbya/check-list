package com.example.checklistapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class listAdapter extends RecyclerView.Adapter<listAdapter.viewHolder> {
    private final ArrayList<goodExample> products_list;

    public  static class viewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public TextView countView;
        public Button delete;
        public Button change;

        public viewHolder(View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.productName);
            countView = itemView.findViewById(R.id.count);
            delete = itemView.findViewById(R.id.delete);
            change = itemView.findViewById(R.id.refactor);
        }
    }

    public listAdapter(ArrayList<goodExample> goods){
        products_list = goods;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_list, parent, false);
        viewHolder vh = new viewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        goodExample item = products_list.get(i);

        viewHolder.countView.setText(item.getCount());
        viewHolder.textView.setText(item.getName());
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //removing
                //update
            }
        });
        viewHolder.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent

            }
        });
    }

    @Override
    public int getItemCount() {
        return products_list.size();
    }
}
