/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clonalgtcc;

import java.util.ArrayList;

/**
 *
 * @author Juliano
 * classe possuir 4 atributos de cada solucao
 *  anticorpos (prototipos)
 *  erro quadratico
 *  limiar
 *  pcc
 */
public class Particao {
   private ArrayList<Anticorpo> prototipos = new ArrayList<>();
   private double erroQ;
   private double limiar;
   private double pcc;
   
   public Particao(ArrayList<Anticorpo> prototipos, double erroQ, double limiar, 
           double pcc){
       this.prototipos = prototipos;
       this.erroQ = erroQ;
       this.limiar = limiar;
       this.pcc = pcc;
   }

    /**
     * @return the prototipos
     */
    public ArrayList<Anticorpo> getPrototipos() {
        return prototipos;
    }

    /**
     * @param prototipos the prototipos to set
     */
    public void setPrototipos(ArrayList<Anticorpo> prototipos) {
        this.prototipos = prototipos;
    }

    /**
     * @return the erroQ
     */
    public double getErroQ() {
        return erroQ;
    }

    /**
     * @param erroQ the erroQ to set
     */
    public void setErroQ(double erroQ) {
        this.erroQ = erroQ;
    }

    /**
     * @return the limiar
     */
    public double getLimiar() {
        return limiar;
    }

    /**
     * @param limiar the limiar to set
     */
    public void setLimiar(double limiar) {
        this.limiar = limiar;
    }

    /**
     * @return the pcc
     */
    public double getPcc() {
        return pcc;
    }

    /**
     * @param pcc the pcc to set
     */
    public void setPcc(double pcc) {
        this.pcc = pcc;
    }
}
