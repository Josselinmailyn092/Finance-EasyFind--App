package com.example.proyecto;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DetallesPerfilActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imgProfilePhoto;
    private EditText etUsername, etFirstName, etLastName, etPhone, etPassword, etProfession;
    private Button btnChangePhoto, btnSave;
    private BaseDatos baseDatos;
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detallesperfil);

        imgProfilePhoto = findViewById(R.id.imgProfilePhoto);
        etUsername = findViewById(R.id.etUsername);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etProfession = findViewById(R.id.etProfession);
        btnChangePhoto = findViewById(R.id.btnChangePhoto);
        btnSave = findViewById(R.id.btnSave);

        baseDatos = new BaseDatos(this);
        currentUser = getIntent().getStringExtra("USERNAME");
/*
        loadUserData();

        btnChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });
    }
/*
    private void loadUserData() {
        Cursor cursor = baseDatos.getUsuario(currentUser);
        if (cursor != null && cursor.moveToFirst()) {
            etUsername.setText(cursor.getString(cursor.getColumnIndex("usuario")));
            etFirstName.setText(cursor.getString(cursor.getColumnIndex("nombre")));
            etLastName.setText(cursor.getString(cursor.getColumnIndex("apellido")));
            etPhone.setText(cursor.getString(cursor.getColumnIndex("celular")));
            etPassword.setText(cursor.getString(cursor.getColumnIndex("contrasena")));
            etProfession.setText(cursor.getString(cursor.getColumnIndex("ocupacion")));

            cursor.close();
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imgProfilePhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveUserData() {
        String username = etUsername.getText().toString();
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();
        String profession = etProfession.getText().toString();

        int rowsAffected = baseDatos.updateUsuario(username, firstName, lastName, phone, password, profession, 0.0);
        if (rowsAffected > 0) {
            Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al actualizar datos", Toast.LENGTH_SHORT).show();
        }
    }*/
    }}

