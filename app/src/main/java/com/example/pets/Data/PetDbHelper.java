package com.example.pets.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.security.PublicKey;

public class PetDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME  = "shelter.db";
    public static final int DATABASE_VERSION = 1;


    public PetDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + PetContract.PetEntry.TABLE_NAME + " ("
                + PetContract.PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PetContract.PetEntry.PET_NAME + " TEXT NOT NULL, "
                + PetContract.PetEntry.BREED + " TEXT, "
                + PetContract.PetEntry.GENDER + " INTEGER NOT NULL, "
                + PetContract.PetEntry.WEIGHT + " INTEGER NOT NULL DEFAULT 0);";

        sqLiteDatabase.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
