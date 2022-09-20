package com.example.pets;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.ActionBar;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pets.Data.PetContract;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_PET_LOADER = 1;

    private EditText mNameEditText;


    private EditText mBreedEditText;


    private EditText mWeightEditText;


    private Spinner mGenderSpinner;

   private  Uri currentpeturi;


    

    private int mGender = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        Intent intent = getIntent();
        currentpeturi = intent.getData();

        if(currentpeturi != null )
        {
            getLoaderManager().initLoader(1, null, this);
        }

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_pet_name);
        mBreedEditText = (EditText) findViewById(R.id.edit_pet_breed);
        mWeightEditText = (EditText) findViewById(R.id.edit_pet_weight);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);

        setupSpinner();
    }


    private void setupSpinner() {

        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
      mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              String selection = (String) adapterView.getItemAtPosition(i);
              if (!TextUtils.isEmpty(selection)) {
                  if (selection.equals("Male")) {
                      mGender = 1; // Male
                  } else if (selection.equals("Female")) {
                      mGender = 2; // Female
                  } else {
                      mGender = 0; // Unknown
                  }
              }
          }

          @Override
          public void onNothingSelected(AdapterView<?> adapterView) {
              mGender = 0;
          }
      });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }


    public void insertdummydata() {


        ContentValues contentValues = new ContentValues();
        contentValues.put(PetContract.PetEntry.PET_NAME, mNameEditText.getText().toString());
        contentValues.put(PetContract.PetEntry.WEIGHT, Integer.parseInt(mWeightEditText.getText().toString()));
        contentValues.put(PetContract.PetEntry.BREED, mBreedEditText.getText().toString());
        contentValues.put(PetContract.PetEntry.GENDER, mGender);


        if (currentpeturi == null) {
            Uri insert = getContentResolver().insert(PetContract.CONTENT_URI, contentValues);
        } else {
            int rowaffected = getContentResolver().update(currentpeturi, contentValues, null, null);

            if (rowaffected > 1) {
                Toast.makeText(this, "More rows updated", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void deletepet(){
       int deleted =  getContentResolver().delete(currentpeturi,null, null);
       finish();
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                insertdummydata();
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                deletepet();
                return true;
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                PetContract.PetEntry._ID,
                PetContract.PetEntry.PET_NAME,
                PetContract.PetEntry.BREED,
                PetContract.PetEntry.GENDER,
                PetContract.PetEntry.WEIGHT};

        return new CursorLoader(this, currentpeturi, projection, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor == null || cursor.getCount() <1){
            Toast.makeText(this, "Empty cursor", Toast.LENGTH_SHORT).show();
        }

        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.PET_NAME);
            int breedColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.BREED);
            int genderColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.GENDER);
            int weightColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.WEIGHT);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            String breed = cursor.getString(breedColumnIndex);
            int gender = cursor.getInt(genderColumnIndex);
            int weight = cursor.getInt(weightColumnIndex);

            mNameEditText.setText(name);
            mBreedEditText.setText(breed);
            mWeightEditText.setText(Integer.toString(weight));

            switch (gender) {
                case PetContract.PetEntry.GENDER_MALE:
                    mGenderSpinner.setSelection(1);
                    break;
                case PetContract.PetEntry.GENDER_FEMALE:
                    mGenderSpinner.setSelection(2);
                    break;
                default:
                    mGenderSpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mBreedEditText.setText("");
        mNameEditText.setText("");
        mWeightEditText.setText("");
        mGenderSpinner.setSelection(0);
    }

}