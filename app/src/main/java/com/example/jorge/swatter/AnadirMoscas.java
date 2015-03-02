package com.example.jorge.swatter;

/**
 * Created by Jorge on 01/03/2015.
 */
public class AnadirMoscas extends Thread {
    private VistaJuego vista;
    private boolean funcionando = false;

    public AnadirMoscas(VistaJuego vj) {
        this.vista = vj;
    }
    public void setFuncionando(boolean f) {
        funcionando = f;
    }

    @Override
    public void run() {
        while (funcionando){
            vista.anadirMoscas();
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
