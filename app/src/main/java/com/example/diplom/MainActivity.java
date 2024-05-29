package com.example.diplom;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.EditText;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    EditText editTextName;
    EditText editTextSurname;
    EditText editTextEmail;
    EditText editTextPassword;
    long userId=0;
    boolean isAllFieldsChecked = false;

    Button buttonRegister;
    Button buttonlogin;
    Button buttonzaRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editTextName = findViewById(R.id.PersonName);
        editTextSurname = findViewById(R.id.PersonFamily);
        editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        buttonRegister = findViewById(R.id.register);
        buttonlogin = findViewById(R.id.loginr);
        buttonzaRegister = findViewById(R.id.zaregister);
        //делаем поля не видимыми
        editTextSurname.setVisibility(View.INVISIBLE);
        editTextName.setVisibility(View.INVISIBLE);
        buttonzaRegister.setVisibility(View.INVISIBLE);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        // создаем базу данных
        databaseHelper.create_db();
        db = databaseHelper.open();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Проверяем текущее состояние видимости EditText
                if (editTextSurname.getVisibility() == View.INVISIBLE && editTextName.getVisibility() == View.INVISIBLE) {
                    // Если EditText невидим, делаем его видимым
                    editTextSurname.setVisibility(View.VISIBLE);
                    editTextName.setVisibility(View.VISIBLE);
                    buttonzaRegister.setVisibility(View.VISIBLE);
                    buttonlogin.setVisibility(View.INVISIBLE);
                    buttonRegister.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(MainActivity.this, "Ошибка, возможно некоторые поля незаполнены", Toast.LENGTH_SHORT).show();
                    //сюда можно вставить код авторизации
                }
            }
        });

        buttonzaRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isAllFieldsChecked = CheckAllFields();

                if (isAllFieldsChecked){
                    ContentValues cv = new ContentValues();
                    cv.put(DatabaseHelper.COLUMN_NAME, editTextName.getText().toString());
                    cv.put(DatabaseHelper.COLUMN_SURNAME, editTextSurname.getText().toString());
                    cv.put(DatabaseHelper.COLUMN_E_MAIL, editTextEmail.getText().toString());
                    cv.put(DatabaseHelper.COLUMN_PASSWORD, editTextPassword.getText().toString());

                    editTextSurname.setVisibility(View.INVISIBLE);
                    editTextName.setVisibility(View.INVISIBLE);
                    buttonzaRegister.setVisibility(View.INVISIBLE);
                    buttonlogin.setVisibility(View.VISIBLE);
                    buttonRegister.setVisibility(View.VISIBLE);
                }

            }
        });

        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                userCursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE +
                        " WHERE " + DatabaseHelper.COLUMN_E_MAIL + " = ? AND " + DatabaseHelper.COLUMN_PASSWORD + " = ?", new String[]{email, password});

                userCursor.moveToFirst();
                userCursor.close();
                if (userCursor.moveToFirst()) {
                    // Пользователь найден
                    Intent intent = new Intent(MainActivity.this, LoginIn.class);
                    startActivity(intent);
                } else {
                    // Пользователь не найден
                    Toast.makeText(MainActivity.this, "Пользователь не найден, проверте коректность введенных данных или зарегистрируйтесь", Toast.LENGTH_SHORT).show();
                }
                userCursor.close();

            }
        });

    }
    private boolean CheckAllFields() {
        if (editTextName.length() == 0) {
            editTextName.setError("Поле не заполнено");
            return false;
        }

        if (editTextSurname.length() == 0) {
            editTextSurname.setError("Поле не заполнено");
            return false;
        }

        if (editTextEmail.length() == 0) {
            editTextEmail.setError("Поле не заполнено");
            return false;
        }

        if (editTextPassword.length() == 0) {
            editTextPassword.setError("Поле не заполнено");
            return false;
        } else if (editTextPassword.length() < 8) {
            editTextPassword.setError("В пароле должно быть минимум 8 символов");
            return false;
        }

        // after all validation return true.
        return true;
    }

}