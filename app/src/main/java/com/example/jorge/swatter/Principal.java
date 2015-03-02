package com.example.jorge.swatter;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class Principal extends Activity {

    private VistaJuego vjuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vjuego = new VistaJuego(this);
        setContentView(vjuego);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        vjuego = new VistaJuego(this);
        setContentView(vjuego);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
