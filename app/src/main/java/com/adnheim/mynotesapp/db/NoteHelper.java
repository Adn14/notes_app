package com.adnheim.mynotesapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.adnheim.mynotesapp.DataBaseHelper;
import com.adnheim.mynotesapp.DatabaseContract;
import com.adnheim.mynotesapp.entity.Note;

import java.sql.SQLData;
import java.sql.SQLException;
import java.util.ArrayList;

public class NoteHelper {

    private static String DATABASE_TABLE = DatabaseContract.TABLE_NOTE;
    private Context context;
    private DataBaseHelper dataBaseHelper;

    private SQLiteDatabase database;

    public NoteHelper(Context context){
        this.context = context;
    }

    public NoteHelper open() throws SQLException{
        dataBaseHelper = new DataBaseHelper(context);
        database = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dataBaseHelper.close();
    }

    public ArrayList<Note> query(){
        ArrayList<Note> arrayList = new ArrayList<Note>();
        Cursor cursor = database.query(DATABASE_TABLE, null, null, null, null, null, DatabaseContract.NoteColumns._ID + " DESC", null);
        cursor.moveToFirst();
        Note note;
        if (cursor.getCount() > 0){
            do {
                note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns._ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.TITLE)));
                note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.DESCRIPTION)));
                note.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.DATE)));

                arrayList.add(note);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(Note note){
        ContentValues initialValues = new ContentValues();
        initialValues.put(DatabaseContract.NoteColumns.TITLE, note.getTitle());
        initialValues.put(DatabaseContract.NoteColumns.DESCRIPTION, note.getDescription());
        initialValues.put(DatabaseContract.NoteColumns.DATE, note.getDate());
        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    public int update(Note note){
        ContentValues args = new ContentValues();
        args.put(DatabaseContract.NoteColumns.TITLE, note.getTitle());
        args.put(DatabaseContract.NoteColumns.DESCRIPTION, note.getDescription());
        args.put(DatabaseContract.NoteColumns.DATE, note.getDate());
        return database.update(DATABASE_TABLE, args, DatabaseContract.NoteColumns._ID +  "= '" + note.getId() + "'", null);
    }

    public int delete(int id ){
        return database.delete(DatabaseContract.TABLE_NOTE, DatabaseContract.NoteColumns._ID + " = '"+id+"'", null);
    }
}
