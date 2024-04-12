package com.example.diplom;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText editTextName, editTextSurname, editTextEmail, editTextPassword;
    boolean isAllFieldsChecked = false;

    private Button buttonRegister, buttonlogin, buttonzaRegister;

    @SuppressLint("MissingInflatedId")
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

        editTextSurname.setVisibility(View.INVISIBLE);
        editTextName.setVisibility(View.INVISIBLE);
        buttonzaRegister.setVisibility(View.INVISIBLE);

        dbHelper = new DatabaseHelper(this);

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

                }
            }
        });

        buttonzaRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = editTextName.getText().toString();
                String surname = editTextSurname.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                isAllFieldsChecked = CheckAllFields();

                long id = dbHelper.insertUser(name, surname, email, password);
                if (isAllFieldsChecked){
                    editTextSurname.setVisibility(View.INVISIBLE);
                    editTextName.setVisibility(View.INVISIBLE);
                    buttonzaRegister.setVisibility(View.INVISIBLE);
                    buttonlogin.setVisibility(View.VISIBLE);
                    buttonRegister.setVisibility(View.VISIBLE);

                }
                else{
                    if (editTextName.length() == 0 && editTextSurname.length() == 0 && editTextEmail.length() == 0) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Ошибка")
                                .setMessage("Вы не заполнили поле")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                    if (editTextPassword.length() < 8) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Ошибка")
                                .setMessage("В пароле должно быть не манее 8 символов")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                }
                if (id < 0) {
                    Toast.makeText(MainActivity.this, "Ошибка, возможно некоторые поля незаполнены", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Вы успешно зарегистрировались", Toast.LENGTH_SHORT).show();
                }

            }
        });

        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                Cursor cursor = dbHelper.searchUserByEmailAndPassword(email, password);
                if (cursor.moveToFirst()) {
                    // Пользователь найден
                    Intent intent = new Intent(MainActivity.this, LoginIn.class);
                    startActivity(intent);
                } else {
                    // Пользователь не найден
                    Toast.makeText(MainActivity.this, "Пользователь не найден, проверте коректность введенных данных или зарегистрируйтесь", Toast.LENGTH_SHORT).show();
                }
                cursor.close();

            }
        });



    }
    private boolean CheckAllFields() {
        if (editTextName.length() == 0) {
            editTextName.setError("This field is required");
            return false;
        }

        if (editTextSurname.length() == 0) {
            editTextSurname.setError("This field is required");
            return false;
        }

        if (editTextEmail.length() == 0) {
            editTextEmail.setError("Email is required");
            return false;
        }

        if (editTextPassword.length() == 0) {
            editTextPassword.setError("Password is required");
            return false;
        } else if (editTextPassword.length() < 8) {
            editTextPassword.setError("Password must be minimum 8 characters");
            return false;
        }

        // after all validation return true.
        return true;
    }

}