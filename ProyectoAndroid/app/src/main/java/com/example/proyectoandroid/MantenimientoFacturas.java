package com.example.proyectoandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    private void modificar() {
    }

    private void darDeBaja() {
        sqldb=MainActivity.toh.getWritableDatabase();
        String id=txtIdCodigoNota.getText().toString();
        sqldb.delete("NotasReparaciones","CodigoNota=?",new String[]{id});
        btnBaja.setEnabled(false);
        btnModificacion.setEnabled(false);
        btnAlta.setEnabled(true);
    }

    private void darDeAlta() {
        sqldb=MainActivity.toh.getWritableDatabase();
        ContentValues cv=new ContentValues();
        llenarCv(cv);
        try {
            sqldb.insertOrThrow("NotasReparaciones",null,cv);
            //Habilitar y deshabilitar los botones correspondientes
            btnAlta.setEnabled(false);
            btnBaja.setEnabled(true);
            btnModificacion.setEnabled(true);
        }
        catch(Exception e){
            Toast.makeText(this,"Ha habido un error con el DNI",Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void llenarCv(ContentValues cv) {
        String idCodigoNota=txtIdCodigoNota.getText().toString();
        String dni=txtDNI.getText().toString();
        String fechaReparacion=txtFechaReparacion.getText().toString();
        String concepto=txtConcepto.getText().toString();
        String importe=txtImporte.getText().toString();
        //Llenar el contentValue con el valor de la caja y su nombre
        String[]elementos=new String[]{idCodigoNota,dni,fechaReparacion,concepto,importe};
        String[]nombreelementos=new String[]{"idCodigoNota","DNI","FechaReparacion","Concepto","Importe"};
        for (int i = 0; i < elementos.length; i++) {
            cv.put(nombreelementos[i],elementos[i]);
        }
    }

    private void buscar() {
        String idCodigoNota=txtIdCodigoNota.getText().toString();
        sqldb=MainActivity.toh.getReadableDatabase();
        Cursor c=sqldb.query("NotasReparaciones",new String[]
                        {"CodigoNota","DNI","FechaReparacion","Concepto","Importe"},
                "CodigoNota=?",
                new String[]{idCodigoNota},
                null,null,null);
        if (c.moveToNext()){
            //Insertar los datos del cursor en las cajas de texto
            txtDNI.setText(c.getString(1));
            txtFechaReparacion.setText(c.getString(2));
            txtConcepto.setText(c.getString(3));
            txtImporte.setText(Integer.toString(c.getInt(4)));
            //Habilitar y deshabilitar los botones correspondientes
            btnAlta.setEnabled(false);
            btnBaja.setEnabled(true);
            btnModificacion.setEnabled(true);
        }
        else{
            mostrarMensaje("No se ha encontrado esa factura");
            //Habilitar y deshabilitar los botones correspondientes
            btnAlta.setEnabled(true);
            btnBaja.setEnabled(false);
            btnModificacion.setEnabled(false);
        }
    }

    private void mostrarMensaje(String s) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }
}