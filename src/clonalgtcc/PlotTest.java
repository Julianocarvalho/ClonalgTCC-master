/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clonalgtcc;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.text.DefaultCaret;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

/**
 *
 * @author joao
 */
public class PlotTest {
    
    
    private XYSeriesCollection dataset;

    public PlotTest (ArrayList<Anticorpo> iniciais, ArrayList<Antigeno> antigenos, ArrayList<Anticorpo> anticorpos, String it) {
        dataset = new XYSeriesCollection();
        XYSeries data1 = new XYSeries("Anticorpos");
//        XYSeries data2 = new XYSeries("Class2");
//        XYSeries data3 = new XYSeries("Class3");
//        XYSeries data4 = new XYSeries("Class4");
        XYSeries dataAnticorpos = new XYSeries("Antigenos");
        XYSeries dataInicial = new XYSeries("Pop. Inicial");
        
        for (Antigeno antigeno : antigenos) {
                data1.add(antigeno.getVars().get(0), antigeno.getVars().get(1));
                 }
        for(Anticorpo anticorpo : anticorpos){
            dataAnticorpos.add(anticorpo.getVars().get(0),anticorpo.getVars().get(1));
        }
        for(Anticorpo inicial : iniciais){
            dataInicial.add(inicial.getVars().get(0),inicial.getVars().get(1));
        }
        dataset.addSeries(dataAnticorpos);
//        dataset.addSeries(data2);
//        dataset.addSeries(data3);
//        dataset.addSeries(data4);
        dataset.addSeries(data1);
        dataset.addSeries(dataInicial);

        
        showGraph(String.valueOf(it));
    }
    
        public PlotTest (ArrayList<Antigeno> antigenos) {
        dataset = new XYSeriesCollection();
        // Pegando tamanaho base
        int tamanhoBase = 0;
            for (Antigeno antigeno : antigenos) {
                if(antigeno.getRotulo()>tamanhoBase){
                    tamanhoBase=antigeno.getRotulo();
                }
               
            }
        ArrayList<XYSeries> dados = new ArrayList<>();
            for (int i = 1; i < tamanhoBase+1; i++) {
                dados.add(new XYSeries("Classe "+i));
            }
        
            for (Antigeno ant : antigenos) {
                dados.get(ant.getRotulo()-1).add(ant.getVars().get(0), ant.getVars().get(1));
            }
            
            for (XYSeries dado : dados) {
                dataset.addSeries(dado);
            }
            
        showGraph("Teste");
    }
    private void showGraph(String it) {
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
//        chartPanel.setPreferredSize(new java.awt.Dimension(500, 380)); //270
        final ApplicationFrame frame = new ApplicationFrame("Dados "+it);
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }
    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart chart = ChartFactory.createScatterPlot(
            "BASE",                  // chart title
            "X",                      // x axis label
            "Y",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );
        XYPlot xyPlot = chart.getXYPlot();
        XYItemRenderer renderer = xyPlot.getRenderer();
 
//        renderer.setSeriesPaint( 4, Color.BLUE );
//        renderer.setSeriesShape(0, new DefaultCaret.Double(-3, -3, 8, 8));
//        renderer.setSeriesShape(1, new Ellipse2D.Double(-3, -3, 8, 8));
//        renderer.setSeriesShape(2, new Ellipse2D.Double(-3, -3, 8, 8));
//        renderer.setSeriesShape(3, new Ellipse2D.Double(-3, -3, 8, 8));
//        renderer.setSeriesShape(4, new Ellipse2D.Double(-3, -3, 8, 8));
        
 
        // Comentando essas linhas abaixo;
//        XYPlot plot = (XYPlot) chart.getPlot();
//        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
//        renderer.setSeriesLinesVisible(0, true);
//        plot.setRenderer(renderer);
        // Até aqui
        return chart;
    }
    public static void main(String[] args) {
//        Leitor leitor = new Leitor();
        ArrayList<Double> vars = new ArrayList<>();
        int rotulo =0;
        vars.add(5.12345);
        vars.add(6.12345);
        
        Antigeno antigeno1 = new Antigeno(vars,rotulo);
        Antigeno antigeno2 = new Antigeno(vars,rotulo);
        System.out.println(antigeno1.getVars()==antigeno2.getVars());
       // PlotTest plot = new PlotTest(antigenos);
        
    }
}
