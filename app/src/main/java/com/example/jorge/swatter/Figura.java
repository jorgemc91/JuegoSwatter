package com.example.jorge.swatter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by Jorge on 25/02/2015.
 */
public class Figura {

    private int ejeY = 0;
    private int direccionY;
    private int ejeX = 0;
    private int direccionX;
    private float ejeXFijo = 0.0f, ejeYFijo = 0.0f;
    private Bitmap bmp;
    private int alto, ancho;
    private static int anchoMax=0, altoMax=0;


    public Figura(Bitmap bmp) {
        this.bmp = bmp;
        this.ancho=bmp.getWidth();
        this.alto=bmp.getHeight();
        setMovimiento();
        setPosicion();
    }

    public static void setDimension(int ancho, int alto){
        anchoMax=ancho;
        altoMax=alto;
    }

    public void setPosicion(){
        ejeX = 0;
        ejeY = 0;
        ejeXFijo = 0.0f;
        ejeYFijo = 0.0f;
    }

    public void setPosicionAleatorio(){
        Random rnd = new Random();
        ejeX = ancho + rnd.nextInt(anchoMax  - ancho*2);
        ejeY = alto + rnd.nextInt(altoMax- alto*2) ;
    }

    public void setMovimiento(){
        Random rnd = new Random();
        direccionX=rnd.nextInt(12)-5;
        if(direccionX==0)
            direccionX=2;
        direccionY=rnd.nextInt(12)-5;
        if(direccionY==0)
            direccionY=2;

    }

    //Aumenta la velocidad de las moscas.
    public void aumentarVelocidad(){
        direccionX = 30;
        direccionY = 30;
    }

    public void dibujar(Canvas canvas) {
        movimiento();
        canvas.drawBitmap(bmp, ejeX, ejeY, null);
    }

    private void movimiento(){
        if (ejeX > anchoMax - ancho - direccionX ||
                ejeX + direccionX < 0) {
            direccionX = -direccionX;
        }
        ejeX = ejeX + direccionX;
        if (ejeY > altoMax - alto - direccionY ||
                ejeY + direccionY < 0) {
            direccionY = -direccionY;
        }
        ejeY = ejeY + direccionY;
    }

    public boolean tocado(float x, float y){
        return x > ejeX && x < ejeX + ancho && y > ejeY && y < ejeY + alto;
    }

    //Detiene el elemento, en la posicion que lo hemos tocado.
    public void parar(float x, float y){
        direccionX=0;
        direccionY=0;
        ejeXFijo=x;
        ejeYFijo=y;
    }

    //Cambia la imagen de la mosca
    public void cambiarMosca(Context context){
        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.moscamuerte);
    }

    //Cambia la imagen de la avispa
    public void cambiarAvispa(Context context){
        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.avispamuerte);
    }
}
