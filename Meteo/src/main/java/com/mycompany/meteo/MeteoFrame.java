package com.mycompany.meteo;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

// Pour les requêtes okhttp
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class MeteoFrame extends JFrame {
    public MeteoFrame(String title){
        super(title);
        String url ="https://weatherbit-v1-mashape.p.rapidapi.com/forecast/3hourly?lat=45.7578137&lon=4.8320114" ;
        
        System.out.println("avant la requête");
        
        new ForecastWorker(url).execute();
        
        System.out.println("après la requête");
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
    
    class ForecastWorker extends SwingWorker

    {
        private String url ;

        public ForecastWorker(String _url){
            this.url = _url ;
        }

        @Override
        protected JSONObject doInBackground() {
            System.out.println(Thread.currentThread().getName());

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
               .url(url)
               .get()
               .addHeader("X-RapidAPI-Key", "0ecc3ebd7emsh522740b0d22b6d6p121e16jsn270b210d61a4")
               .addHeader("X-RapidAPI-Host", "weatherbit-v1-mashape.p.rapidapi.com")
               .build();

            try {
                    
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()){ //on verifie si la reponse est un ereponse avec succes
                    String jsonData= response.body().string(); //retour de la requette sous forme de chaine de caractere
                    //a ce stade on utilise une librairie pour retourner notre resultat
                    //sous forme d'un objet jSon pour pouvoir le manipuler plus facilement et extraire les donnees dont on a besoin 
                    JSONObject forcast =(JSONObject) JSONValue.parse(jsonData) ;//prend une string retourne un object JSON
                    return forcast ;
                }
                else{
                    JOptionPane.showMessageDialog(null, "oups une erreur est survenue ,veiller ressayer", "ERREUR", JOptionPane.ERROR_MESSAGE);
                }
                  
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "oups une erreur est survenue ,veiller ressayer", "ERREUR", JOptionPane.ERROR_MESSAGE);
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
                JSONObject forcast = (JSONObject) get(); //on recupere tout l'objet JSon
                JSONArray data = (JSONArray) forcast.get("data");// on recupere la premiere cle data qui contient un tableau
                JSONObject firstEntry = (JSONObject) data.get(0); //dans data on recupere la cle 0 qui est un objet 
                double temperature = (double) firstEntry.get("temp"); //et dans la cle 0 on recupere enfin la cle temperature
                //remarque en recuperant les donnees JSON les { correspondent a des objets et les [ a des Arrays
                System.out.println("Température : " + temperature);
            } 
            catch (InterruptedException | ExecutionException ex) {
                JOptionPane.showMessageDialog(null, "Oups, une erreur est survenue, veuillez réessayer", "ERREUR", JOptionPane.ERROR_MESSAGE);
                System.out.println("Réponse vide : " + ex.getMessage());
            }
        }
    }
}





