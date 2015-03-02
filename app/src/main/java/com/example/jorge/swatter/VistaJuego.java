package com.example.jorge.swatter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by Jorge on 25/02/2015.
 */
public class VistaJuego extends SurfaceView implements SurfaceHolder.Callback {

    private Bitmap bmpMosca, bmpAvispa, bmpFondo, bmpFinal;
    private int alto, ancho;
    private HebraJuego hebraJuego;
    private AnadirAvispas hebraAAvispas;
    private AnadirMoscas hebraAMoscas;
    private ArrayList<Figura> figurasMoscas;
    private ArrayList<Figura> figurasAvispas;
    private Context context;
    private int puntuacion = 0;
    private int vidas = 3;

    /**********************************************************************************************/
    /*                                      constructor                                           */
    /**********************************************************************************************/

    public VistaJuego(Context contexto) {
        super(contexto);
        context = contexto;
        getHolder().addCallback(this);
        bmpFondo = BitmapFactory.decodeResource(getResources(), R.drawable.fondo);
        bmpMosca = BitmapFactory.decodeResource(getResources(), R.drawable.mosca);
        bmpAvispa = BitmapFactory.decodeResource(getResources(), R.drawable.avispa);
        bmpFinal = BitmapFactory.decodeResource(getResources(), R.drawable.finpartida);
        hebraJuego = new HebraJuego(this);
        hebraAAvispas = new AnadirAvispas(this);
        hebraAMoscas = new AnadirMoscas(this);
        figurasMoscas = new ArrayList<Figura>();
        figurasAvispas = new ArrayList<Figura>();
        for (int i = 0; i < 10; i++) {
            figurasMoscas.add(new Figura(bmpMosca));
        }
        for (int i = 0; i < 2; i++) {
            figurasAvispas.add(new Figura(bmpAvispa));
        }
    }

    /**********************************************************************************************/
    /*                              Metodo de la clase surfaceview                                */
    /**********************************************************************************************/

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        bmpFondo = redimensionarFondo(bmpFondo, getWidth(), getHeight());
        canvas.drawBitmap(bmpFondo, 0, 0, null);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        canvas.drawText("Puntuacion: " + puntuacion, getWidth() - 250, 35, paint);
        canvas.drawText("Vidas: " + vidas, getWidth() - 250, 70, paint);
        for (Figura f : figurasMoscas) {
            f.dibujar(canvas);
        }
        for (Figura f : figurasAvispas) {
            f.dibujar(canvas);
        }
        if (vidas == 0){
            partidaAcabada(canvas);
        }
    }

    /**********************************************************************************************/
    /*                                   Interfaz callback                                        */
    /**********************************************************************************************/

    @Override
    public void surfaceCreated(SurfaceHolder holder) {//No cambia con o si sprite
        hebraJuego.setFuncionando(true);
        hebraAAvispas.setFuncionando(true);
        hebraAMoscas.setFuncionando(true);
        hebraJuego.start();
        hebraAAvispas.start();
        hebraAMoscas.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        alto = height;
        ancho = width;
        Figura.setDimension(ancho, alto);
        for (int i = 0; i < figurasMoscas.size(); i++) {
            figurasMoscas.get(i).setPosicionAleatorio();
            for (int j = 0; j < i - 1; j++) {
                figurasMoscas.get(i).setPosicionAleatorio();
            }
        }
        for (int i = 0; i < figurasAvispas.size(); i++) {
            figurasAvispas.get(i).setPosicionAleatorio();
            for (int j = 0; j < i - 1; j++) {
                figurasAvispas.get(i).setPosicionAleatorio();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean reintentar = true;
        hebraJuego.setFuncionando(false);
        hebraAAvispas.setFuncionando(false);
        hebraAMoscas.setFuncionando(false);
        while (reintentar) {
            try {
                hebraJuego.join();
                reintentar = false;
            } catch (InterruptedException e) {
            }
        }
    }

    private long ultimoClick = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - ultimoClick > 300) {
            ultimoClick = System.currentTimeMillis();
            float x, y;
            x = event.getX();
            y = event.getY();
            synchronized (getHolder()) {
                for (final Figura fMosca : figurasMoscas) {
                    if (fMosca.tocado(x, y)) {
                        fMosca.parar(x, y);
                        fMosca.cambiarMosca(context);
                        puntuacion = puntuacion + 10;
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                figurasMoscas.remove(fMosca);
                            };
                        }, 1000);
                        break;
                    }
                }
                for (final Figura fAvispa : figurasAvispas) {
                    if (fAvispa.tocado(x, y)) {
                        fAvispa.parar(x, y);
                        fAvispa.cambiarAvispa(context);
                        vidas = vidas-1;
                        //Aumenta la velocidad de las moscas
                        for (int i = 0; i < figurasMoscas.size(); i++) {
                            figurasMoscas.get(i).aumentarVelocidad();
                        }
                        //Devuelve las moscas a la velocidad normal
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                for (int i = 0; i < figurasMoscas.size(); i++) {
                                    figurasMoscas.get(i).setMovimiento();
                                }
                                figurasAvispas.remove(fAvispa);
                            };
                        }, 3000);
                        break;
                    }
                }
            }
        }
        return true;
    }

    /**********************************************************************************************/
    /*                              Metodos personalizados                                        */
    /**********************************************************************************************/
    public Bitmap redimensionarFondo(Bitmap mBitmap, float newWidth, float newHeigth) {
        //Redimensionamos el fondo
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeigth) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
    }

    public void anadirAvispas() {//Añade avispas cada 10 segundos.
        for (int i = 0; i < 2; i++) {
            figurasAvispas.add(new Figura(bmpAvispa));
        }
    }

    public void anadirMoscas() {//Añade moscas cada 2 segundos.
        for (int i = 0; i < 6; i++) {
            figurasMoscas.add(new Figura(bmpMosca));
        }
    }

    //Cuando la partida finaliza
    public void partidaAcabada(Canvas canvas){
        int anchoFondo = (getWidth()/2)-bmpFinal.getWidth()/2;
        int altoFondo = (getHeight()/2)-bmpFinal.getHeight()/2;
        canvas.drawBitmap(bmpFinal, anchoFondo, altoFondo,null);
        Paint puntosFinales = new Paint();
        puntosFinales.setColor(Color.BLACK);
        puntosFinales.setTextSize(50);
        int anchoTexto = (getWidth()/2)-200;
        int altoTexto = (getHeight()/2)+150;
        canvas.drawText("Puntuacion: " + puntuacion, anchoTexto, altoTexto, puntosFinales);
        hebraJuego.setFuncionando(false);
        hebraAAvispas.setFuncionando(false);
        hebraAMoscas.setFuncionando(false);
    }
}
