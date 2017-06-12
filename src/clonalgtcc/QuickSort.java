/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clonalgtcc;

import java.util.ArrayList;

/**
 * Forma Crescente
 * @author joao
 */
public class QuickSort {
    public QuickSort() {
    }
    
    public static void quickSortErro(ArrayList<Particao> particoes){
        int inicio = 0; int fim = particoes.size()-1;
        quickSortErro(particoes, inicio, fim);
    }
        
    private static void quickSortErro(ArrayList<Particao> particoes, int inicio, int fim) {
        if (inicio < fim) {
            int posicaoPivo = separarErro(particoes, inicio, fim);
            quickSortErro(particoes, inicio, posicaoPivo - 1);
            quickSortErro(particoes, posicaoPivo + 1, fim);
        }
    }
    private static int separarErro(ArrayList<Particao> particoes, int inicio, int fim) {
        Particao pivo = particoes.get(inicio);
        int i = inicio + 1, f = fim;
        while (i <= f) {
            if (particoes.get(i).getErroQ() <= pivo.getErroQ()) {
                i++;
            } else if (pivo.getErroQ() < particoes.get(f).getErroQ()) {
                f--;
            } else {
                Particao troca = particoes.get(i);
                particoes.set(i, particoes.get(f));
                particoes.set(f, troca);
                i++;
                f--;
            }
        }
        particoes.set(inicio, particoes.get(f));
        particoes.set(f, pivo);
        return f;
    }
    
    public void quickSort(ArrayList<Anticorpo> vetor){
        int inicio = 0; int fim = vetor.size()-1;
        quickSort(vetor, inicio, fim);
    }
    private void quickSort(ArrayList<Anticorpo> vetor, int inicio, int fim) {
        if (inicio < fim) {
            int posicaoPivo = separar(vetor, inicio, fim);
            quickSort(vetor, inicio, posicaoPivo - 1);
            quickSort(vetor, posicaoPivo + 1, fim);
        }
    }

    private int separar(ArrayList<Anticorpo> vetor, int inicio, int fim) {
        Anticorpo pivo = vetor.get(inicio);
        int i = inicio + 1, f = fim;
        while (i <= f) {
            if (vetor.get(i).getAfinidade() <= pivo.getAfinidade()) {
                i++;
            } else if (pivo.getAfinidade() < vetor.get(f).getAfinidade()) {
                f--;
            } else {
                Anticorpo troca = vetor.get(i);
                vetor.set(i, vetor.get(f));
                vetor.set(f, troca);
                i++;
                f--;
            }
        }
        vetor.set(inicio, vetor.get(f));
        vetor.set(f, pivo);
        return f;
    }
//    public static void main(String[] args) {
//        Anticorpo a = new Anticorpo(null, null, 5.0);
//        Anticorpo a2 = new Anticorpo(null, null, 2.0);
//        ArrayList<Anticorpo> ants = new ArrayList<>();
//        ants.add(a);
//        ants.add(a2);
//        QuickSort q = new QuickSort();
//        q.quickSort(ants);
//        for (Anticorpo ant : ants) {
//            System.out.println(ant.getAfinidade());
//        }
//    }

}
