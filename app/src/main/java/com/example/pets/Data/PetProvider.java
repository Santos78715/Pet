package com.example.pets.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PetProvider extends ContentProvider {
    PetDbHelper DbHelper;
    SQLiteDatabase sqLiteDatabase;


    private static final int PETS = 100;
    private static final int PET_ID = 101;
    private static final UriMatcher urimatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        urimatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.Pets_path, PETS);
        urimatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.Pets_path + "/#", PET_ID);
    }

    @Override
    public boolean onCreate() {
        DbHelper = new PetDbHelper(getContext());
        sqLiteDatabase = DbHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionargs, @Nullable String sortorder) {

        SQLiteDatabase sqLiteDatabase = DbHelper.getReadableDatabase();
        Cursor cursor;
        int match = urimatcher.match(uri);
        switch (match) {
            case PETS:
                cursor = sqLiteDatabase.query(PetContract.PetEntry.TABLE_NAME, projection, selection, selectionargs,
                        null, null, sortorder);
                break;
            case PET_ID:
                selection = PetContract.PetEntry._ID + "=?";
                selectionargs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = sqLiteDatabase.query(PetContract.PetEntry.TABLE_NAME, projection, selection, selectionargs,
                        null, null, sortorder);
                break;
            default:
                throw new IllegalArgumentException("Can't query the database");
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = urimatcher.match(uri);
        switch (match) {
            case PETS:
                return PetContract.CONTENT_LIST_TYPE;
            case PET_ID:
                return PetContract.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = urimatcher.match(uri);
        switch (match) {
            case PETS:
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertPet(Uri uri, ContentValues values) {
        // Get writeable database
        SQLiteDatabase database = DbHelper.getWritableDatabase();

        // Insert the new pet with the given values

        long id = database.insert(PetContract.PetEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e("PetProvider", "Failed to insert row for " + uri);
            return null;
        }
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = DbHelper.getWritableDatabase();

        final int match = urimatcher.match(uri);
        switch (match) {
            case PETS:
                // Delete all rows that match the selection and selection args
                return database.delete(PetContract.PetEntry.TABLE_NAME, selection, selectionArgs);
            case PET_ID:
                // Delete a single row given by the ID in the URI
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return database.delete(PetContract.PetEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionargs) {
        final int match = urimatcher.match(uri);
        switch (match) {
            case PETS:
                return updatepet(uri, contentValues, selection, selectionargs);

            case PET_ID:
                selection = PetContract.PetEntry._ID + "=?";
                selectionargs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatepet(uri, contentValues, selection, selectionargs);

            default:
                throw new IllegalArgumentException("Invalid updation");

        }
    }

    private int updatepet(Uri uri, ContentValues contentValues, String selection, String[] selectionargs) {
        if (contentValues.containsKey(PetContract.PetEntry.PET_NAME)) {
            String name = contentValues.getAsString(PetContract.PetEntry.PET_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }


        if (contentValues.size() == 0) {
            return 0;
        }

        SQLiteDatabase sqLiteDatabase = DbHelper.getWritableDatabase();
        int rowupdated = sqLiteDatabase.update(PetContract.PetEntry.TABLE_NAME, contentValues, null, null);
        if (rowupdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowupdated;
    }
}

