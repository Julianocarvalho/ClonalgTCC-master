/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clonalgtcc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author joao
 */
public class Normalizador {
    public void normaliza(){
        ArrayList<String> linhas = new ArrayList<>();
        ArrayList<Antigeno> antigenos = new ArrayList<>();
        String nome = "soybean_smallNaoPro.txt";
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
                String[] separa = linha.split(";");
                ArrayList<Double> vars = new ArrayList<>();
                for(int i=0;i<separa.length;i++){
                    vars.add(Double.parseDouble(separa[i]));
                }
                Antigeno ant = new Antigeno(vars,0);
                antigenos.add(ant);
            }
        // Todos dados não normalizados estão dentro do array de antigenos
        // Encontrar mim e max
//        double min=Double.MAX_VALUE, max=0;
        ArrayList<Double> max = new ArrayList<>();
        ArrayList<Double> min = new ArrayList<>();
        for(int i=0;i<antigenos.get(0).getVars().size();i++){
            max.add(0.0);
            min.add(Double.MAX_VALUE);
        }
        
        for (Antigeno antigeno : antigenos) {
            for(int i=0;i<antigenos.get(0).getVars().size();i++){
                if(antigeno.getVars().get(i)>max.get(i)){
                    max.set(i, antigeno.getVars().get(i));
                }
                if(antigeno.getVars().get(i) < min.get(i)){
                    min.set(i, antigeno.getVars().get(i));
                }
            }
//            for(Double var : antigeno.getVars()){
//                if(var > max){
//                    max = var;
//                }
//                if(var<min){
//                    min = var;
//                }
//            }
        }
//        System.out.println("Min: "+min+" Máx: "+max);
        // Normalizar tudo
        for (Antigeno antigeno : antigenos) {
            for(int i=0;i<antigeno.getVars().size();i++){
//              afinidade = (aux.getDist() - min) / (max - min);
//                antigeno.getVars().set(i, (antigeno.getVars().get(i)-min)/(max-min));
                antigeno.getVars().set(i, (antigeno.getVars().get(i)-min.get(i))/(max.get(i)-min.get(i)));
            }
        }
        DecimalFormat df = new DecimalFormat("#.####");
        // Imprimir base normalizada
        for (Antigeno antigeno : antigenos) {
            for(Double var : antigeno.getVars()){
                System.out.print(df.format(var)+" ");
            }
            System.out.println("");
        }
        
        
        
    }
    
    
    public static void main(String[] args) {
        Normalizador n = new Normalizador();
        n.normaliza();
    }
}
