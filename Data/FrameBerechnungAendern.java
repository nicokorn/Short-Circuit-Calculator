
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import javax.swing.*;
import java.text.*;

import javax.print.*;


/**
 * Trennstellenfenster für Änderung an einem bestehenden Trennstellen-Eintrag
 */

public class FrameBerechnungAendern extends Dialog implements ActionListener{
    //Instanzen deklarieren/definieren
    //Allgemeines
    private Panel pKopf;  //Für Daten zum Auftrag
    private Panel pOben;  //Für Wert eingaben
    private Panel pMitte; //Für Totalausgaben von ad1+ad2 und Schlaufen innen+aussen
    private Button btnBerechne;
    private Button btnSpeichern;
    private Button btnAbbrechen;
   
    //Kabelwerte
    private int listIndex=JFrameHaupt.getListIndex();   
    private Kabelwerte aendern=Trennen.getTrennstelle(listIndex);
    
    //Ausgaben
    private double trennstelle=aendern.getTrennstelle();
    private double ad1ad2=aendern.getAd1ad2();
    private double innenaussen=aendern.getInnenaussen();
    private double kabellaengei=0;
    private double ad1i=0;
    private double ad2i=0;
    private double ausseni=0;
    private double inneni=0;
    private String sAuftragsnummer;
    private String sDatum;
    private String sName;
    private GregorianCalendar now = new GregorianCalendar();
    private DateFormat aktuellesDatum = DateFormat.getDateInstance(DateFormat.SHORT);

    //Eingabefelder
    private TextField ader1;
    private TextField ader2;
    private TextField innen;
    private TextField aussen;
    private TextField kabellaenge;
    private TextField auftragsnummer;
    private TextField datum;
    private TextField name;
    
    //Textfelder
    private TextArea kommentarfeld;
 
    //Genauigkeit
    private Boolean genauigkeitOk=true;
    
    //Eingabe
    private Boolean eingabeOk=true;
    private Boolean namenOk=true;
    
    //Hilfsvariabel für die Einstellung der Höhe der Komponenten
    private int y = 15;
    
