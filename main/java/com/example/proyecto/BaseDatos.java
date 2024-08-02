package com.example.proyecto;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatos extends SQLiteOpenHelper {

    // Definir nombre de base de datos y versión
    private static final String DATABASE_NAME = "easyfind.db";
    private static final int DATABASE_VERSION = 2;

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
        db.execSQL("CREATE TABLE " + TABLA_USUARIO + " (" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Usuario + " TEXT UNIQUE, " +
                Nombre + " TEXT, " +
                Apellido + " TEXT, " +
                Celular + " TEXT, " +
                Contraseña + " TEXT, " +
                Ocupacion + " TEXT, " +
                Saldo_Total + " REAL)");

        db.execSQL("CREATE TABLE " + TABLA_CATEGORIA + " (" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NombreCategoria + " TEXT, " +
                ID_Usuario + " INTEGER, " +
                "FOREIGN KEY(" + ID_Usuario + ") REFERENCES " + TABLA_USUARIO + "(" + KEY_ID + "))");

        db.execSQL("CREATE TABLE " + TABLA_INGRESOS + " (" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ID_Usuario_Ingreso + " INTEGER, " +
                Monto + " REAL, " +
                Fecha + " TEXT, " +
                Descripcion + " TEXT, " +
                "FOREIGN KEY(" + ID_Usuario_Ingreso + ") REFERENCES " + TABLA_USUARIO + "(" + KEY_ID + "))");

        db.execSQL("CREATE TABLE " + TABLA_GASTOS + " (" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ID_Categoria + " INTEGER, " +
                ID_Usuario_Gasto + " INTEGER, " +
                MontoGastos + " REAL, " +
                FechaGastos + " TEXT, " +
                DescripcionGastos + " TEXT, " +
                "FOREIGN KEY(" + ID_Categoria + ") REFERENCES " + TABLA_CATEGORIA + "(" + KEY_ID + "), " +
                "FOREIGN KEY(" + ID_Usuario_Gasto + ") REFERENCES " + TABLA_USUARIO + "(" + KEY_ID + "))");

        // Insertar usuario inicial
        db.execSQL("INSERT INTO " + TABLA_USUARIO + " (" + Usuario + ", " + Nombre + ", " + Apellido + ", " + Celular + ", " + Contraseña + ", " + Ocupacion + ", " + Saldo_Total + ") VALUES ('admin@gmail.com', 'Administración','OLA', '0000000000', 'admin', 'Ingeniero', 0.0)");

        // Insertar datos ficticios
        db.execSQL("INSERT INTO " + TABLA_CATEGORIA + " (" + NombreCategoria + ", " + ID_Usuario + ") VALUES ('Alimentación', 1)");
        db.execSQL("INSERT INTO " + TABLA_CATEGORIA + " (" + NombreCategoria + ", " + ID_Usuario + ") VALUES ('Alquiler', 1)");
        db.execSQL("INSERT INTO " + TABLA_CATEGORIA + " (" + NombreCategoria + ", " + ID_Usuario + ") VALUES ('Deporte', 1)");

        // Insertar gastos ficticios
        db.execSQL("INSERT INTO " + TABLA_GASTOS + " (" + ID_Categoria + ", " + ID_Usuario_Gasto + ", " + MontoGastos + ", " + FechaGastos + ", " + DescripcionGastos + ") VALUES (1, 1, 50.0, '2024-08-01', 'Compra de comida')");
        db.execSQL("INSERT INTO " + TABLA_GASTOS + " (" + ID_Categoria + ", " + ID_Usuario_Gasto + ", " + MontoGastos + ", " + FechaGastos + ", " + DescripcionGastos + ") VALUES (2, 1, 500.0, '2024-08-01', 'Pago de alquiler')");
        db.execSQL("INSERT INTO " + TABLA_GASTOS + " (" + ID_Categoria + ", " + ID_Usuario_Gasto + ", " + MontoGastos + ", " + FechaGastos + ", " + DescripcionGastos + ") VALUES (3, 1, 30.0, '2024-08-01', 'Gimnasio')");

        // Insertar ingresos ficticios
        db.execSQL("INSERT INTO " + TABLA_INGRESOS + " (" + ID_Usuario_Ingreso + ", " + Monto + ", " + Fecha + ", " + Descripcion + ") VALUES (1, 1000.0, '2024-08-01', 'Salario')");
        db.execSQL("INSERT INTO " + TABLA_INGRESOS + " (" + ID_Usuario_Ingreso + ", " + Monto + ", " + Fecha + ", " + Descripcion + ") VALUES (1, 200.0, '2024-08-01', 'Venta de artículos')");

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


    // Método para obtener un usuario por nombre de usuario
    public Cursor getUsuario(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLA_USUARIO, null, Usuario + "=?", new String[]{usuario}, null, null, null);
    }
    // Método para insertar un nuevo usuario
    public long insertUsuario(String usuario, String nombre, String apellido, String celular, String contrasena, String ocupacion, double saldoTotal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Usuario, usuario);
        values.put(Nombre, nombre);
        values.put(Apellido, apellido);
        values.put(Celular, celular);
        values.put(Contraseña, contrasena);
        values.put(Ocupacion, ocupacion);
        values.put(Saldo_Total, saldoTotal);
        long id = db.insert(TABLA_USUARIO, null, values);
        db.close();
        return id;
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
        int rowsAffected = db.update(TABLA_USUARIO, values, Usuario + "=?", new String[]{usuario});
        db.close();
        return rowsAffected;
    }
    // Método para eliminar un usuario por nombre de usuario
    public int deleteUsuario(String usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLA_USUARIO, Usuario + "=?", new String[]{usuario});
        db.close();
        return rowsAffected;
    }
    public int getUserIdByusuario(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + KEY_ID + " FROM " + TABLA_USUARIO + " WHERE " + Usuario + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{usuario});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID));
        }
        cursor.close();
        return userId;
    }

    // Método para obtener ingresos por mes
    public Cursor getIngresosPorMes(int userId, int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT descripcion, monto, fecha FROM " + TABLA_INGRESOS +
                " WHERE strftime('%m', fecha) = ? AND strftime('%Y', fecha) = ? AND " + ID_Usuario_Ingreso + " = ?";
        return db.rawQuery(query, new String[]{String.format("%02d", month), String.valueOf(year), String.valueOf(userId)});
    }

    // Método para obtener gastos por mes
    public Cursor getGastosPorMes(int userId, int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT descripcion, monto, fecha FROM " + TABLA_GASTOS +
                " WHERE strftime('%m', fecha) = ? AND strftime('%Y', fecha) = ? AND " + ID_Usuario_Gasto + " = ?";
        return db.rawQuery(query, new String[]{String.format("%02d", month), String.valueOf(year), String.valueOf(userId)});
    }

    // Método para obtener ingresos por semana
    public Cursor getIngresosPorSemana(int userId, String startDate, String endDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT descripcion, monto, fecha FROM " + TABLA_INGRESOS +
                " WHERE date(fecha) BETWEEN date(?) AND date(?) AND " + ID_Usuario_Ingreso + " = ?";
        return db.rawQuery(query, new String[]{startDate, endDate, String.valueOf(userId)});
    }

    // Método para obtener gastos por semana
    public Cursor getGastosPorSemana(int userId, String startDate, String endDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT descripcion, monto, fecha FROM " + TABLA_GASTOS +
                " WHERE date(fecha) BETWEEN date(?) AND date(?) AND " + ID_Usuario_Gasto + " = ?";
        return db.rawQuery(query, new String[]{startDate, endDate, String.valueOf(userId)});
    }

    // Método para obtener ingresos por año
    public Cursor getIngresosPorAño(int userId, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT descripcion, monto, fecha FROM " + TABLA_INGRESOS +
                " WHERE strftime('%Y', fecha) = ? AND " + ID_Usuario_Ingreso + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(year), String.valueOf(userId)});
    }

    // Método para obtener gastos por año
    public Cursor getGastosPorAño(int userId, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT descripcion, monto, fecha FROM " + TABLA_GASTOS +
                " WHERE strftime('%Y', fecha) = ? AND " + ID_Usuario_Gasto + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(year), String.valueOf(userId)});
    }

    // Método para obtener la suma total de ingresos para un usuario
    public double getTotalIngresos(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + Monto + ") as total FROM " + TABLA_INGRESOS + " WHERE " + ID_Usuario_Ingreso + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            double total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
            cursor.close();
            return total;
        } else {
            cursor.close();
            return 0.0;
        }
    }

    // Método para obtener la suma total de gastos para un usuario
    public double getTotalGastos(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + MontoGastos + ") as total FROM " + TABLA_GASTOS + " WHERE " + ID_Usuario_Gasto + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            double total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
            cursor.close();
            return total;
        } else {
            cursor.close();
            return 0.0;
        }
    }

    // Método para obtener una categoría por ID
    public Cursor getCategoria(int idCategoria) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLA_CATEGORIA, null, KEY_ID + "=?", new String[]{String.valueOf(idCategoria)}, null, null, null);
    }



    // Método para obtener todos los usuarios
    public Cursor getAllUsuarios() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLA_USUARIO, null, null, null, null, null, null);
    }
    // Métodos para categorías

    public Cursor getAllCategorias() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLA_CATEGORIA, null);
    }
    public Cursor getCategoriaNombre(int idCategoria) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM categorias WHERE id = ?", new String[]{String.valueOf(idCategoria)});
    }

    public boolean insertCategoria(String nombre, int idUsuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NombreCategoria, nombre);
        values.put(ID_Usuario, idUsuario);
        long result = db.insert(TABLA_CATEGORIA, null, values);
        return result != -1;
    }

    public boolean deleteCategoria(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLA_CATEGORIA, KEY_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public Cursor getCategoriaNombre(String nombre) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " + KEY_ID + " FROM " + TABLA_CATEGORIA + " WHERE " + NombreCategoria + " =?", new String[]{nombre});
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

    // Método para obtener un usuario por ID
    public Cursor getUsuarioById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                TABLA_USUARIO,        // Nombre de la tabla
                null,                 // Seleccionar todas las columnas
                KEY_ID + "=?",        // Condición WHERE
                new String[]{String.valueOf(userId)},  // Argumentos para la condición WHERE
                null,                 // Agrupamiento
                null,                 // Ordenamiento
                null                  // Limitación
        );
    }

    // Método para obtener las 3 categorías más recientes (por usuario y genéricas)
    public Cursor getCategoriasPorUsuario(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLA_CATEGORIA +
                " WHERE " + ID_Usuario + " = ? OR " + ID_Usuario + " = 0" +
                " ORDER BY " + KEY_ID + " DESC LIMIT 3";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }

    //MEtodo para obtener 3 gastos de usuario
    public Cursor getGastosPorUsuario(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLA_GASTOS +
                " WHERE " + ID_Usuario_Gasto + " = ?" +
                " ORDER BY " + FechaGastos + " DESC LIMIT 3";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }

    // Método para obtener los ingresos por usuario
    public Cursor getIngresosPorUsuario(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLA_INGRESOS +
                " WHERE " + ID_Usuario_Ingreso + " = ?" +
                " ORDER BY " + Fecha + " DESC ";

        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }


    // Método para obtener los ingresos del mes actual
    public double getIngresoMes(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + Monto + ") as total FROM " + TABLA_INGRESOS +
                " WHERE " + ID_Usuario_Ingreso + " = ?" +
                " AND strftime('%m', " + Fecha + ") = strftime('%m', 'now')" +
                " AND strftime('%Y', " + Fecha + ") = strftime('%Y', 'now')";

        double ingresoTotal = 0.0;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
            if (cursor != null && cursor.moveToFirst()) {
                ingresoTotal = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return ingresoTotal;
    }

    // Método para obtener los gastos del mes actual
    public double getGastoMes(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + MontoGastos + ") as total FROM " + TABLA_GASTOS +
                " WHERE " + ID_Usuario_Gasto + " = ?" +
                " AND strftime('%m', " + FechaGastos + ") = strftime('%m', 'now')" +
                " AND strftime('%Y', " + FechaGastos + ") = strftime('%Y', 'now')";

        double gastoTotal = 0.0;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
            if (cursor != null && cursor.moveToFirst()) {
                gastoTotal = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return gastoTotal;
    }


    // Método para obtener el saldo total del usuario
    public double getSaldoTotalUsuario(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + Saldo_Total + " FROM " + TABLA_USUARIO +
                " WHERE " + KEY_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.moveToFirst()) {
            double saldoTotal = cursor.getDouble(cursor.getColumnIndexOrThrow(Saldo_Total));
            cursor.close();
            return saldoTotal;
        }
        return 0.0;
    }
}