
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;



/**
 * Typ:         Application
 * Name:        Trennen.java
 * Autor:       Nico Korn
 * Datum:       14.07.2013
 * Projekt:     Trennen
 * Version:     Beta
 * 
 * Beschreibung: Dieses Programm erlaubt das effiziente berechnen mehrer Trennstellen
 * mit Druckfunktion und simplen Eintragsmanagement
 * 
 */
public class Trennen{
    //Deklarieren
    //JFrames
    private static JFrameHaupt m;
    
    //Arraylist für Kabeldaten
    private static ArrayList<Kabelwerte> trennstellen = new ArrayList<Kabelwerte>();

    /**
     * Main
     */
    public static void main(String args[]){
        m = new JFrameHaupt();
    }

    /**
     * Methode zum speichern einer Trennstelle
     */
    public static void safeTrennstelle(Kabelwerte neuerEintrag){
        trennstellen.add(neuerEintrag);
    }
    
    /**
     * Methode zur Übergabe einer Trennstelle
     */
    public static Kabelwerte getTrennstelle(int lstIndex){
        return trennstellen.get(lstIndex);
    }
    
    /**
     * Methode zur Übergabe der grösse der ArrayList
     */
    public static int getLength(){
        return trennstellen.size();
    }
    
    /**
     * Methode zum Überschreiben eines Eintrages
     */
    public static void setTrennstelle(int lstIndex, Kabelwerte aenderung){
        trennstellen.set(lstIndex, aenderung);
    }
    
    public static void removeTrennstelle(int lstIndex){
        trennstellen.remove(lstIndex);
    }
    
    public static void clearTrennstelle(){
        trennstellen.clear();
    }
}