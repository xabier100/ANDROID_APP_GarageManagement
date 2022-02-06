package com.example.proyectoandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static final String NOMBRE_BD = "tallerDB";
    public static TallerOpenHelper toh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toh=new TallerOpenHelper(this, NOMBRE_BD,null,1);
    }
}