    /**
     * Konstruktor, welcher unter anderem das GUI festlegt
     */
    public FrameBerechnungAendern(Frame f1){
        //Layout
        super(f1, "Trennstelle Ändern", true);
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        setLayout(null);
        setSize(600,650);
        setBackground(Color.lightGray);
        setLocation(200,200);
        setResizable(false);

        //Panels initialisieren
        pKopf = new Panel();
        pOben = new Panel();
        pMitte = new Panel();
        //Grid Layouts der Panel definieren
        pKopf.setLayout(new GridLayout(2,3,15,1));
        pOben.setLayout(new GridLayout(2,4,15,1));
        pMitte.setLayout(new GridLayout(1,1,1,1));
        //Positionen der Panels definieren
        pKopf.setBounds(50,60,500,50);
        pOben.setBounds(50,170+y,500,50);
        pMitte.setBounds(140,240+y,500,30);
        //Hintergrundfarben der Panels
        pKopf.setBackground(Color.lightGray);
        pOben.setBackground(Color.lightGray);
        pMitte.setBackground(Color.lightGray);
        
        //Zeile mit Angaben zum Kabel(Panel Kopf)
        //Label Auftragsnummer
        Label lblAuftragsnummer = new Label("Auftragsnummer:");
        pKopf.add(lblAuftragsnummer);
        //Label Datum
        Label lblDatum = new Label("Datum:");
        pKopf.add(lblDatum);
        //Label Name
        Label lblName = new Label("Name/Personalnummer:");
        pKopf.add(lblName);
        //Zeile mit Eingabenfelder
        //Eingabe Auftragsnummer
        auftragsnummer = new TextField(10);
        auftragsnummer.setText(aendern.getAuftragsnummer());
        pKopf.add(auftragsnummer);
        auftragsnummer.addActionListener(this);
        //Eingabe Datum
        datum = new TextField(10);
        pKopf.add(datum);
        datum.setText(aktuellesDatum.format(now.getTime()));
        datum.addActionListener(this);
        //Eingabe Name
        name = new TextField(10);
        name.setText(aendern.getNamen());
        pKopf.add(name);
        name.addActionListener(this);
        add(pKopf);
     
        //Zeile mit Angaben zur Kabelänge
        //Label Kabellänge
        Label lblKabellaenge = new Label("Kabellänge in Meter");
        lblKabellaenge.setBounds(245,110+y,130,20);
        add(lblKabellaenge);
        //Eingabe kabellänge
        kabellaenge = new TextField(10);
        kabellaenge.setText(""+aendern.getKabellaenge());
        kabellaenge.setBounds(245,130+y,110,24);
        add(kabellaenge);
        
        //Zeile mit Eingaben für die Widerstandswerten
        //Label Ader 1 
        Label lblAder1 = new Label("1. Ader"); // Eingabefeld für Widerstandswert der Ader 1
        pOben.add(lblAder1);
        // Ader 2 oder Schirm 
        Label lblAder2 = new Label("2. Ader/Schirm");; // Eingabefeld für Widerstandswert der Ader 2 oder des Schirms
        pOben.add(lblAder2);
        //Label Schlaufe innen
        Label lblInnen = new Label("3. Schlaufe innen"); // Eingabefeld für Widerstandswert der inneren Schlaufe
        pOben.add(lblInnen);
        //Label Schlaufe innen
        Label lblAussen = new Label("4. Schlaufe aussen"); // Eingabefeld für Widerstandswert der äusseren Schlaufe
        pOben.add(lblAussen);
        //4.Zeile (Panel oben)
        //ad1
        ader1 = new TextField(10);
        ader1.setText(""+aendern.getAder1());
        pOben.add(ader1);
        ader1.addActionListener(this);
        //ad2
        ader2 = new TextField(10);
        ader2.setText(""+aendern.getAder2());
        pOben.add(ader2);
        ader2.addActionListener(this);
        //Schlaufe innen
        innen = new TextField(10);
        innen.setText(""+aendern.getInnen());
        pOben.add(innen);
        innen.addActionListener(this);
        //Schlaufe aussen
        aussen = new TextField(10);
        aussen.setText(""+aendern.getAussen());
        pOben.add(aussen);
        aussen.addActionListener(this);
        add(pOben);
        
        //Zeile mit den zusammengerechneten Totalen
        //Label Total ad1+ad2
        Label lblAd1Ad2 = new Label("Total 1. + 2.");
        pMitte.add(lblAd1Ad2);
        //Label Total Schlaufe innen + aussen
        Label lblInnenAussen = new Label("Total 3. + 4.");
        pMitte.add(lblInnenAussen);
        add(pMitte);
        
        //Label Trennstelle
        Label lblTrennstelle = new Label("Von aussen Trennen bei:");
        lblTrennstelle.setBounds(231,310+y,180,20);
        add(lblTrennstelle);
        
        //"Berechne Trennstelle" Button deklarieren
        btnBerechne = new Button("Berechne Trennstelle");
        btnBerechne.setBounds(230,355+y,140,20);
        add(btnBerechne);
        btnBerechne.addActionListener(this);
        
        //Kommentarfeld
        Label lblKommentarfeld = new Label("Kommentar");
        lblKommentarfeld.setBounds(267,450+y,300,15);
        add(lblKommentarfeld);
        kommentarfeld = new TextArea("",1,1,3);
        kommentarfeld.setBounds(165, 470+y, 270,53);
        kommentarfeld.setText(aendern.getKommentar());
        add(kommentarfeld);
        
        //Speichern Button
        btnSpeichern = new Button("Speichern und Schliessen");
        btnSpeichern.setBounds(220,580,160,20);
        btnSpeichern.setEnabled(true);
        add(btnSpeichern);
        btnSpeichern.addActionListener(this);

        setVisible(true);
    }

    /**
     * ActionListener
     */
    public void actionPerformed(ActionEvent e){
        //Aktion A für das Betätigen des Berechnen Buttons
        if(e.getSource() == btnBerechne){
            try{
                //Mess-Eingaben in Ints umwandeln
                ad1i = Double.parseDouble(ader1.getText());
                ad2i = Double.parseDouble(ader2.getText());
                inneni = Double.parseDouble(innen.getText());
                ausseni = Double.parseDouble(aussen.getText());
                double gross = 0;
                double klein = 0;
                double differenz12und34 = 0;
                double schlaufeAussenAngepasst = 0;
                
                //Angaben zu Auftragsnummer, Datum und Namen in Strings abspeichern.
                sAuftragsnummer = auftragsnummer.getText();
                sDatum = datum.getText();
                sName = name.getText();
                kabellaengei = Double.parseDouble(kabellaenge.getText());
            
                //Berechnen von ad1 + ad2
                ad1ad2 = ad1i + ad2i;
            
                //Berechnen von äusserer und innerer Schlaufe
                innenaussen = inneni + ausseni;
            
                //Berechnung der Differenz zwischen den Totalen 1+2 und 3+4
                differenz12und34 = Math.sqrt((innenaussen - ad1ad2)*(innenaussen - ad1ad2));
                
                //Schlaufe aussen muss mit der Differenz angepasst werden.
                schlaufeAussenAngepasst = ausseni - (differenz12und34/2);
                
                //Berechnen von Trennstelle
                if(innenaussen == 0){
                    trennstelle = 0;
                }else{
                    trennstelle = (kabellaengei / ad1ad2) * schlaufeAussenAngepasst;
                }
                //Flag berechnet auf true setzen

                
                //System.out.println("ArrayGrösse: "+trennstellen.size()+"Kabelindex: "+kabelindex);
                //Checkups von Angaben und Genauigkeit
                //Bestimmung der Genauigkeit
                if(ad1ad2 > innenaussen){
                    gross = ad1ad2;
                    klein = innenaussen;
                }else{
                    gross = innenaussen;
                    klein = ad1ad2;
                }
                if((1.00-(klein/gross)) > 0.2 || trennstelle < 0){
                    genauigkeitOk = false;
                    namenOk = true;
                    eingabeOk = true;
                }else{
                    genauigkeitOk = true;
                }
                eingabeOk = true;
                //Kontrolle ob Namen eingegeben wurde
                if(("").equals(sName) || ("").equals(sAuftragsnummer) || ("").equals(sDatum)){
                    namenOk = false;
                    genauigkeitOk = true;
                }else{
                    namenOk = true;
                }

            }
            catch(NumberFormatException exc){
                eingabeOk = false;
                namenOk = false;
                genauigkeitOk = true;
            }
        }
        
        //Aktion B Speichern und Schliessen
        if(e.getSource() == btnSpeichern){
            //Bestehende Werte wieder initialisieren
            sAuftragsnummer = auftragsnummer.getText();
            sDatum = datum.getText();
            sName = name.getText();
            kabellaengei = Double.parseDouble(kabellaenge.getText());
            ad1i = Double.parseDouble(ader1.getText());
            ad2i = Double.parseDouble(ader2.getText());
            inneni = Double.parseDouble(innen.getText());
            ausseni = Double.parseDouble(aussen.getText());
            //Speichern
            Trennen.setTrennstelle(listIndex, new Kabelwerte(sAuftragsnummer, sDatum, sName, innenaussen, ad1ad2, trennstelle, kabellaengei, ad1i, ad2i, ausseni, inneni, kommentarfeld.getText()));
            JFrameHaupt.setList(listIndex, sDatum, sAuftragsnummer, kabellaengei, trennstelle);
            dispose();
            setVisible(false);
        }
        repaint();
    }
    
