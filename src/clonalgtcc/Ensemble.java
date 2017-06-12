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
 */
public class Ensemble {
    private ArrayList<Particao> particoes = new ArrayList<>();
    private float melhorlimiar;
    private ArrayList<Particao> elitismo = new ArrayList<>();
    private ArrayList<Particao> roleta = new ArrayList<>();
    
    public Ensemble(ArrayList<Particao> particoes, float melhorlimiar){
        this.particoes=particoes;
        this.melhorlimiar = melhorlimiar;
    }

    /**
     * @return the particoes
     */
    public ArrayList<Particao> getParticoes() {
        return particoes;
    }

    /**
     * @param particoes the particoes to set
     */
    public void setParticoes(ArrayList<Particao> particoes) {
        this.particoes = particoes;
    }

    /**
     * @return the melhorlimiar
     */
    public float getMelhorlimiar() {
        return melhorlimiar;
    }

    /**
     * @param melhorlimiar the melhorlimiar to set
     */
    public void setMelhorlimiar(float melhorlimiar) {
        this.melhorlimiar = melhorlimiar;
    }

    /**
     * @return the elitismo
     */
    public ArrayList<Particao> getElitismo() {
        return elitismo;
    }

    /**
     * @param elitismo the elitismo to set
     */
    public void setElitismo(ArrayList<Particao> elitismo) {
        this.elitismo = elitismo;
    }

    /**
     * @return the roleta
     */
    public ArrayList<Particao> getRoleta() {
        return roleta;
    }

    /**
     * @param roleta the roleta to set
     */
    public void setRoleta(ArrayList<Particao> roleta) {
        this.roleta = roleta;
    }

        
}
