/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dp.poid.method.utils;

import javax.swing.JFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author Daniel
 */
public class ChartDrawer extends JFrame{
    public static void drawChart(JFreeChart chart){
        ChartDrawer cd = new ChartDrawer();
        ChartPanel cp = new ChartPanel(chart);
        cd.setContentPane(cp);
        cd.setDefaultCloseOperation(EXIT_ON_CLOSE);
        cd.pack();
        cd.setVisible(true);
    }

}
