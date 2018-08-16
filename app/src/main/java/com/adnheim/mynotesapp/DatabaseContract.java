package com.adnheim.mynotesapp;

import android.provider.BaseColumns;

public class DatabaseContract {

    public static String TABLE_NOTE = "note";

    public static final class  NoteColumns implements BaseColumns {

        //note title
        public static String TITLE = "title";
        //note description
        public static String DESCRIPTION = "description";
        //note date
        public static String DATE = "date";
    }
}
