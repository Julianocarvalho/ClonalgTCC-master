package clonalgtcc;

import java.util.ArrayList;

/**
 *
 * @author juliano
 */
public class Antigeno {

    private ArrayList<Double> vars = new ArrayList<>();
    private int rotulo;

    public Antigeno() {
    }

    public Antigeno(ArrayList<Double> vars, int rotulo) {
        this.vars = vars;
        this.rotulo = rotulo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
//        sb.append(vars.toString());
//        sb.append(", ");
        sb.append(rotulo);
        sb.append("\n");
        return sb.toString(); //To change body of generated methods, choose Tools | Templates.
    }
    /**
     * @return the rotulo
     */
    public int getRotulo() {
        return rotulo;
    }

    /**
     * @param rotulo the rotulo to set
     */
    public void setRotulo(int rotulo) {
        this.rotulo = rotulo;
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
