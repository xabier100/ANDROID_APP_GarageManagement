package com.example.proyectoandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MantenimientoVehiculos extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Button btnAlta,btnBaja,btnModificacion,btnBuscar;
    EditText txtIdVehiculo, txtMarca, txtModelo, txtMatricula;
    SQLiteDatabase sqldb;
    Spinner spClientes;
    MediaPlayer mpTokyoDrift;
    String idCliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantenimiento_vehiculos);
        txtIdVehiculo=findViewById(R.id.txtIdVehiculo);
        txtMarca =findViewById(R.id.txtMarca);
        txtModelo =findViewById(R.id.txtModelo);
        txtMatricula =findViewById(R.id.txtMatricula);
        spClientes=findViewById(R.id.spClientes);
        cargarSpinner(spClientes);
        spClientes.setOnItemSelectedListener(this);
        mpTokyoDrift=MediaPlayer.create(this,R.raw.tokyo_drift);
        mpTokyoDrift.start();
        AudioManager ad=(AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        int volumen=ad.getStreamMaxVolume(AudioManager.STREAM_MUSIC)/2;
        while (volumen>ad.getStreamVolume(AudioManager.STREAM_MUSIC)){
            ad.adjustVolume(AudioManager.ADJUST_RAISE,AudioManager.FLAG_PLAY_SOUND);
        }
        mpTokyoDrift.setLooping(true);
        btnAlta=findViewById(R.id.btnAlta);
        btnBaja=findViewById(R.id.btnBaja);
        btnModificacion=findViewById(R.id.btnModificacion);
        btnBuscar=findViewById(R.id.btnBuscar);
        btnAlta.setOnClickListener(this);
        btnBaja.setOnClickListener(this);
        btnModificacion.setOnClickListener(this);
        btnBuscar.setOnClickListener(this);
    }

    private void cargarSpinner(Spinner spClientes) {
        sqldb=MainActivity.toh.getReadableDatabase();
        Cursor c=sqldb.query("Clientes",new String[]{"Nombre as _id","Apellido","DNI"},null,null,null,null,"IdCliente");
        String[] columnas=new String[]{"_id","Apellido","DNI"};
        int []textViews=new int[]{R.id.txtNombre,R.id.txtApellido,R.id.txtDni};
        SimpleCursorAdapter sca=new SimpleCursorAdapter(this,R.layout.fila_spcategorias,c,columnas,textViews, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        spClientes.setAdapter(sca);
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

    @Override
    public void onBackPressed() {
        mpTokyoDrift.stop();
        super.onBackPressed();
    }

    private void modificar() {
        sqldb=MainActivity.toh.getWritableDatabase();
        String id=txtIdVehiculo.getText().toString();
        ContentValues cv=new ContentValues();
        llenarCv(cv);
        try {
            sqldb.update("Vehiculos",cv,"IdVehiculo=?",new String[]{id});
            mostrarMensaje("Vehiculo modificado con exito");
        }
        catch (Exception e){
            mostrarMensaje("Ha habido un error:"+e.getMessage());
        }
    }

    private void darDeBaja() {
        sqldb=MainActivity.toh.getWritableDatabase();
        String id=txtIdVehiculo.getText().toString();
        sqldb.delete("Vehiculos","IdVehiculo=?",new String[]{id});
        btnBaja.setEnabled(false);
        btnModificacion.setEnabled(false);
        btnAlta.setEnabled(true);
    }

    private void darDeAlta() {
        sqldb=MainActivity.toh.getWritableDatabase();
        ContentValues cv=new ContentValues();
        llenarCv(cv);
        try {
            sqldb.insertOrThrow("Vehiculos",null,cv);
            //Habilitar y deshabilitar los botones correspondientes
            btnAlta.setEnabled(false);
            btnBaja.setEnabled(true);
            btnModificacion.setEnabled(true);
        }
        catch(Exception e){
            mostrarMensaje("Ha habido un error "+e.getMessage());
        }
    }

    private void llenarCv(ContentValues cv) {
        //Obtener los valores de cada caja de texto
        String idVehiculo=txtIdVehiculo.getText().toString();
        String marca=txtMarca.getText().toString();
        String modelo=txtModelo.getText().toString();
        String matricula=txtMatricula.getText().toString();
        String IdCliente=idCliente;
        //Llenar el contentValue con el valor de la caja y su nombre
        String[]elementos=new String[]{idVehiculo,marca,modelo,matricula,IdCliente};
        String[]nombreelementos=new String[]{"idVehiculo","Marca","Modelo","Matricula","IdCliente"};
        for (int i = 0; i < elementos.length; i++) {
            cv.put(nombreelementos[i],elementos[i]);
        }
    }

    private void buscar() {
        String idVehiculo=txtIdVehiculo.getText().toString();
        sqldb=MainActivity.toh.getReadableDatabase();
        Cursor c=sqldb.query("Vehiculos",new String[]
                        {"IdVehiculo","Marca","Modelo","Matricula","IdCliente"},
                "IdVehiculo=?",
                new String[]{idVehiculo},
                null,null,null);
        if (c.moveToNext()){
            //Insertar los datos del cursor en las cajas de texto
            txtMarca.setText(c.getString(1));
            txtModelo.setText(c.getString(2));
            txtMatricula.setText(c.getString(3));
            int pos=-1;
            try {
                pos=buscarEnQuePosicionDelCursorEsta(c.getInt(4));
            }
            catch (Exception e){
                mostrarMensaje(e.getMessage());
            }
            mostrarMensaje(String.valueOf(pos));
            spClientes.setSelection(pos);
            //Habilitar y deshabilitar los botones correspondientes
            btnAlta.setEnabled(false);
            btnBaja.setEnabled(true);
            btnModificacion.setEnabled(true);
        }
        else{
            mostrarMensaje("No se ha encontrado ese vehiculo");
            //Habilitar y deshabilitar los botones correspondientes
            btnAlta.setEnabled(true);
            btnBaja.setEnabled(false);
            btnModificacion.setEnabled(false);
        }
    }

    private int buscarEnQuePosicionDelCursorEsta(int idCliente) throws Exception {
        sqldb=MainActivity.toh.getReadableDatabase();
        Cursor c=sqldb.query("Clientes",new String[]{"idCliente as _id",},null,null,null,null,"IdCliente");
        c.moveToFirst();
        int i=0;
        while (c.isAfterLast()==false){
            if (c.getInt(0)==idCliente){
                return i;
            }
            c.moveToNext();
            i++;
        }
        throw new Exception("Se ha pasado de los limites al buscar");

    }

    private void mostrarMensaje(String s) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sqldb=MainActivity.toh.getReadableDatabase();
        Cursor c=sqldb.rawQuery("SELECT IdCliente FROM Clientes ORDER BY IdCliente",null);
        c.moveToPosition(position);
        idCliente=c.getString(0);
        Toast.makeText(this,idCliente,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}