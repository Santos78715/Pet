package com.example.pets;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TintableCheckedTextView;

import com.example.pets.Data.PetContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    public static final int PET_LOADER = 10;
    PetCursorAdapter petCursorAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, EditorActivity.class);
            startActivity(intent);
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
          // Define a projection that specifies which columns from the database
          // you will actually use after this query.
          String[] projection = {
                  PetContract.PetEntry._ID,
                  PetContract.PetEntry.PET_NAME,
                  PetContract.PetEntry.BREED,
                  PetContract.PetEntry.GENDER,
                  PetContract.PetEntry.WEIGHT
          };
          // Perform a query on the provider using the ContentResolver.

          Cursor cursor = getContentResolver().query(
                  PetContract.CONTENT_URI,   // The content URI of the words table
                  projection,             // The columns to return for each row
                  null,                   // Selection criteria
                  null,                   // Selection criteria
                  null);

        ListView listView = (ListView) findViewById(R.id.listview);
        View empty = findViewById(R.id.empty_view);
        listView.setEmptyView(empty);
        PetCursorAdapter petCursorAdapter = new PetCursorAdapter(this, cursor);
        listView.setAdapter(petCursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent  = new Intent(MainActivity.this, EditorActivity.class);

                Uri currentpeturi = ContentUris.withAppendedId(PetContract.CONTENT_URI, l);
                 intent.setData(currentpeturi);
                startActivity(intent);
            }
        });


    }
}





