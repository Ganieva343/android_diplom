package ru.ganieva343.diplom;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_PATH;
    private static final String DB_NAME = "diplom.db";
    private static final int SCHEMA = 1;
    static final String TABLE = "users";
    // Название столбцов
    static final String COLUMN_ID = "_id";
    static final String COLUMN_SURNAME = "surname";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_E_MAIL = "email";
    static final String COLUMN_PASSWORD = "password";

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_SURNAME
                + " TEXT, " + COLUMN_NAME + " TEXT, " + COLUMN_E_MAIL + " TEXT, " + COLUMN_PASSWORD + " TEXT);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users;");
    }


}

