
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import javax.swing.*;
import java.text.*;

import javax.print.*;


/**
 * Die Klasse beinhaltet eine Musterseite des Trennstellenformulars
 */
class Druckseite {
    
    //Instanzenvariabeln deklarieren
    //Daten
    private static String auftragsnummer;
    private static String datum;
    private static String name;
    //Werte
    private static double kabellaenge = 0;
    private static int y = 0;
    private static double trennstelle=0;    
    //Zusatzkommentar
    private static String kommentar;
    
    /**
     * Konstruktor der Klasse 
     */
    public Druckseite(int y, double trennstelle, String auftragsnummer, String datum, String name, double kabellaenge, String kommentar){
        this.y = y;
        this.trennstelle = trennstelle;
        this.auftragsnummer = auftragsnummer;
        this.datum = datum;
        this.name = name;
        this.kabellaenge = kabellaenge;
        this.kommentar = kommentar;
    }
    
    /**
     * Diese Methode zeichnet die Druckseite
     */
    public static void zeichne(Graphics g){
        //Dezinastellen minimieren
        DecimalFormat f = new DecimalFormat("#0.00"); 
        DecimalFormat h = new DecimalFormat("#0"); 
        
        //Zeichnen
        g.setFont(new Font("Bradley Hand ITC", Font.PLAIN, 50));
        g.drawString(""+name,80,110+y);
        g.setFont(new Font("Calibri", Font.PLAIN, 24));
        g.drawString("Fehlerbehebung:        Spannungsdurchschlag",80,165+y);
        g.setFont(new Font("Calibri", Font.PLAIN, 20));
        g.drawString("Auftragsnr: "+auftragsnummer,80,205+y);
        g.drawString("Datum: "+datum,375,205+y);
        g.drawString("Kabellänge: "+h.format(kabellaenge)+" m",80,230+y);
        g.setFont(new Font("Calibri", Font.BOLD, 30));
        if(trennstelle != 0){
            g.drawString("Trennen bei "+Math.round(trennstelle)+" m von aussen.", 80, 290+y);
        }else{
            g.drawString("Trennen bei -siehe Kommentar-", 80, 290+y);
        }
        g.setFont(new Font("Calibri", Font.PLAIN, 20));
        g.drawString(""+kommentar, 80, 325+y);
        g.drawRect(40,55+y,510,330);
    }
    
}