package com.example.pets.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class PetContract  {

    public static final String CONTENT_AUTHORITY  = "com.example.pets";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String Pets_path = "pets"; //value of the pets_path should be table name
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, Pets_path);

    public static final String CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + Pets_path;


    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + Pets_path;


    public PetContract() {
    }

    public static final class PetEntry implements BaseColumns{

        public static final String TABLE_NAME = "pets";
        public static final String _ID= BaseColumns._ID;
        public static final String PET_NAME = "name";
        public static final String GENDER = "gender";
        public static final String BREED = "breed";
        public static final String WEIGHT = "weight";

        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
        public static final int GENDER_UNKNOWN = 0;
    }


}
