/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clonalgtcc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author juliano
 */
public class Leitor {
    public static ArrayList<Antigeno> leAntigenos(){
        ArrayList<String> linhas = new ArrayList<>();
        ArrayList<Antigeno> antigenos = new ArrayList<>();
        //bases                         D       G
        //irisN                         4       3
        //balanceN                      4       3  
        //ruspiniN                      2       4
        //glassN                        9       6
        //habermanN                     3       2
        //liverN                        6       2
        //wineN                         10      3
        //wisconsinoriginalN            9       2
        //sinteticaAN                   2       4
        //sinteticaBN                   2       6
        //sinteticaCN                   5       3
        //sinteticaDN                   10      2
        //sinteticaEN                   11      4
        
                
        String nome = "ruspiniN.txt";
        //String nome = "iris.txt";
        try {
            FileReader arq = new FileReader(nome);
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine(); 
            while (linha != null) { // adicionando todas linhas do arquivo no array linhas.
                linhas.add(linha);
                linha = lerArq.readLine();
            }
            arq.close();
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }
        

        // Separa os X e Y e cria os antigenos
            for (String linha : linhas) {
                String[] separa = linha.split(" ");
                ArrayList<Double> vars = new ArrayList<>(); //vars(0) X; vars(1) Y
                for(int i=0;i<separa.length-1;i++){
                    vars.add(Double.parseDouble(separa[i]));
                }
                Antigeno ant = new Antigeno(vars, Integer.parseInt(separa[separa.length-1]));
                antigenos.add(ant);
            }
            return antigenos;
    }
    public static void main(String[] args) {
        Random r = new Random();
//        Leitor l = new Leitor();
//        ArrayList<Antigeno> antigenos = l.leAntigenos();
////        System.out.println(l.leAntigenos().toString());
//        for (Antigeno antigeno : antigenos) {
//            System.out.println("X: "+antigeno.getVars().get(0)+" Y: "+antigeno.getVars().get(1)+ 
//                    " Roluto: "+ antigeno.getRotulo());
//        }
        for (int i = 0; i < 100; i++) {
            System.out.println(r.nextDouble()*5);
        }
    }
}

