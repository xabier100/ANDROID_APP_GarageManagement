package com.example.proyectoandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TallerOpenHelper extends SQLiteOpenHelper {

    private String strCrearTablaClientes="CREATE TABLE Clientes (" +
            "    IdCliente INTEGER," +
            "    Nombre    TEXT," +
            "    Apellido  TEXT," +
            "    DNI       TEXT    UNIQUE," +
            "    PRIMARY KEY (" +
            "        IdCliente" +
            "    )," +
            "    UNIQUE (" +
            "        DNI" +
            "    )" +
            ");";
    private String strCrearTablaVehiculos="CREATE TABLE Vehiculos (" +
            "    IdVehiculo INTEGER," +
            "    Marca      TEXT," +
            "    Modelo     TEXT," +
            "    Matricula  TEXT," +
            "    Año        INTEGER," +
            "    Dni        INTEGER UNIQUE," +
            "    FOREIGN KEY (" +
            "        Dni" +
            "    )" +
            "    REFERENCES Clientes (DNI)," +
            "    PRIMARY KEY (" +
            "        IdVehiculo" +
            "    )" +
            ");";
    private String strCrearTablaReparaciones="CREATE TABLE NotasReparaciones (" +
            "    CodigoNota         INTEGER," +
            "    DNI                INTEGER UNIQUE," +
            "    FechaReparacion INTEGER," +
            "    Concepcto          TEXT," +
            "    Importe            INTEGER," +
            "    PRIMARY KEY (" +
            "        CodigoNota" +
            "    )," +
            "    FOREIGN KEY (" +
            "        DNI" +
            "    )" +
            "    REFERENCES Clientes (DNI) " +
            ");";

    private String strTriggerFKDNiNotasReparaciones="CREATE TRIGGER insertFKdniNotasReparaciones" +
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
    private String strTriggerFKDNIVehiculos="CREATE TRIGGER insertFKdniVehiculos" +
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


    public TallerOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(strCrearTablaClientes);
        db.execSQL(strCrearTablaReparaciones);
        db.execSQL(strCrearTablaVehiculos);
        db.execSQL(strTriggerFKDNIVehiculos);
        db.execSQL(strTriggerFKDNiNotasReparaciones);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
