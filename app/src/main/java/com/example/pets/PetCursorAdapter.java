package com.example.pets;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;

import com.example.pets.Data.PetContract;

public class PetCursorAdapter extends CursorAdapter {


 public PetCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView) view.findViewById(R.id.textview1);
        TextView textView1 = (TextView) view.findViewById(R.id.textview2);

        int nameColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.PET_NAME);
        int breedColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.BREED);

        String name = cursor.getString(nameColumnIndex);
        String breed = cursor.getString(breedColumnIndex);

        textView.setText(name);
        textView1.setText(breed);
    }

}
