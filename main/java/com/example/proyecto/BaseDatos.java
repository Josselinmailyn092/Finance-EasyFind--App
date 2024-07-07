package com.example.proyecto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper {

    // Definir nombre de base de datos y versión
    private static final String DATABASE_NAME = "easyfind.db";
    private static final int DATABASE_VERSION = 1;

    // Nombre de las tablas
    private static final String TABLA_USUARIO = "usuario";
    private static final String TABLA_CATEGORIA = "categoria";
    private static final String TABLA_INGRESOS = "ingresos";
    private static final String TABLA_GASTOS = "gastos";

    // Atributo común
    private static final String KEY_ID = "id";

    // Atributos para usuario
    private static final String Usuario = "usuario";
    private static final String Nombre = "nombre";
    private static final String Apellido = "apellido";
    private static final String Celular = "celular";
    private static final String Contraseña = "contrasena";
    private static final String Ocupacion = "ocupacion";
    private static final String Saldo_Total = "saldo_total";

    // Atributos para categoría
    private static final String NombreCategoria = "nombre";
    private static final String ID_Usuario = "id_cliente";

    // Atributos para ingresos
    private static final String ID_Usuario_Ingreso = "id_cliente";
    private static final String Monto = "monto";
    private static final String Fecha = "fecha";
    private static final String Descripcion = "descripcion";

    // Atributos para gastos
    private static final String ID_Categoria = "id_categoria";
    private static final String ID_Usuario_Gasto = "id_cliente";
    private static final String MontoGastos = "monto_gastos";
    private static final String FechaGastos = "fecha_gastos";
    private static final String DescripcionGastos = "descripcion_gastos";

    // Constructor
    public BaseDatos(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear las tablas al iniciar la base de datos si no existen
        db.execSQL(CREATE_TABLE_USUARIO);
        db.execSQL(CREATE_TABLE_CATEGORIA);
        db.execSQL(CREATE_TABLE_INGRESOS);
        db.execSQL(CREATE_TABLE_GASTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar tablas existentes y crear nuevas versiones de las tablas
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CATEGORIA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_INGRESOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_GASTOS);
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
        values.put(Saldo_Total, saldoTotal);
        db.insert(TABLA_USUARIO, null, values);
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
        values.put(Saldo_Total, saldoTotal);
        // Actualizar fila
        return db.update(TABLA_USUARIO, values, Usuario + " = ?", new String[]{usuario});
    }

    // Método para eliminar un usuario
    public void deleteUsuario(String usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLA_USUARIO, Usuario + " = ?", new String[]{usuario});
        db.close();
    }

    // Método para obtener todos los usuarios
    public Cursor getAllUsuarios() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLA_USUARIO, null, null, null, null, null, null);
    }

    // Método para obtener un usuario por nombre de usuario
    public Cursor getUsuario(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLA_USUARIO, null, Usuario + "=?", new String[]{usuario}, null, null, null);
    }

    // Método para insertar una nueva categoría
    public void insertCategoria(String nombre, Integer idUsuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NombreCategoria, nombre);
        if (idUsuario != null) {
            values.put(ID_Usuario, idUsuario);
        }
        db.insert(TABLA_CATEGORIA, null, values);
        db.close();
    }

    // Método para actualizar una categoría
    public int updateCategoria(int idCategoria, String nombre, Integer idUsuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NombreCategoria, nombre);
        if (idUsuario != null) {
            values.put(ID_Usuario, idUsuario);
        }
        // Actualizar fila
        return db.update(TABLA_CATEGORIA, values, KEY_ID + " = ?", new String[]{String.valueOf(idCategoria)});
    }

    // Método para eliminar una categoría
    public void deleteCategoria(int idCategoria) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLA_CATEGORIA, KEY_ID + " = ?", new String[]{String.valueOf(idCategoria)});
        db.close();
    }

    // Método para obtener todas las categorías
    public Cursor getAllCategorias() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLA_CATEGORIA, null, null, null, null, null, null);
    }

    // Método para obtener una categoría por ID
    public Cursor getCategoria(int idCategoria) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLA_CATEGORIA, null, KEY_ID + "=?", new String[]{String.valueOf(idCategoria)}, null, null, null);
    }

    // Método para insertar un nuevo ingreso
    public void insertIngreso(int idCliente, double monto, String fecha, String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_Usuario_Ingreso, idCliente);
        values.put(Monto, monto);
        values.put(Fecha, fecha);
        values.put(Descripcion, descripcion);
        db.insert(TABLA_INGRESOS, null, values);
        db.close();
    }

    // Método para actualizar un ingreso
    public int updateIngreso(int idIngreso, int idCliente, double monto, String fecha, String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_Usuario_Ingreso, idCliente);
        values.put(Monto, monto);
        values.put(Fecha, fecha);
        values.put(Descripcion, descripcion);
        // Actualizar fila
        return db.update(TABLA_INGRESOS, values, KEY_ID + " = ?", new String[]{String.valueOf(idIngreso)});
    }

    // Método para eliminar un ingreso
    public void deleteIngreso(int idIngreso) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLA_INGRESOS, KEY_ID + " = ?", new String[]{String.valueOf(idIngreso)});
        db.close();
    }

    // Método para obtener todos los ingresos
    public Cursor getAllIngresos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLA_INGRESOS, null, null, null, null, null, null);
    }

    // Método para obtener un ingreso por ID
    public Cursor getIngreso(int idIngreso) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLA_INGRESOS, null, KEY_ID + "=?", new String[]{String.valueOf(idIngreso)}, null, null, null);
    }

    // Método para insertar un nuevo gasto
    public void insertGasto(int idCategoria, int idUsuario, double monto, String fecha, String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_Categoria, idCategoria);
        values.put(ID_Usuario_Gasto, idUsuario);
        values.put(MontoGastos, monto);
        values.put(FechaGastos, fecha);
        values.put(DescripcionGastos, descripcion);
        db.insert(TABLA_GASTOS, null, values);
        db.close();
    }

    // Método para actualizar un gasto
    public int updateGasto(int idGasto, int idCategoria, int idUsuario, double monto, String fecha, String descripcion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_Categoria, idCategoria);
        values.put(ID_Usuario_Gasto, idUsuario);
        values.put(MontoGastos, monto);
        values.put(FechaGastos, fecha);
        values.put(DescripcionGastos, descripcion);
        // Actualizar fila
        return db.update(TABLA_GASTOS, values, KEY_ID + " = ?", new String[]{String.valueOf(idGasto)});
    }

    // Método para eliminar un gasto
    public void deleteGasto(int idGasto) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLA_GASTOS, KEY_ID + " = ?", new String[]{String.valueOf(idGasto)});
        db.close();
    }

    // Método para obtener todos los gastos
    public Cursor getAllGastos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLA_GASTOS, null, null, null, null, null, null);
    }

    // Método para obtener un gasto por ID
    public Cursor getGasto(int idGasto) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLA_GASTOS, null, KEY_ID + "=?", new String[]{String.valueOf(idGasto)}, null, null, null);
    }

}


    /*//Se crean  nuevos usuarios y se insertan en la tabla
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
