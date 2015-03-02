package com.example.jorge.swatter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class Portada extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_portada);

        new Handler().postDelayed(new Runnable(){
            public void run(){
                Intent intent = new Intent(Portada.this, Principal.class);
                startActivity(intent);
                finish();
            };
        }, 5000);
    }
}
