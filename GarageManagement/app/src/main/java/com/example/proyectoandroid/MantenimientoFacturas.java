package com.example.proyectoandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
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

public class MantenimientoFacturas extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Button btnAlta,btnBaja,btnModificacion,btnBuscar;
    EditText txtIdFactura, txtFechaReparacion, txtConcepto, txtImporte;
    SQLiteDatabase sqldb;
    MediaPlayer mpDanzaKuduro;
    String idCliente;
    Spinner spClientes;
    @Override
    public void onBackPressed() {
        mpDanzaKuduro.stop();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantenimiento_facturas);
        txtIdFactura =findViewById(R.id.txtIdFactura);
        txtFechaReparacion =findViewById(R.id.txtFechaReparacion);
        txtConcepto =findViewById(R.id.txtConcepto);
        txtImporte=findViewById(R.id.txtImporte);
        spClientes=findViewById(R.id.spClientes);
        spClientes.setOnItemSelectedListener(this);

        cargarSpinner(spClientes);

        mpDanzaKuduro = MediaPlayer.create(this,R.raw.fast_five_danza_kuduro);
        mpDanzaKuduro.start();
        AudioManager ad=(AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        int volumen=ad.getStreamMaxVolume(AudioManager.STREAM_MUSIC)/2;
        while (volumen>ad.getStreamVolume(AudioManager.STREAM_MUSIC)){
            ad.adjustVolume(AudioManager.ADJUST_RAISE,AudioManager.FLAG_PLAY_SOUND);
        }
        mpDanzaKuduro.setLooping(true);
        
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
        SimpleCursorAdapter sca=new SimpleCursorAdapter(this,R.layout.fila_sp_clientes,c,columnas,textViews, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
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

    private void modificar() {
        sqldb=MainActivity.toh.getWritableDatabase();
        String id= txtIdFactura.getText().toString();
        ContentValues cv=new ContentValues();
        llenarCv(cv);
        try {
            sqldb.update("Facturas",cv,"IdFactura=?",new String[]{id});
            mostrarMensaje("Factura modificada con exito");
        }
        catch (SQLiteConstraintException e){
            mostrarMensaje("Ha habido un error con el DNI");
        }
    }

    private void darDeBaja() {
        sqldb=MainActivity.toh.getWritableDatabase();
        String id= txtIdFactura.getText().toString();
        sqldb.delete("Facturas","IdFactura=?",new String[]{id});
        btnBaja.setEnabled(false);
        btnModificacion.setEnabled(false);
        btnAlta.setEnabled(true);
    }

    private void darDeAlta() {
        sqldb=MainActivity.toh.getWritableDatabase();
        ContentValues cv=new ContentValues();
        llenarCv(cv);
        try {
            sqldb.insertOrThrow("Facturas",null,cv);
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
        String idFactura= txtIdFactura.getText().toString();
        String fechaReparacion=txtFechaReparacion.getText().toString();
        String concepto=txtConcepto.getText().toString();
        String importe=txtImporte.getText().toString();
        //Llenar el contentValue con el valor de la caja y su nombre
        String[]elementos=new String[]{idFactura,idCliente,fechaReparacion,concepto,importe};
        String[]nombreelementos=new String[]{"IdFactura","IdCliente","FechaReparacion","Concepto","Importe"};
        for (int i = 0; i < elementos.length; i++) {
            cv.put(nombreelementos[i],elementos[i]);
        }
    }

    private void buscar() {
        String idFactura= txtIdFactura.getText().toString();
        sqldb=MainActivity.toh.getReadableDatabase();
        Cursor c=sqldb.query("Facturas",new String[]
                        {"IdFactura","IdCliente","FechaReparacion","Concepto","Importe"},
                "IdFactura=?",
                new String[]{idFactura},
                null,null,null);
        if (c.moveToNext()){
            //Insertar los datos del cursor en las cajas de texto
            int pos=-1;
            try {
                pos=buscarEnQuePosicionDelCursorEsta(c.getInt(1));
            }
            catch (Exception e){
                mostrarMensaje(e.getMessage());
            }
            spClientes.setSelection(pos);
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
        idCliente=String.valueOf(c.getInt(0));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}