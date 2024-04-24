/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.meteo;

import java.awt.EventQueue;
import javax.swing.JFrame;

/**
 *
 * @author utilisateur
 */
public class Meteo {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable(){
            public void run(){
                System.out.println("Hello World!");
               MeteoFrame frame= new MeteoFrame("Meteo");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame. setLocationRelativeTo(null);
                frame.setSize(400, 400);
                frame.setVisible(true);
            }
        });

    }
}
