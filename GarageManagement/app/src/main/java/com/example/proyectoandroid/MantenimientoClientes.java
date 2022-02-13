package com.example.proyectoandroid;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MantenimientoClientes extends AppCompatActivity implements View.OnClickListener{

    Button btnAlta,btnBaja,btnModificacion,btnBuscar;
    EditText txtIdCliente,txtNombre,txtApellido,txtDni;
    SQLiteDatabase sqldb;
    ImageView imgCliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantenimiento_clientes);
        txtIdCliente=findViewById(R.id.txtIdCliente);
        txtNombre=findViewById(R.id.txtMarca);
        txtApellido=findViewById(R.id.txtConcepto);
        txtDni=findViewById(R.id.txtDni);
        btnAlta=findViewById(R.id.btnAlta);
        btnBaja=findViewById(R.id.btnBaja);
        btnModificacion=findViewById(R.id.btnModificacion);
        btnBuscar=findViewById(R.id.btnBuscar);
        imgCliente=findViewById(R.id.imgCliente);
        btnAlta.setOnClickListener(this);
        btnBaja.setOnClickListener(this);
        btnModificacion.setOnClickListener(this);
        btnBuscar.setOnClickListener(this);
        imgCliente.setOnClickListener(this);

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
        else if (v==imgCliente){
            Intent i=new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i,1);
        }
    }

    private void modificar() {
        sqldb=MainActivity.toh.getWritableDatabase();
        String id=txtIdCliente.getText().toString();
        ContentValues cv=new ContentValues();
        llenarCv(cv);
        try {
            sqldb.update("Clientes",cv,"IdCliente=?",new String[]{id});
            Toast.makeText(this,"Cliente modificado con exito",Toast.LENGTH_LONG).show();
        }
        catch (SQLiteConstraintException e){
            Toast.makeText(this,"Ha habido un error con el DNI",Toast.LENGTH_LONG).show();
        }
    }

    private void darDeBaja() {
        sqldb=MainActivity.toh.getWritableDatabase();
        String id=txtIdCliente.getText().toString();
        sqldb.delete("Clientes","IdCliente=?",new String[]{id});
        btnBaja.setEnabled(false);
        btnModificacion.setEnabled(false);
        btnAlta.setEnabled(true);
    }

    private void darDeAlta() {
        sqldb=MainActivity.toh.getWritableDatabase();
        ContentValues cv=new ContentValues();
        llenarCv(cv);
        try {
            sqldb.insertOrThrow("Clientes",null,cv);
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
        //Obtener los valores de cada caja de texto
        String idCliente=txtIdCliente.getText().toString();
        String nombre=txtNombre.getText().toString();
        String apellido=txtApellido.getText().toString();
        String dni=txtDni.getText().toString();
        String img=this.convertirABase64(imgCliente);
        //Llenar el contentValue con el valor de la caja y su nombre
        String[]elementos=new String[]{idCliente,nombre,apellido,dni,img};
        String[]nombreelementos=new String[]{"idCliente","nombre","apellido","DNI","ImgCliente"};
        for (int i = 0; i < elementos.length; i++) {
            cv.put(nombreelementos[i],elementos[i]);
        }
    }

    private void buscar() {
        String idCliente=txtIdCliente.getText().toString();
        sqldb=MainActivity.toh.getReadableDatabase();
        Cursor c=sqldb.query("Clientes",new String[]
                        {"IdCliente","Nombre","Apellido","DNI","ImgCliente"},
                "IdCliente=?",
                new String[]{idCliente},
                null,null,null);
        if (c.moveToNext()){
            //Insertar los datos del cursor en las cajas de texto
            txtNombre.setText(c.getString(1));
            txtApellido.setText(c.getString(2));
            txtDni.setText(c.getString(3));
            //Crear el bitmpap y insertarlo
            Bitmap bmp=this.obtenerBmpDeString(c.getString(4));
            imgCliente.setImageBitmap(bmp);
            //Habilitar y deshabilitar los botones correspondientes
            btnAlta.setEnabled(false);
            btnBaja.setEnabled(true);
            btnModificacion.setEnabled(true);
        }
        else{
            mostrarMensaje("No se ha encontrado ese cliente");
            //Habilitar y deshabilitar los botones correspondientes
            btnAlta.setEnabled(true);
            btnBaja.setEnabled(false);
            btnModificacion.setEnabled(false);
        }
    }

    private Bitmap obtenerBmpDeString(String string) {
        byte [] array=Base64.decode(string,Base64.DEFAULT);
        Bitmap bmp= BitmapFactory.decodeByteArray(array,0,array.length);
        return bmp;
    }



    private void mostrarMensaje(String s) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    public String convertirABase64(ImageView img){
        Bitmap bmp=((BitmapDrawable)img.getDrawable()).getBitmap();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte []array=baos.toByteArray();
        String strfoto= Base64.encodeToString(array,Base64.DEFAULT);
        return strfoto;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1)
        {
            if(resultCode==RESULT_OK) {
                Uri uri = data.getData();
                try {
                    ContentResolver cr = getContentResolver();
                    /*Un manejador de imagenes es el cr*/
                    Bitmap bmp = ImageDecoder.decodeBitmap(ImageDecoder.createSource(cr, uri));
                    imgCliente.setImageBitmap(bmp);

                } catch (Exception e) {
                    mostrarMensaje(e.getMessage());
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}