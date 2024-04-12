package com.example.diplom;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user_db";
    private static final int DATABASE_VERSION = 1;

    // Creating table query
    private static final String TABLE_CREATE = "create table " +
            User.TABLE + "(" +
            User.COLUMN_ID + " integer primary key autoincrement, " +
            User.COLUMN_NAME + " text not null, " +
            User.COLUMN_SURNAME + " text not null, " +
            User.COLUMN_EMAIL + " text not null, " +
            User.COLUMN_PASSWORD + " text not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE);
        onCreate(db);
    }

    public long insertUser(String name, String surname, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(User.COLUMN_NAME, name);
        values.put(User.COLUMN_SURNAME, surname);
        values.put(User.COLUMN_EMAIL, email);
        values.put(User.COLUMN_PASSWORD, password);

        long id = db.insert(User.TABLE, null, values);
        db.close();
        return id;
    }
    public Cursor searchUserByEmailAndPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM User WHERE COLUMN_EMAIL = ? AND COLUMN_PASSWORD = ?", new String[]{email, password});
    }


    public class User {
        public static final String TABLE = "users";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SURNAME = "surname";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
    }

}

