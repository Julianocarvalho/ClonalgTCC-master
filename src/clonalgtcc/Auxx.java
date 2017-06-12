/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clonalgtcc;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

/**
 *
 * @author joao
 */
public class Auxx {
    private Anticorpo anticorpo;
    private double dist;

    public Auxx() {
    }
    
    public Auxx(double dist) {
        this.dist = dist;
    }
    
    public Auxx(Anticorpo anticorpo, double dist) {
        this.anticorpo = anticorpo;
        this.dist = dist;
    }

    /**
     * @return the anticorpo
     */
    public Anticorpo getAnticorpo() {
        return anticorpo;
    }

    /**
     * @param anticorpo the anticorpo to set
     */
    public void setAnticorpo(Anticorpo anticorpo) {
        this.anticorpo = anticorpo;
    }

    /**
     * @return the dist
     */
    public double getDist() {
        return dist;
    }

    /**
     * @param dist the dist to set
     */
    public void setDist(double dist) {
        this.dist = dist;
    }
    
    public static void main(String[] args) {
        Random r = new Random();
        DecimalFormat df = new DecimalFormat("#.####");
//        ArrayList<Integer> nums = new ArrayList<>();
//        ArrayList<Integer> nums2 = new ArrayList<>();
//        for(int i =0;i<10;i++){
//            for(int j=0;j<15;j++){
//                System.out.print(df.format(r.nextFloat()) + " ");
//            }
//            System.out.println("");
//        }
//***********************************************************
//    ArrayList<Integer> l1 = new ArrayList<>();
//    ArrayList<Integer> l2 = new ArrayList<>();
//
//    l1.add(1);
//    l1.add(2);
//    l1.add(3);
//    l2.addAll(l1);
//    
//    l2.remove(0);
//    
//        for (Integer integer : l1) {
//            System.out.println(integer);
//        }
//        System.out.println("");
//        for (Integer integer : l2) {
//            System.out.println(integer);
//        }
    ArrayList<Double> valores = new ArrayList<>();
    int sum = 0;
        System.out.println("Valores: ");
        for (int i = 0; i < 15; i++) {
            valores.add((double)r.nextInt(30));
            sum+= valores.get(i);
            System.out.print(valores.get(i)+", ");
        }
        System.out.println("");
        
        double media = sum/valores.size();
        Clonalg clo = new Clonalg();
        
        System.out.println("Média: "+media+" Desvio: "+df.format(clo.desvioPadrao(valores, media)));
        
        
        


    }

}
