/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.meteo;

import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

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
               
                frame.setSize(400, 400);
                frame.setVisible(true);
                frame.getContentPane().setBackground(Color.blue);
                
                ImageIcon icon =new ImageIcon("C:\\Users\\utilisateur\\Desktop\\ApplicationMeteoJava\\Meteo\\src\\main\\java\\Data\\meteo.png");
                frame.setIconImage(icon.getImage());
                        
                
                
                
                
            }
        });

    }
}
