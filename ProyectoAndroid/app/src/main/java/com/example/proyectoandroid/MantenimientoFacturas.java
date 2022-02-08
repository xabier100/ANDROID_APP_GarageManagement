package com.example.proyectoandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MantenimientoFacturas extends AppCompatActivity implements View.OnClickListener {

    Button btnAlta,btnBaja,btnModificacion,btnBuscar;
    EditText txtIdCodigoNota, txtFechaReparacion, txtConcepto, txtImporte,txtDNI;
    SQLiteDatabase sqldb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantenimiento_facturas);
        txtIdCodigoNota=findViewById(R.id.txtIdCodigoNota);
        txtFechaReparacion =findViewById(R.id.txtMarca);
        txtConcepto =findViewById(R.id.txtConcepto);
        txtImporte=findViewById(R.id.txtImporte);
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

    @Override
    public void onClick(View v) {
        if (v==btnBuscar){
            buscar();
        }
        else if(v==btnAlta){
            darDeAlta();
        }
        else if(v==btnBaja){
            darDeBaja();
        }
        else if(v==btnModificacion){
            modificar();
        }
    }

    private void buscar() {
        
    }
}