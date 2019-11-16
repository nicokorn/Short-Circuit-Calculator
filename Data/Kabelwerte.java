
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import javax.swing.*;
import java.text.*;
import javax.print.*;


/**
 * Die Klasse beinhaltet alle Daten und Werte zu einem ausgewertetem Kabel
 */
class Kabelwerte {
    
    //Instanzenvariabeln deklarieren
    //Daten
    private String auftragsnummer;
    private String datum;
    private String name;
    //Werte
    private double ader1 = 0;
    private double ader2 = 0;
    private double innen = 0;
    private double aussen = 0;
    private double innenaussen = 0;
    private double ad1ad2 = 0;
    private double trennstelle = 0;
    private double kabellaenge = 0;
    //Zusatzkommentar
    private String kommentar = "";
    
    /**
     * Konstruktor der Klasse 
     */
    public Kabelwerte(String auftragsnummer, String datum, String name, double innenaussen, double ad1ad2, double trennstelle, double kabellaenge, double ader1, double ader2, double aussen, double innen, String kommentar){
        this.auftragsnummer = auftragsnummer;
        this.datum = datum;
        this.name = name;
        this.innenaussen = innenaussen;
        this.ad1ad2 = ad1ad2;
        this.trennstelle = trennstelle;
        this.kabellaenge = kabellaenge;
        this.ader1 = ader1;
        this.ader2 = ader2;
        this.innen = innen;
        this.aussen = aussen;
        this.kommentar = kommentar;
    }
    
    /**
     * Diese Methode gibt die Auftragsnummer zurück
     */
    public String getAuftragsnummer(){
        return auftragsnummer;
    }
    
    /**
     * Diese Methode gibt das Datum zurück
     */
    public String getDatum(){
        return datum;
    }
    
    /**
     * Diese Methode gibt den Namen zurück
     */
    public String getNamen(){
        return name;
    }
    
    /**
     * Diese Methode gibt den summierten Widerstandswert der innen und äusseren Schlaufe zurück
     */
    public double getInnenaussen(){
        return innenaussen;
    }
    
    /**
     * Diese Methode gibt den summierten Widerstandswert der beiden gemessenen Leiter zurück
     */
    public double getAd1ad2(){
        return ad1ad2;
    }
    
    /**
     * Diese Methode gibt die Meterlänge der Trennstelle zurück
     */
    public double getTrennstelle(){
        return trennstelle;
    }
    
    /**
     * Diese Methode gibt die Meterlänge des Kabels zurück
     */
    public double getKabellaenge(){
        return kabellaenge;
    }
    
    /**
     * Diese Methode gibt den eingegebenen Zusatzkommentar zurück
     */
    public String getKommentar(){
        return kommentar;
    }
    
    /**
     * Diese Methode holt sich den Zusatzkommentar aus dem MainProgramm
     */
    public void setKommentar(String kommentar){
        this.kommentar = kommentar;
    }
    
    /**
     * Diese methode gibt den Widerstandswert der 1. Ader zurück
     */
    public double getAder1(){
        return ader1;
    }
    
    /**
     * Diese Methode gibt den Widerstandswert der 2. Ader zurück
     */
    public double getAder2(){
        return ader2;
    }
    
    /**
     * Diese methode gibt den Widerstandswert der äusseren Schlaufe zurück
     */
    public double getAussen(){
        return aussen;
    }
    
    /**
     * Diese Methode gibt den Widerstandswert der inneren Schlaufe zurück
     */
    public double getInnen(){
        return innen;
    }
}