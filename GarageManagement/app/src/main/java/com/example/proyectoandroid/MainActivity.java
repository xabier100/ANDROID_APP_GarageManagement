package com.example.proyectoandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String NOMBRE_BD = "tallerDB";
    public static TallerOpenHelper toh;//Publico para que lo puedan haceder desde las otras clases

    Button btnMantenimientoClientes,btnMantenimientoFacturas,btnMantenimientoVehiculos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toh=new TallerOpenHelper(this, NOMBRE_BD,null,5);
        //Asignar a cada boton su vista
        btnMantenimientoClientes=findViewById(R.id.btnMantenimientoClientes);
        btnMantenimientoFacturas=findViewById(R.id.btnMantenimientoFacturas);
        btnMantenimientoVehiculos=findViewById(R.id.btnMantenimientoVehiculos);

        //Asignar los listener a los botones
        Button[] botones={btnMantenimientoClientes,btnMantenimientoFacturas,btnMantenimientoVehiculos};
        for (Button b : botones) {
            b.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        Intent i=null;
        if (v==btnMantenimientoClientes){
            i=new Intent(this,MantenimientoClientes.class);
        }
        else if (v==btnMantenimientoFacturas){
            i=new Intent(this,MantenimientoFacturas.class);
        }
        else if(v==btnMantenimientoVehiculos){
            i=new Intent(this,MantenimientoVehiculos.class);
        }
        startActivity(i);
    }
}