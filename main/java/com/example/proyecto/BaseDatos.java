package com.example.proyecto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class BaseDatos extends SQLiteOpenHelper {
    //definir nombre de base de datos y version
    private static final String bdNombre = "easyfind.db";
    private static final int dbVersion = 1;


    //Nombre de la tabla y atributos
    private static final String Tabla = "usuarios";
    private static final String  Id= "id";
    private static final String Usuario = "usuario";
    private static final String Nombre= "nombre";
    private static final String Apellido = "apellido";
    private static final String Celular = "celular";
    private static final String Contraseña= "contraseña";
    private static final String Ocupación= "ocupacion";

    //Constructor
    public BaseDatos (Context context){
        super(context, bdNombre, null, dbVersion);
    }




    public BaseDatos(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    //Se crea base tabla Usuarios y sus columnas
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + Nombre + "("
                + Id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Usuario + " TEXT,"
                + Nombre + " TEXT,"
                + Apellido + " TEXT,"
                + Celular+ " TEXT,"
                + Contraseña + " TEXT,"
                + Ocupación+ " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    //Actualización de base de datos, se elimina la tabla existente y se crea nuevamente
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int anteriosVersion, int nuevaVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Tabla);
        onCreate(sqLiteDatabase);
    }

    //Se crean  nuevos usuarios y se insertan en la tabla
    public long addUsuario(String usuario, String nombre, String apellido, String celular, String contraseña, String ocupacion) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Usuario, usuario);
        values.put(Nombre, nombre);
        values.put(Apellido, apellido);
        values.put(Celular, celular);
        values.put(Contraseña, contraseña);
        values.put(Ocupación, ocupacion);

        long id = sqLiteDatabase.insert(Tabla, null, values);
        sqLiteDatabase.close();
        return id;
    }
    //Se obtiene datos de usuario acorde a la consulta y devuelve resultado
    public Cursor getUsuario(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(Tabla, null, Usuario + "=?", new String[]{usuario}, null, null, null);
    }


    // Realiza actualizacion de un registro ya existente
    public int updateUsuario(String usuario, String nombre, String apellido, String celular, String contraseña, String ocupacion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Nombre, nombre);
        values.put(Apellido, apellido);
        values.put(Celular, celular);
        values.put(Contraseña, contraseña);
        values.put(Ocupación, ocupacion);

        return db.update(Tabla, values, Usuario + "=?", new String[]{usuario});
    }
    //Elimina un usuario ya exitente
    public int deleteUsuario(String usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Tabla, Usuario + "=?", new String[]{usuario});
    }




}
