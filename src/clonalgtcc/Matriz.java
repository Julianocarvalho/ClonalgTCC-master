/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clonalgtcc;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * i = Protótipos j = Objetos
 *
 * @author joao
 */
public class Matriz {
//    ArrayList<ArrayList> matriz = new ArrayList<>();

    private ArrayList<Anticorpo> anticorpos;
    private ArrayList<Antigeno> antigenos;
    private int[][] matriz;

    DecimalFormat df = new DecimalFormat("#.##");

    public Matriz() {
    }

    public Matriz(ArrayList<Anticorpo> anticorpos, ArrayList<Antigeno> antigenos) {
        this.anticorpos = anticorpos;
        this.antigenos = antigenos;
    }

    public int[][] criaMatriz(boolean imprimir) {
        setMatriz(new int[getAntigenos().size()][getAnticorpos().size()]);
        int linha = 0; // linha
        for (Antigeno antigeno : getAntigenos()) {
            double melhorDist = Double.MAX_VALUE;
//            Anticorpo melhorAnticorpo;
            int cont = 0;
            int coluna = 0;
            for (Anticorpo anticorpo : getAnticorpos()) {
                // calcular a distância de todos anticorpos em relação ao antigeno, e marcar o mais próximo
                double soma = 0;
                for (int v = 0; v < anticorpo.getVars().size(); v++) {
                    // distância eucliadina
                    soma += Math.pow(antigeno.getVars().get(v) - anticorpo.getVars().get(v), 2);
                }
                double distEuclidiana = Math.sqrt(soma);

//                double distEuclidiana = Math.sqrt(Math.pow(antigeno.getX() - anticorpo.getX(), 2) + Math.pow(antigeno.getY() - anticorpo.getY(), 2)); // distância euclidiana
                if (distEuclidiana < melhorDist) {
                    melhorDist = distEuclidiana;
//                    melhorAnticorpo = anticorpo;
                    coluna = cont;
                }
                cont++;
            }
            getMatriz()[linha][coluna] = 1;
            coluna = 0;
            linha++;
        }

        if (imprimir) {
            //Imprime matriz
            System.out.println("Matriz (Antigenos/Anticorpos): ");
            System.out.print("\n\t");
            for (int v = 0; v < antigenos.get(0).getVars().size(); v++) {
                for (Anticorpo anticorpo : getAnticorpos()) {
                    System.out.print("\tX:" + df.format(anticorpo.getVars().get(v)) + " ");
                }
                System.out.print("\n\t");
            }
            System.out.println("");
//            for (Anticorpo anticorpo : getAnticorpos()) {
//                System.out.print("\tX:" + df.format(anticorpo.getX()) + " ");
//            }
//            System.out.print("\n\t");
//            for (Anticorpo anticorpo : getAnticorpos()) {
//                System.out.print("\tY:" + df.format(anticorpo.getY()) + " ");
//            }
//            System.out.println("");
            ArrayList<Integer> somas = new ArrayList<>();
            for (int i = 0; i < getAnticorpos().size(); i++) {
                somas.add(0);
            }
            // Imprime dados
            for (int i = 0; i < getAntigenos().size(); i++) {
                for (int v = 0; v < antigenos.get(0).getVars().size(); v++) {
                    System.out.print("Var"+v+": "+df.format(getAntigenos().get(i).getVars().get(v))+" \t");
                }
//                System.out.print("X:" + df.format(getAntigenos().get(i).getX()) + "\tY:" + df.format(getAntigenos().get(i).getY()) + "\t ");
               
                for (int j = 0; j < getAnticorpos().size(); j++) {
                    System.out.print(getMatriz()[i][j] + "\t");
                    if (getMatriz()[i][j] == 1) {
                        somas.set(j, somas.get(j) + 1);
                    }
                }
                System.out.println("");
            }

            //Imprimir linha final
            System.out.print("\t\t\t\t");
            for (Integer soma : somas) {
                System.out.print(soma + "\t");
            }
            System.out.println("");
        }
        return getMatriz();
    }

        public ArrayList<Integer> pegasoma() {
        setMatriz(new int[getAntigenos().size()][getAnticorpos().size()]);
        int linha = 0; // linha
        for (Antigeno antigeno : getAntigenos()) {
            double melhorDist = Double.MAX_VALUE;
//            Anticorpo melhorAnticorpo;
            int cont = 0;
            int coluna = 0;
            for (Anticorpo anticorpo : getAnticorpos()) {
                // calcular a distância de todos anticorpos em relação ao antigeno, e marcar o mais próximo
                double soma = 0;
                for (int v = 0; v < anticorpo.getVars().size(); v++) {
                    // distância eucliadina
                    soma += Math.pow(antigeno.getVars().get(v) - anticorpo.getVars().get(v), 2);
                }
                double distEuclidiana = Math.sqrt(soma);

//                double distEuclidiana = Math.sqrt(Math.pow(antigeno.getX() - anticorpo.getX(), 2) + Math.pow(antigeno.getY() - anticorpo.getY(), 2)); // distância euclidiana
                if (distEuclidiana < melhorDist) {
                    melhorDist = distEuclidiana;
//                    melhorAnticorpo = anticorpo;
                    coluna = cont;
                }
                cont++;
            }
            getMatriz()[linha][coluna] = 1;
            coluna = 0;
            linha++;
        }


            ArrayList<Integer> somas = new ArrayList<>();
            for (int i = 0; i < getAnticorpos().size(); i++) {
                somas.add(0);
            }
            // Imprime dados
            for (int i = 0; i < getAntigenos().size(); i++) {
                for (int j = 0; j < getAnticorpos().size(); j++) {
                    if (getMatriz()[i][j] == 1) {
                        somas.set(j, somas.get(j) + 1);
                    }
                }
            }

        
        return somas;
    }
    
    /**
     * @return the anticorpos
     */
    public ArrayList<Anticorpo> getAnticorpos() {
        return anticorpos;
    }

    /**
     * @param anticorpos the anticorpos to set
     */
    public void setAnticorpos(ArrayList<Anticorpo> anticorpos) {
        this.anticorpos = anticorpos;
    }

    /**
     * @return the antigenos
     */
    public ArrayList<Antigeno> getAntigenos() {
        return antigenos;
    }

    /**
     * @param antigenos the antigenos to set
     */
    public void setAntigenos(ArrayList<Antigeno> antigenos) {
        this.antigenos = antigenos;
    }

    /**
     * @return the matriz
     */
    public int[][] getMatriz() {
        return matriz;
    }

    /**
     * @param matriz the matriz to set
     */
    public void setMatriz(int[][] matriz) {
        this.matriz = matriz;
    }

}
