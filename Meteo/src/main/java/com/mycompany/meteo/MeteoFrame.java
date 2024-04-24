/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.meteo;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

// Pour les requêtes okhttp

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * @author utilisateur
 */
public class MeteoFrame extends JFrame {
    public MeteoFrame(String title){
        super(title);
        String url ="https://ai-weather-by-meteosource.p.rapidapi.com/find_places?text=lyon&language=en\n" ;
        
        System.out.println("avant la requette");
        
        new forecastWorker(url).execute();
        
        System.out.println("apres la requette");
        
        
    }    
    
    
     // Vu que la requête HTTP prend du temps, on le met dans un thread 
        // qui s'exécute en background pour avoir une appli plus robuste 
        
        // Remarque: les tâches qui prennent du temps ne doivent pas être exécutées dans le event thread mais dans un 
        // autre thread 
        
        /**
         * Cette méthode anonyme permettra d'exécuter la requête 
         * en background pour ne pas empêcher le code suivant de s'exécuter 
         * cela nous permettra d'avoir une appli plus robuste 
         * c'est un Thread 
         */
    
    class forecastWorker extends SwingWorker<String, Void>{
        
        private String url ;
        /**
         * constructeur de la classe 
         * @param _url represente l'url ves la requelle la requette sera envoyé
         */
        public forecastWorker(String _url){
            this.url = _url ;
        }

         @Override
            
            // Permet de faire la tâche en background, dans notre cas la tâche 
            // à effectuer c'est la requête HTTP
            protected String doInBackground(){
                System.out.println(Thread.currentThread().getName());
                
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("X-RapidAPI-Key", "0ecc3ebd7emsh522740b0d22b6d6p121e16jsn270b210d61a4")
                    .addHeader("X-RapidAPI-Host", "ai-weather-by-meteosource.p.rapidapi.com")
                    .build();
        
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()){ //on verifie si la reponse est un ereponse avec succes
                        return response.body().string(); 
                    }
                  
                    
                } catch (IOException ex) {
                    Logger.getLogger(MeteoFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                return null;
            }
            
             @Override
            // Cette fonction nous permettra de gérer les traitements après la réponse 
            // de notre requête 
             //dans cette fonction on se retrovera dans le event thread
             //donc on peut modifier les composantes de notre fenetre 
            protected void done() {
                
                try {
                    System.out.println(get());
                } catch (InterruptedException |ExecutionException ex) {
                    System.out.println("reponse vide :"+ex.getMessage());
                } 
              
            }
    }
}