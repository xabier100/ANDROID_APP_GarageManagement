package com.example.proyectoandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TallerOpenHelper extends SQLiteOpenHelper {

    private String strCrearTablaClientes = "CREATE TABLE Clientes (" +
            "    IdCliente INTEGER," +
            "    Nombre    TEXT," +
            "    Apellido  TEXT," +
            "    DNI       TEXT    UNIQUE," +
            "    ImgCliente TEXT," +
            "    PRIMARY KEY (" +
            "        IdCliente" +
            "    )," +
            "    UNIQUE (" +
            "        DNI" +
            "    )" +
            ");";
    private String strCrearTablaVehiculos = "CREATE TABLE Vehiculos (" +
            "    IdVehiculo INTEGER," +
            "    Marca      TEXT," +
            "    Modelo     TEXT," +
            "    Matricula  TEXT," +
            "    Year        INTEGER," +
            "    Dni        TEXT UNIQUE," +
            "    FOREIGN KEY (" +
            "        Dni" +
            "    )" +
            "    REFERENCES Clientes (DNI)," +
            "    PRIMARY KEY (" +
            "        IdVehiculo" +
            "    )" +
            ");";
    private String strCrearTablaReparaciones = "CREATE TABLE NotasReparaciones (" +
            "    CodigoNota         INTEGER," +
            "    DNI                TEXT UNIQUE," +
            "    FechaReparacion INTEGER," +
            "    Concepto          TEXT," +
            "    Importe            INTEGER," +
            "    PRIMARY KEY (" +
            "        CodigoNota" +
            "    )," +
            "    FOREIGN KEY (" +
            "        DNI" +
            "    )" +
            "    REFERENCES Clientes (DNI) " +
            ");";

    private String strTriInsFKDNiNotasReparaciones = "CREATE TRIGGER insertFKdniNotasReparaciones" +
            "        BEFORE INSERT" +
            "            ON NotasReparaciones" +
            "      FOR EACH ROW " +
            "BEGIN" +
            "    SELECT RAISE(ABORT, \"No existe registro seleccionado\")" +
            "     WHERE new.DNI IS NULL OR " +
            "           NOT EXISTS (" +
            "                   SELECT DNI" +
            "                     FROM Clientes" +
            "                    WHERE DNI = new.DNI" +
            "               );" +
            "END;";

    private String strTriUpdFKDNINotasReparaciones="CREATE TRIGGER updateFKdniNotasReparaciones" +
            "        BEFORE UPDATE" +
            "            ON NotasReparaciones" +
            "      FOR EACH ROW " +
            "BEGIN" +
            "    SELECT RAISE(ABORT, \"No existe registro seleccionado\") " +
            "     WHERE new.DNI IS NULL OR " +
            "           NOT EXISTS (" +
            "                   SELECT DNI" +
            "                     FROM Clientes" +
            "                    WHERE DNI = new.DNI" +
            "               );" +
            "END;";

    private String strTriInsFKDNIVehiculos = "CREATE TRIGGER insertFKdniVehiculos" +
            "        BEFORE INSERT" +
            "            ON Vehiculos" +
            "      FOR EACH ROW " +
            "BEGIN" +
            "    SELECT RAISE(ABORT, \"No existe registro seleccionado\")" +
            "     WHERE new.DNI IS NULL OR " +
            "           NOT EXISTS (" +
            "                   SELECT DNI" +
            "                     FROM Clientes" +
            "                    WHERE DNI = new.DNI" +
            "               );" +
            "END;";

    private String strTriUpdFKDNIVehiculos="CREATE TRIGGER updateFKdniVehiculos" +
            "        BEFORE UPDATE" +
            "            ON Vehiculos" +
            "      FOR EACH ROW " +
            "BEGIN" +
            "    SELECT RAISE(ABORT, \"No existe registro seleccionado\") " +
            "     WHERE new.DNI IS NULL OR " +
            "           NOT EXISTS (" +
            "                   SELECT DNI" +
            "                     FROM Clientes" +
            "                    WHERE DNI = new.DNI" +
            "               );" +
            "END;";

    private String strDropTablaClientes = "DROP TABLE IF EXISTS Clientes";
    private String strDropTablaVehiculos = "DROP TABLE IF EXISTS Vehiculos";
    private String strDropTablaFacturas = "DROP TABLE IF EXISTS NotasReparaciones";

    public TallerOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private Cliente[] clientes = new Cliente[]{
            new Cliente(1, "pedro", "jimenez", "1G"),
            new Cliente(2, "xabier", "garrote", "2G"),
            new Cliente(3, "jose", "ortiz", "3G"),};

    private String[] sentencias={strCrearTablaClientes,strCrearTablaReparaciones,strCrearTablaVehiculos,
            strTriInsFKDNIVehiculos,strTriInsFKDNiNotasReparaciones,
            strTriUpdFKDNIVehiculos,strTriUpdFKDNINotasReparaciones};
    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String sentencia : sentencias) {
            db.execSQL(sentencia);
        }
        inicializarValores(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(strDropTablaClientes);
        db.execSQL(strDropTablaVehiculos);
        db.execSQL(strDropTablaFacturas);
        this.onCreate(db);
    }

    private void inicializarValores(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        for (Cliente cliente : clientes) {
            cv.put("idCliente", cliente.getIdCliente());
            cv.put("nombre", cliente.getNombre());
            cv.put("apellido", cliente.getApellido());
            cv.put("DNI", cliente.getDni());
            db.insertOrThrow("Clientes", null, cv);
        }
    }
}