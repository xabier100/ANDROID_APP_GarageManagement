package com.example.proyectoandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TallerOpenHelper extends SQLiteOpenHelper {

    /*Sentencias de creacion de tablas*/
    private String strCrearTablaClientes = "CREATE TABLE Clientes (" +
            "    IdCliente  INTEGER CHECK (IdCliente > 0)," +
            "    Nombre     TEXT," +
            "    Apellido   TEXT," +
            "    DNI        TEXT    UNIQUE," +
            "    ImgCliente TEXT," +
            "    PRIMARY KEY (" +
            "        IdCliente" +
            "    )," +
            "    UNIQUE (" +
            "        DNI" +
            "    )" +
            ");";
    private String strCrearTablaFacturas = "CREATE TABLE Facturas (" +
            "    IdFactura       INTEGER CHECK (IdFactura > 0)," +
            "    IdCliente       INTEGER," +
            "    FechaReparacion TEXT," +
            "    Concepto        TEXT," +
            "    Importe         INTEGER," +
            "    PRIMARY KEY (" +
            "        IdFactura" +
            "    )," +
            "    FOREIGN KEY (" +
            "        IdCliente" +
            "    )" +
            "    REFERENCES Clientes (IdCliente) " +
            ");";
    private String strCrearTablaVehiculos = "CREATE TABLE Vehiculos (" +
            "    IdVehiculo INTEGER CHECK (IdVehiculo > 0)," +
            "    IdCliente  INTEGER," +
            "    Marca      TEXT," +
            "    Modelo     TEXT," +
            "    Matricula  TEXT," +
            "    FOREIGN KEY (" +
            "        IdCliente" +
            "    )" +
            "    REFERENCES Clientes (IdCliente)," +
            "    PRIMARY KEY (" +
            "        IdVehiculo" +
            "    )" +
            ");";

    /*Sentencias de creacion de triggers*/
    private String strTriInsFKIdClienteFacturas ="CREATE TRIGGER insertFKIdClienteFacturas" +
            "        BEFORE INSERT" +
            "            ON Facturas" +
            "      FOR EACH ROW " +
            "BEGIN" +
            "    SELECT RAISE(ABORT, \"No existe registro seleccionado en la tabla clientes\") " +
            "     WHERE new.IdCliente IS NULL OR " +
            "           NOT EXISTS (" +
            "                   SELECT IdCliente" +
            "                     FROM Clientes" +
            "                    WHERE IdCliente = new.IdCliente" +
            "               );" +
            "END;" ;
    private String strTriUpdFKIdClienteFacturas ="CREATE TRIGGER updateFKIdClienteFacturas" +
            "        BEFORE UPDATE" +
            "            ON Facturas" +
            "      FOR EACH ROW " +
            "BEGIN" +
            "    SELECT RAISE(ABORT, \"No existe registro seleccionado en la tabla clientes\") " +
            "     WHERE new.IdCliente IS NULL OR " +
            "           NOT EXISTS (" +
            "                   SELECT IdCliente" +
            "                     FROM Clientes" +
            "                    WHERE IdCliente = new.IdCliente" +
            "               );" +
            "END;";
    private String strTriInsFKIdClienteVehiculos = "CREATE TRIGGER insertFKIdClienteVehiculos" +
            "        BEFORE INSERT" +
            "            ON Vehiculos" +
            "      FOR EACH ROW " +
            "BEGIN" +
            "    SELECT RAISE(ABORT, \"No existe registro seleccionado en la tabla clientes\") " +
            "     WHERE new.IdCliente IS NULL OR " +
            "           NOT EXISTS (" +
            "                   SELECT IdCliente" +
            "                     FROM Clientes" +
            "                    WHERE IdCliente = new.IdCliente" +
            "               );" +
            "END;";
    private String strTriUpdFKIdClienteVehiculos ="CREATE TRIGGER updateFKIdClienteVehiculos" +
            "        BEFORE UPDATE" +
            "            ON Vehiculos" +
            "      FOR EACH ROW " +
            "BEGIN" +
            "    SELECT RAISE(ABORT, \"No existe registro seleccionado\") " +
            "     WHERE new.IdCliente IS NULL OR " +
            "           NOT EXISTS (" +
            "                   SELECT IdCliente" +
            "                     FROM Clientes" +
            "                    WHERE IdCliente = new.IdCliente" +
            "               );" +
            "END;";
    /*Sentencias de eliminacion de triggers*/
    private String strDropTablaClientes = "DROP TABLE IF EXISTS Clientes";
    private String strDropTablaVehiculos = "DROP TABLE IF EXISTS Vehiculos";
    private String strDropTablaFacturas = "DROP TABLE IF EXISTS Facturas";
    private String strDropTablaNotasReparaciones="DROP TABLE IF EXISTS NotasReparaciones";
    public TallerOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private Cliente[] clientes = new Cliente[]{
            new Cliente(1, "pedro", "jimenez", "1G"),
            new Cliente(2, "xabier", "garrote", "2G"),
            new Cliente(5, "jose", "ortiz", "3G"),};

    private String[] sentencias={strCrearTablaClientes, strCrearTablaFacturas,strCrearTablaVehiculos,
            strTriInsFKIdClienteVehiculos,strTriInsFKIdClienteFacturas,
            strTriUpdFKIdClienteVehiculos, strTriUpdFKIdClienteFacturas};
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
        db.execSQL(strDropTablaNotasReparaciones);
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