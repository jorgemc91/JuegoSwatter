package com.example.jorge.swatter;

/**
 * Created by Jorge on 01/03/2015.
 */
public class AnadirAvispas extends Thread {
    private VistaJuego vista;
    private boolean funcionando = false;

    public AnadirAvispas(VistaJuego vj) {
        this.vista = vj;
    }
    public void setFuncionando(boolean f) {
        funcionando = f;
    }

    @Override
    public void run() {
        while (funcionando){
            vista.anadirAvispas();
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
