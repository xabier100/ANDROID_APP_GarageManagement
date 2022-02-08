package com.example.proyectoandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MantenimientoVehiculos extends AppCompatActivity {
    Button btnAlta,btnBaja,btnModificacion,btnBuscar;
    EditText txtIdVehiculo, txtMarca, txtModelo, txtMatricula,txtYear,txtDNI;
    SQLiteDatabase sqldb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantenimiento_vehiculos);
        txtIdVehiculo=findViewById(R.id.txtIdCliente);
        txtMarca =findViewById(R.id.txtMarca);
        txtModelo =findViewById(R.id.txtModelo);
        txtMatricula =findViewById(R.id.txtMatricula);
        txtYear=findViewById(R.id.txtYear);
        txtDNI=findViewById(R.id.txtDni);
        
        btnAlta=findViewById(R.id.btnAlta);
        btnBaja=findViewById(R.id.btnBaja);
        btnModificacion=findViewById(R.id.btnModificacion);
        btnBuscar=findViewById(R.id.btnBuscar);
        btnAlta.setOnClickListener(this);
        btnBaja.setOnClickListener(this);
        btnModificacion.setOnClickListener(this);
        btnBuscar.setOnClickListener(this);
    }
}