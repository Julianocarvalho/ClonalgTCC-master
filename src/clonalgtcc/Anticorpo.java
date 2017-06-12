package clonalgtcc;

import java.util.ArrayList;

/**
 *
 * @author joao
 */
public class Anticorpo {

    private ArrayList<Double> vars = new ArrayList<>();
    private Antigeno antigeno; // armazena o antigeno de maior afinidade
    // A afinidade entre antígeno e anticorpo é dada pela distância Euclidiana normalizada (0,1].
    private double afinidade; // Distância euclidiana dividida pela soma de todas distâncias.

    // Para a normalização, fazer a distância do antigeno a todas os anticorpos e dividi-la pela maior distância.
    // Dividir a distância Euclidiana (entre objeto e protótipo) pela soma de todas as distâncias 
    // (entre objeto e todos os protótipos). Com isso temos uma distância relativa, que representa a afinidade 
    // relativa dos anticorpos (protótipos) a um antígeno (objeto). Assim, teremos distâncias (normalizadas) no 
    //intervalo (0,1].
    // Distância euclidiana = raiz((px-qx)^2 + (py - qy)^2).
    public Anticorpo() {
    }

    public Anticorpo(ArrayList<Double> vars, Antigeno antigeno, double afinidade) {
        this.vars = vars;
        this.antigeno = antigeno;
        this.afinidade = afinidade;
    }

    public Anticorpo(int tam) {
        for (int i = 0; i < tam; i++) {
            this.vars.add((double) 0);
        }
    }

    public Anticorpo(ArrayList<Double> vars) {
        this.vars = vars;
    }

    /**
     * @return the antigeno
     */
    public Antigeno getAntigeno() {
        return antigeno;
    }

    /**
     * @param antigeno the antigeno to set
     */
    public void setAntigeno(Antigeno antigeno) {
        this.antigeno = antigeno;
    }

    /**
     * @return the afinidade
     */
    public double getAfinidade() {
        return afinidade;
    }

    /**
     * @param afinidade the afinidade to set
     */
    public void setAfinidade(double afinidade) {
        this.afinidade = afinidade;
    }

//        public static void main(String[] args) {
//            Anticorpo a = new Anticorpo(0.1, 0.4);
//            Anticorpo a2 = new Anticorpo(1.0, 1.0);
//            Antigeno an = new Antigeno(0.0 , 0.0);
//            ArrayList<Anticorpo> array = new ArrayList<>();
//            array.add(a);
//            array.add(a2);
//            
//            a.atualizaAfi(array, an);
//    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("X: %f Y: %f, Afinidade: %f", getVars().get(0),getVars().get(1),afinidade));
//        if(antigeno != null){
//            sb.append(String.format("Geno: X: %f Y: %f\n", antigeno.getVars().get(0),antigeno.getVars().get(1)));
//        }
//        sb.append(vars.toString());
//        sb.append(", Afinidade: ");
//        sb.append(afinidade);
        sb.append("\n");
        return sb.toString(); //To change body of generated methods, choose Tools | Templates.
    }
    /**
     * @return the vars
     */
    public ArrayList<Double> getVars() {
        return vars;
    }

    /**
     * @param vars the vars to set
     */
    public void setVars(ArrayList<Double> vars) {
        this.vars = vars;
    }

}
