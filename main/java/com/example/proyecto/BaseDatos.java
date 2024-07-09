package com.example.proyecto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper {

    // Definir nombre de base de datos y versión
    private static final String nombreBD = "easyfind.db";
    private static final int versionDB = 1;

    // Nombre de las tablas
    private static final String tablaUsuario = "usuario";
    private static final String tablaCategorias = "categoria";
    private static final String tablaIngresos = "ingresos";
    private static final String tablaGastos = "gastos";

    // Atributo común
    private static final String keyID = "id";

    // Atributos para usuario
    private static final String Usuario = "usuario";
    private static final String Nombre = "nombre";
    private static final String Apellido = "apellido";
    private static final String Celular = "celular";
    private static final String Contraseña = "contrasena";
    private static final String Ocupacion = "ocupacion";
    private static final String SaldoTotal = "saldo_total";

    // Atributos para categoría
    private static final String NombreCategoria = "nombre";
    private static final String IDUsuario = "id_cliente";

    // Atributos para ingresos
    private static final String IDUsuarioIngreso = "id_cliente";
    private static final String Monto = "monto";
    private static final String Fecha = "fecha";
    private static final String Descripcion = "descripcion";

    // Atributos para gastos
    private static final String IDCategoria = "id_categoria";
    private static final String IDUsuarioGasto = "id_cliente";
    private static final String MontoGastos = "monto_gastos";
    private static final String FechaGastos = "fecha_gastos";
    private static final String DescripcionGastos = "descripcion_gastos";


    // Sentencias SQL para crear las tablas
    private static final String createTableUsuario = "CREATE TABLE " + tablaUsuario + " ("
            + keyID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Usuario + " TEXT, "
            + Nombre + " TEXT, "
            + Apellido + " TEXT, "
            + Celular + " TEXT, "
            + Contraseña + " TEXT, "
            + Ocupacion + " TEXT, "
            + SaldoTotal + " REAL)";

    private static final String createTableCategoria = "CREATE TABLE " + tablaCategorias + " ("
            + keyID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NombreCategoria + " TEXT, "
            + IDUsuario + " INTEGER)";

    private static final String createTableIngresos = "CREATE TABLE " + tablaIngresos + " ("
            + keyID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + IDUsuarioIngreso + " INTEGER, "
            + Monto + " REAL, "
            + Fecha + " TEXT, "
            + Descripcion + " TEXT)";

    private static final String createTableGastos = "CREATE TABLE " + tablaGastos + " ("
            + keyID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + IDCategoria + " INTEGER, "
            + IDUsuarioGasto + " INTEGER, "
            + MontoGastos + " REAL, "
            + FechaGastos + " TEXT, "
            + DescripcionGastos + " TEXT)";

    // Constructor
    public BaseDatos(Context context) {
        super(context, nombreBD, null, versionDB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear las tablas al iniciar la base de datos si no existen
        db.execSQL(createTableUsuario);
        db.execSQL(createTableCategoria);
        db.execSQL(createTableIngresos);
        db.execSQL(createTableGastos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar tablas existentes y crear nuevas versiones de las tablas
        db.execSQL("DROP TABLE IF EXISTS " + tablaUsuario);
        db.execSQL("DROP TABLE IF EXISTS " + tablaCategorias);
        db.execSQL("DROP TABLE IF EXISTS " + tablaGastos);
        onCreate(db);
    }

    // Método para insertar un nuevo usuario
    public void insertUsuario(String usuario, String nombre, String apellido, String celular, String contrasena, String ocupacion, double saldoTotal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Usuario, usuario);
        values.put(Nombre, nombre);
        values.put(Apellido, apellido);
        values.put(Celular, celular);
        values.put(Contraseña, contrasena);
        values.put(Ocupacion, ocupacion);
        values.put(SaldoTotal, saldoTotal);
        db.insert(tablaUsuario, null, values);
        db.close();
    }

    // Método para actualizar un usuario
    public int updateUsuario(String usuario, String nombre, String apellido, String celular, String contrasena, String ocupacion, double saldoTotal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Nombre, nombre);
        values.put(Apellido, apellido);
        values.put(Celular, celular);
        values.put(Contraseña, contrasena);
        values.put(Ocupacion, ocupacion);
        values.put(SaldoTotal, saldoTotal);
        // Actualizar fila
        return db.update(tablaUsuario, values, Usuario + " = ?", new String[]{usuario});
    }

    // Método para eliminar un usuario
    public void deleteUsuario(String usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tablaUsuario, Usuario + " = ?", new String[]{usuario});
        db.close();
    }

    // Método para obtener todos los usuarios
    public Cursor getAllUsuarios() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(tablaUsuario, null, null, null, null, null, null);
    }

    // Método para obtener un usuario por nombre de usuario
    public Cursor getUsuario(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(tablaUsuario, null, Usuario + "=?", new String[]{usuario}, null, null, null);
    }

    // Método para insertar una nueva categoría
    public void insertCategoria(String nombre, Integer idUsuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NombreCategoria, nombre);
        if (idUsuario != null) {
            values.put(IDUsuario, idUsuario);
        }
        db.insert(tablaCategorias, null, values);
        db.close();
    }

    // Método para actualizar una categoría
    public int updateCategoria(int idCategoria, String nombre, Integer idUsuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NombreCategoria, nombre);
        if (idUsuario != null) {
            values.put(IDUsuario, idUsuario);
        }
        // Actualizar fila
        return db.update(tablaCategorias, values, keyID + " = ?", new String[]{String.valueOf(idCategoria)});
    }

    // Método para eliminar una categoría
    public void deleteCategoria(int idCategoria) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tablaCategorias, keyID + " = ?", new String[]{String.valueOf(idCategoria)});
        db.close();
    }

    // Método para obtener todas las categorías
    public Cursor getAllCategorias() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(tablaCategorias, null, null, null, null, null, null);
    }

    // Método para obtener una categoría por ID
    public Cursor getCategoria(int idCategoria) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(tablaCategorias, null, keyID + "=?", new String[]{String.valueOf(idCategoria)}, null, null, null);
    }

    // Método para insertar un nuevo ingreso
    public void insertIngreso(int idCliente, double monto, String fecha, String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IDUsuarioIngreso, idCliente);
        values.put(Monto, monto);
        values.put(Fecha, fecha);
        values.put(Descripcion, descripcion);
        db.insert(tablaIngresos, null, values);
        db.close();
    }

    // Método para actualizar un ingreso
    public int updateIngreso(int idIngreso, int idCliente, double monto, String fecha, String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IDUsuarioIngreso, idCliente);
        values.put(Monto, monto);
        values.put(Fecha, fecha);
        values.put(Descripcion, descripcion);
        // Actualizar fila
        return db.update(tablaIngresos, values, keyID + " = ?", new String[]{String.valueOf(idIngreso)});
    }

    // Método para eliminar un ingreso
    public void deleteIngreso(int idIngreso) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tablaIngresos, keyID + " = ?", new String[]{String.valueOf(idIngreso)});
        db.close();
    }

    // Método para obtener todos los ingresos
    public Cursor getAllIngresos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(tablaIngresos, null, null, null, null, null, null);
    }

    // Método para obtener un ingreso por ID
    public Cursor getIngreso(int idIngreso) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(tablaIngresos, null, keyID + "=?", new String[]{String.valueOf(idIngreso)}, null, null, null);
    }

    // Método para insertar un nuevo gasto
    public void insertGasto(int idCategoria, int idUsuario, double monto, String fecha, String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IDCategoria, idCategoria);
        values.put(IDUsuarioGasto, idUsuario);
        values.put(MontoGastos, monto);
        values.put(FechaGastos, fecha);
        values.put(DescripcionGastos, descripcion);
        db.insert(tablaGastos, null, values);
        db.close();
    }

    // Método para actualizar un gasto
    public int updateGasto(int idGasto, int idCategoria, int idUsuario, double monto, String fecha, String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IDCategoria, idCategoria);
        values.put(IDUsuarioGasto, idUsuario);
        values.put(MontoGastos, monto);
        values.put(FechaGastos, fecha);
        values.put(DescripcionGastos, descripcion);
        // Actualizar fila
        return db.update(tablaGastos, values, keyID + " = ?", new String[]{String.valueOf(idGasto)});
    }

    // Método para eliminar un gasto
    public void deleteGasto(int idGasto) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tablaGastos, keyID + " = ?", new String[]{String.valueOf(idGasto)});
        db.close();
    }

    // Método para obtener todos los gastos
    public Cursor getAllGastos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(tablaGastos, null, null, null, null, null, null);
    }

    // Método para obtener un gasto por ID
    public Cursor getGasto(int idGasto) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(tablaGastos, null, keyID + "=?", new String[]{String.valueOf(idGasto)}, null, null, null);
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


        long id = sqLiteDatabase.insert(tablaUsuario, null, values);
        sqLiteDatabase.close();
        return id;
    }
}