package com.mycompany.meteo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout.Constraints;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

// Pour les requêtes okhttp
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class MeteoFrame extends JFrame {
    
    JLabel country ;
    JLabel timeLabel ;
    JLabel temperature ;
    JPanel otherInfo ;
    JLabel windLabel ;
    JLabel windValue ;
    JLabel precipLabel ;
    JLabel precipValue ;
    
    JLabel summaryLabel ;
    
   
    public MeteoFrame(String title){
        super(title);
        Component contentPane =getContentPane();
        String url ="https://weatherbit-v1-mashape.p.rapidapi.com/forecast/3hourly?lat=45.7578137&lon=4.8320114" ;
        //constraint.insets =new Insets(10,10,10,10);
       
        ((JComponent)contentPane).setBorder(BorderFactory.createEmptyBorder(14,14,14,14));
        
        System.out.println("avant la requête");
        
        new ForecastWorker(url).execute();
        
        System.out.println("après la requête");
        setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        
        country =new JLabel("lyon fr" ,SwingConstants.CENTER);
        country.setAlignmentX(Component.CENTER_ALIGNMENT);
        country.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
        country.setForeground(new Color(255,255,255,128));
        country.setFont(new Font ("san francisco",Font.ITALIC ,25));
        
        
        
        timeLabel =new JLabel("--" ,SwingConstants.CENTER);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);//pour centrer le container qui contient le texte
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("a",Font.ITALIC,14));
        
        
        
        temperature =new JLabel("--" ,SwingConstants.CENTER);//centrer l texte dans le container
        temperature.setAlignmentX(Component.CENTER_ALIGNMENT);
        temperature.setForeground(Color.WHITE);
        temperature.setFont(new Font("San Fransisco",Font.BOLD,80));
        
        otherInfo =new JPanel();
        otherInfo.setLayout(new GridLayout(2,2));
        otherInfo.setBackground(Color.blue);
        windLabel=new JLabel("vitesse du vent ",SwingConstants.CENTER);
        windLabel.setForeground(new Color(255,255,255,128));
        windValue=new JLabel("--",SwingConstants.CENTER);
        windValue.setForeground(Color.WHITE);
        precipLabel=new JLabel("precipitation",SwingConstants.CENTER);
        precipLabel.setForeground(new Color(255,255,255,128));
        precipValue=new JLabel("--",SwingConstants.CENTER);
        precipValue.setForeground(Color.WHITE);
        //ajout des elelment dans JPanel
        otherInfo.add(windLabel);
        otherInfo.add(precipLabel);
        otherInfo.add(windValue);
        otherInfo.add(precipValue);
        
        
        summaryLabel =new JLabel("summary" ,SwingConstants.CENTER);
        summaryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        summaryLabel.setForeground(new Color(255,255,255,128));
        summaryLabel.setFont(new Font ("san francisco",Font.ITALIC ,25));
        
        
        add(country);
        add(timeLabel);
        add(temperature );
        add(otherInfo);
        add(summaryLabel);
        
        setLocationRelativeTo(null );
        
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
                Double temperatD = (Double) firstEntry.get("temp"); //et dans la cle 0 on recupere enfin la cle temperature
                int temperati =temperatD.intValue();
                String timeZone =(String)forcast.get("timezone");
                Long time = (Long) firstEntry.get("ts");// on recupere le nombre de seconde ecoulées depuis 1970
                
                String tempsActuel =convertir(time,timeZone);
                Double vent =(Double)firstEntry.get("wind_spd");
                System.out.println("temps" +time);
                timeLabel.setText("Il est " +tempsActuel+ "et la temperature est ");
                temperature.setText(String.valueOf(temperati)+"°");
                windValue.setText(vent+"");
                //remarque en recuperant les donnees JSON les { correspondent a des objets et les [ a des Arrays
                System.out.println("Température : " + temperature);
                //System.out.println("Température : " + time);
                
            } 
            catch (InterruptedException | ExecutionException ex) {
                //JOptionPane.showMessageDialog(null, "Oups, une erreur est survenue, veuillez réessayer", "ERREUR", JOptionPane.ERROR_MESSAGE);
                System.out.println("Réponse vide : " + ex.getMessage());
            }
        }
    }
    
    /**
     * cette fonction converti le nombre de seconde ecoulé depuis 1970 en format d'heure 
     * @param time le nombre de seconde ecoulé depuis 1970
     * @param timeZone timeZone de la ville recuperé dans les donnes de l'API
     * @return la date au format souhaité
     */
    public String convertir(Long time,String timeZone){
        Date date =new Date(time ); //convertir le nombre de seconde en milliseconde 
        SimpleDateFormat formatter =new SimpleDateFormat("HH:mm ");
        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
        String formater =formatter.format(date);
        return formater ;
    }
    
   

}