    /**
     * Paint mit der Ausgabe der Zahl
     */
    public void paint(Graphics g){
        //Casten
        Graphics2D g2d = (Graphics2D)g;
        
        //Dezinastellen minimieren
        DecimalFormat f = new DecimalFormat("#0.00"); 
        
        //Schriftart
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));

        //Ausgabe für Total 1. + 2.
        paint(g2d,""+f.format(ad1ad2)+" \u2126",175,280+y);
        
        //Ausgabe für Total 1. + 2.
        paint(g2d,""+f.format(innenaussen)+" \u2126",425,280+y);
        
        //Ausgabe für Trennstelle
        paint(g2d,""+f.format(trennstelle)+" m", 300,344+y);
        
        //Copyright
        g.drawString("\u00A9 Nico Korn", 10, 625+y);
        
        //Status Rahmen
        g.drawRect(50,395+y,500,20);
        paint(g2d,"Status",300,430+y);
        
        //Genauigkeit genügend?
        if(genauigkeitOk != true){
            g.setColor(Color.RED);
            paint(g2d,"Ergebnis zu ungenau! Bitte erneut einbrennen, oder Shirla benutzen.",300,410+y);
            g2d.setStroke(new BasicStroke(4f));
            g.drawLine(238,349+y,362,349+y);
        }
        
        //Eingabe korrekt?
        if(eingabeOk != true || namenOk != true){
            int breite = 152;
            g.setColor(Color.RED);
            paint(g2d,"Nicht korrekt ausgefüllt!",300,410+y);
            g2d.setStroke(new BasicStroke(4f));
            g.drawLine(53,97+y,53+breite,97+y);
            g.drawLine(224,97+y,224+breite,97+y);
            g.drawLine(395,97+y,395+breite,97+y);
            
            g.drawLine(247,156+y,353,156+y);
            
            breite = 162-53;
            g.drawLine(53,222+y,53+breite,222+y);
            g.drawLine(181,222+y,181+breite,222+y);
            g.drawLine(309,222+y,309+breite,222+y);
            g.drawLine(437,222+y,437+breite,222+y);
        }
        
        //Status OK?
        if(eingabeOk == true && genauigkeitOk == true && namenOk == true){
            g.setColor(Color.BLUE);
            paint(g2d,"OK",300,410+y);
        }
    }
    
    /**
     * ProcessEvent
     */
    public void processEvent(AWTEvent e){
        if(e.getID() == Event.WINDOW_DESTROY){
            dispose();
            setVisible(false);
        }else{
            super.processEvent(e);
        }
    }
    
    /**
     * Methode welche das zentrieren eines Strings erleichtert
     */
    protected void paint(Graphics2D g2d, String st,float point1 , float point2) {
        FontRenderContext fontRendContext = g2d.getFontRenderContext();
        Rectangle2D rect = g2d.getFont().getStringBounds(st, fontRendContext);
        float width = (float) rect.getWidth();
        g2d.drawString(st, point1 - width / 2, point2);
    }
}

       