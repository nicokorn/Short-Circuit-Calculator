
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.URL;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import javax.swing.*;
import java.text.*;
import javax.print.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.List.*;
import java.io.File;
import java.io.*;
import java.awt.Graphics;

/**
 * Hauptfenster
 */
public class JFrameHaupt extends JFrame implements ActionListener, WindowListener, ItemListener{
    //Deklarieren
    //Logo
    private URL urlLogo;
    private JLabel lblLogo;
    
    //JButtons
    private JButton btnNeu;
    private JButton btnAendern;
    private JButton btnLoeschen;
    private JButton btnDrucken;
    
    //Auswahlliste
    private static java.awt.List lstAuswahl;
    private static int lstIndex = 0;
    private static int anzahlMarkierteEintraege = 0;
    private static Label lblDatum;
    private static Label lblAuftragsnummer;
    private static Label lblKabellaenge;
    private static Label lblTrennstelle;
    
    //Berechnenfenster
    private static FrameBerechnungNeu neu;
    private static FrameBerechnungAendern aendern;
    
    //MenuBar
    private static JMenuBar menuleiste;
    private static JMenu aktion;
    private static JMenu hilfe;
    private static JMenuItem speichern;
    private static JMenuItem speichernUnter;
    private static JMenuItem laden;
    private static JMenuItem allesLoeschen;
    private static JMenuItem beenden;
    private static JMenuItem ueberDasProgramm;
    
    //Speichern
    private static JFileChooser fcSpeichernUnter;
    private static Boolean speichernUnterOK = false;
    private static Boolean ladenOK = false;
    private static PrintWriter outFile;
    private static BufferedReader inFile;
    private static String eingeleseneDaten;
    private static String[] gesplittet;
    private static int anzEintraege = 0;
    
    //Dialogfenster "Über das Programm"
    private JOptionPane DUeberDasProgramm;
    
    //Dialogfenster, welches auftaucht, sobald geladen wird, und vorhandenes verschwindet
    private JOptionPane DLaden;
    
    /**
     * Konstruktor
     */
    public JFrameHaupt(){
        //Layout
        setTitle("Trennstellen Evaluation");
        setSize(650,260);
        setLayout(null);
        getContentPane().setBackground(new Color(255,251,242));
        setLocation(100,100);
        setResizable(false);
        
        //Logo
        urlLogo = getClass().getResource("hs.jpg");
        lblLogo = new JLabel(new ImageIcon(urlLogo));
        lblLogo.setBounds(5,6,150,30);
        add(lblLogo);

        //JButtons
        //Neue Trennstelle
        btnNeu = new JButton("Neue Trennstelle");
        btnNeu.setBounds(5,40,150,30);
        add(btnNeu);
        btnNeu.addActionListener(this);
        //Ändern
        btnAendern = new JButton("Ändern");
        btnAendern.setBounds(5,75,150,30);
        btnAendern.setEnabled(false);
        add(btnAendern);
        btnAendern.addActionListener(this);
        //Löschen
        btnLoeschen = new JButton("Löschen");
        btnLoeschen.setBounds(5,110,150,30);
        btnLoeschen.setEnabled(false);
        add(btnLoeschen);
        btnLoeschen.addActionListener(this);
        //Alles Drucken
        btnDrucken = new JButton("Drucken");
        btnDrucken.setBounds(5,145,150,30);
        btnDrucken.setEnabled(false);
        add(btnDrucken);
        btnDrucken.addActionListener(this);
        
        //Auswahlliste
        lstAuswahl = new java.awt.List(5,true);
        lstAuswahl.setBounds(160,40,480,165);
        lstAuswahl.setFont(new Font("Monospaced", Font.BOLD, 18));
        add(lstAuswahl);
        lstAuswahl.addItemListener(this);
            lblDatum = new Label("Datum");
            lblDatum.setBounds(203,20,80,20);
            lblDatum.setFont(new Font("Dialog", Font.PLAIN, 16));
            add(lblDatum);
            lblAuftragsnummer = new Label("Auftrag");
            lblAuftragsnummer.setBounds(320,20,80,20);
            lblAuftragsnummer.setFont(new Font("Dialog", Font.PLAIN, 16));
            add(lblAuftragsnummer);
            lblKabellaenge = new Label("Kabellänge");
            lblKabellaenge.setBounds(423,20,80,20);
            lblKabellaenge.setFont(new Font("Dialog", Font.PLAIN, 16));
            add(lblKabellaenge);
            lblTrennstelle = new Label("Trennstelle");
            lblTrennstelle.setBounds(561,20,160,20);
            lblTrennstelle.setFont(new Font("Dialog", Font.PLAIN, 16));
            add(lblTrennstelle);
        
        //Menuleiste
        menuleiste = new JMenuBar();
        //Aktion
        aktion = new JMenu("Aktion");
            speichern = new JMenuItem("Speichern");
            speichern.setEnabled(false);
            speichern.addActionListener(this);
            aktion.add(speichern);
            speichernUnter = new JMenuItem("Speichern unter");
            speichernUnter.setEnabled(false);
            speichernUnter.addActionListener(this);
            aktion.add(speichernUnter);
            laden = new JMenuItem("Laden");
            laden.addActionListener(this);
            aktion.add(laden);
            allesLoeschen = new JMenuItem("Alles Löschen");
            allesLoeschen.setEnabled(false);
            allesLoeschen.addActionListener(this);
            aktion.add(allesLoeschen);
            aktion.addSeparator();
            beenden = new JMenuItem("Beenden");
            beenden.addActionListener(this);
            aktion.add(beenden);
        menuleiste.setBackground(new Color(242,242,208));
        menuleiste.add(aktion);
        //Hilfe
        hilfe = new JMenu("Hilfe");
            ueberDasProgramm = new JMenuItem("Über das Programm");
            ueberDasProgramm.addActionListener(this);
            hilfe.add(ueberDasProgramm);
        menuleiste.add(hilfe);
        //Menuleiste hinzufügen
        setJMenuBar(menuleiste);
        
        setVisible(true);
        addWindowListener(this);
    }
    
    /**
     * Methode welche einen neuen Eintrag zur Liste hinzufügt
     */
    public static void setList(String datum, String auftragsnummer, double kabellaengei, double trennstelle){
        //Anzahl signifikanter Zahlenstellen für die Ausgabe in der List
        DecimalFormat f = new DecimalFormat("#0");
        DecimalFormat h = new DecimalFormat("#0.00");
        //neuer Eintrag in die List
        lstAuswahl.add(String.format("%5s %10s %9s m %9s m", datum, auftragsnummer, f.format(kabellaengei), h.format(trennstelle)));
    }
    
        public static void setList(int index, String datum, String auftragsnummer, double kabellaengei, double trennstelle){
        //Anzahl signifikanter Zahlenstellen für die Ausgabe in der List
        DecimalFormat f = new DecimalFormat("#0");
        DecimalFormat h = new DecimalFormat("#0.00");
        //neuer Eintrag in die List
        lstAuswahl.replaceItem(String.format("%5s %10s %9s m %9s m", datum, auftragsnummer, f.format(kabellaengei), h.format(trennstelle)), index);
    }
    
    /**
     * Methode welcher den aktuellen Listenindex zurückgibt
     */
    public static int getListIndex(){
        return lstIndex;
    }
    
    /**
     * ActionPerformed
     */
    public void actionPerformed(ActionEvent e){
        //Aktion A neue Trennstelle
        if(e.getSource() == btnNeu){
            neu = new FrameBerechnungNeu(this);
            //wurde ein neuer Eintrag in die ArryList hinzugefügt? Wenn Ja, dann folgende MenuItems enablen
            if(Trennen.getLength() != 0){
                speichern.setEnabled(true);
                speichernUnter.setEnabled(true);
                allesLoeschen.setEnabled(true);
            }
        }
        //Aktion B Trennse ändern
        if(e.getSource() == btnAendern){
            //Markierungs Index herauslesen
            lstIndex = lstAuswahl.getSelectedIndex();
            //Neues Fenster öffnen um Anpassungen zu machen
            aendern = new FrameBerechnungAendern(this);
            //Buttons disablen nach schliessen der Änderung
            if(lstAuswahl.getSelectedIndexes().length == 0){
                btnAendern.setEnabled(false);
                btnLoeschen.setEnabled(false);
                btnDrucken.setEnabled(false);
                allesLoeschen.setEnabled(false);
            }else{
                btnAendern.setEnabled(true);
                btnLoeschen.setEnabled(true);
                btnDrucken.setEnabled(true);
                allesLoeschen.setEnabled(true);
            }
        }
        //Aktion C Löschen eines markierten Eintrages
        if(e.getSource() == btnLoeschen){
            //Anzahl selektierte Einträge in der Liste
            anzahlMarkierteEintraege = lstAuswahl.getSelectedIndexes().length;
            //Array mit den markierten Indexen
            int[] ArraylstAuswahlMarkierteEintraege = lstAuswahl.getSelectedIndexes(); 
            int index = lstAuswahl.getSelectedIndex();
            //Markierte Stellen aus der List und dem ArrayList löschen
            if(anzahlMarkierteEintraege > 1){
                for(int i = anzahlMarkierteEintraege-1; i >= 0; i--){
                    lstAuswahl.remove(ArraylstAuswahlMarkierteEintraege[i]);
                    Trennen.removeTrennstelle(ArraylstAuswahlMarkierteEintraege[i]);
                }
            }else{
                lstAuswahl.remove(index);
                Trennen.removeTrennstelle(index);
            }
            //Buttons allenfalls disablen bei 0 Einträgen
            if(Trennen.getLength() == 0){
                btnAendern.setEnabled(false);
                btnLoeschen.setEnabled(false);
                btnDrucken.setEnabled(false);
                speichern.setEnabled(false);
                speichernUnter.setEnabled(false);
                allesLoeschen.setEnabled(false);
            }
            //Nur wenn ein Eintrag markiert wurde soll der Löschen button enabled werden
            if(lstAuswahl.getSelectedIndexes().length == 0){
                btnLoeschen.setEnabled(false);
            }else{
                btnLoeschen.setEnabled(true);
            }
        }
        //Aktion D Drucken aller Einträge
        if(e.getSource() == btnDrucken){                   
            //Das Druckermenu aufrufen
            Toolkit tk = Toolkit.getDefaultToolkit();
            PrintJob pj = tk.getPrintJob(new Frame(), "", null);
            //Wenn Druck akzeptiert wurde, dann folgende Seiten zeichnen und übermitteln
            if(pj != null){
                int anzahlDruckplaetze = 0;
                int k = 0;
                anzahlMarkierteEintraege = lstAuswahl.getSelectedIndexes().length;
                if (anzahlMarkierteEintraege % 2 != 0) {
                    anzahlDruckplaetze = (anzahlMarkierteEintraege/2) + 1;
                }else{
                    anzahlDruckplaetze = anzahlMarkierteEintraege/2;
                }
                int bereitsGedruckt = 0;
                for(int j=0; j<anzahlDruckplaetze; j++){ 
                    Graphics pg = pj.getGraphics();
                    
                    if(pg != null){
                        int y = 0;
                        int anzahlTrennstellenfelder = 0;
                        int[] ArraylstAuswahlMarkierteEintraege = lstAuswahl.getSelectedIndexes(); 
                        
                        //Zurücksetzen des Positiongebers für das Trennstellensheet
                        if(y == 800) {
                            y = 0;
                        }
                        
                        //Checkup ob es sich um 1 oder 2 Trennstellefelder pro Seite handelt
                        System.out.println(" "+((anzahlMarkierteEintraege-bereitsGedruckt) / 2)+" "+(anzahlMarkierteEintraege-bereitsGedruckt)+"Länge: "+anzahlMarkierteEintraege);
                        if ((anzahlMarkierteEintraege-bereitsGedruckt) / 2 > 0) {
                            anzahlTrennstellenfelder = 2;
                        }else{
                            anzahlTrennstellenfelder = 1;
                        }
                        
                        for(int i=0; i<anzahlTrennstellenfelder; i++){
                            new Druckseite(y, Trennen.getTrennstelle(ArraylstAuswahlMarkierteEintraege[k]).getTrennstelle(), Trennen.getTrennstelle(ArraylstAuswahlMarkierteEintraege[k]).getAuftragsnummer(), Trennen.getTrennstelle(ArraylstAuswahlMarkierteEintraege[k]).getDatum(), Trennen.getTrennstelle(ArraylstAuswahlMarkierteEintraege[k]).getNamen(), Trennen.getTrennstelle(ArraylstAuswahlMarkierteEintraege[k]).getKabellaenge(), Trennen.getTrennstelle(ArraylstAuswahlMarkierteEintraege[k]).getKommentar());
                            y += 400;
                            k += 1;
                            Druckseite.zeichne(pg);
                        }
                        pg.dispose();
                        bereitsGedruckt += 2;
                    }
                }   
                pj.end();
            }
        }
        //Aktion E Beenden über Menuleiste
        if(e.getSource() == beenden){
            System.exit(0);
        }
        //Aktion F "Speichern" der Liste
        if(e.getSource() == speichern){
            //Speichernunter Dialog
            Kabelwerte aendern = Trennen.getTrennstelle(0);
            //Etscheidung ob ein Speichern Dialog gestartet werden soll oder einfach das vorhandene File aktualisiert werden soll
            if(speichernUnterOK != true){
                //In ein File schreiben
                try{
                    speichernUnterOK = true;
                    fcSpeichernUnter = new JFileChooser();
                    fcSpeichernUnter.setFileFilter(new FileNameExtensionFilter("Textdateien", "*.txt"));
                    fcSpeichernUnter.setSelectedFile(new File("c:/"+aendern.getAuftragsnummer()+".txt"));
                    fcSpeichernUnter.showSaveDialog(this);
                    outFile = new PrintWriter(new FileWriter(fcSpeichernUnter.getSelectedFile()));
                    for(int i=0; i<lstAuswahl.getItemCount(); i++){
                        aendern = Trennen.getTrennstelle(i);
                        outFile.print(aendern.getAuftragsnummer()+";;"+aendern.getDatum()+";;"+aendern.getNamen()+";;"+aendern.getInnenaussen()+";;"+aendern.getAd1ad2()+";;"+aendern.getTrennstelle()+";;"+aendern.getKabellaenge()+";;"+aendern.getAder1()+";;"+aendern.getAder2()+";;"+aendern.getAussen()+";;"+aendern.getInnen()+";;"+aendern.getKommentar()+";;");
                    }
                    outFile.close();
                }
                catch(IOException f){
                }
            }
            if(speichernUnterOK == true){
                //Das File aktualisieren
                try{
                    outFile = new PrintWriter(new FileWriter(fcSpeichernUnter.getSelectedFile()));
                    for(int i=0; i<lstAuswahl.getItemCount(); i++){
                        aendern = Trennen.getTrennstelle(i);
                        outFile.print(aendern.getAuftragsnummer()+";;"+aendern.getDatum()+";;"+aendern.getNamen()+";;"+aendern.getInnenaussen()+";;"+aendern.getAd1ad2()+";;"+aendern.getTrennstelle()+";;"+aendern.getKabellaenge()+";;"+aendern.getAder1()+";;"+aendern.getAder2()+";;"+aendern.getAussen()+";;"+aendern.getInnen()+";;"+aendern.getKommentar()+";;");
                    }
                    outFile.close();
                }
                catch(IOException f){
                }
            }
        }
        //Aktion G "Speichern unter" der Liste
        if(e.getSource() == speichernUnter){
            //Speichernunter Dialog
            Kabelwerte aendern = Trennen.getTrennstelle(0);
            fcSpeichernUnter = new JFileChooser();
            fcSpeichernUnter.setFileFilter(new FileNameExtensionFilter("Textdateien", "*.txt"));
            fcSpeichernUnter.setSelectedFile(new File("c:/"+aendern.getAuftragsnummer()+".txt"));
            fcSpeichernUnter.showSaveDialog(this);
            //In ein File schreiben
            try{
                outFile = new PrintWriter(new FileWriter(fcSpeichernUnter.getSelectedFile()));
                for(int i=0; i<lstAuswahl.getItemCount(); i++){
                    aendern = Trennen.getTrennstelle(i);
                    outFile.print(aendern.getAuftragsnummer()+";;"+aendern.getDatum()+";;"+aendern.getNamen()+";;"+aendern.getInnenaussen()+";;"+aendern.getAd1ad2()+";;"+aendern.getTrennstelle()+";;"+aendern.getKabellaenge()+";;"+aendern.getAder1()+";;"+aendern.getAder2()+";;"+aendern.getAussen()+";;"+aendern.getInnen()+";;"+aendern.getKommentar()+";;");
                }
                speichernUnterOK = true;
                outFile.close();
            }
            catch(IOException f){
            }
        }
        //Aktion H "Laden" einer Liste
        if(e.getSource() == laden){
            //File Dialog
            fcSpeichernUnter = new JFileChooser();
            fcSpeichernUnter.setFileFilter(new FileNameExtensionFilter("Textdateien","txt"));
            fcSpeichernUnter.showOpenDialog(this);
            //Anzahl signifikanter Zahlenstellen für die Ausgabe in der List
            DecimalFormat f = new DecimalFormat("#0");
            DecimalFormat h = new DecimalFormat("#0.00");
            //File öffnen und in die Arraylist, bzw, List laden
            try{
                if(fcSpeichernUnter.getSelectedFile() != null){
                    //Filedialog mit Speichern unter wird deaktiviert
                    speichernUnterOK = true;
                    allesLoeschen.setEnabled(true);
                    //Vorhandene Daten überschreiben
                    lstAuswahl.removeAll();
                    Trennen.clearTrennstelle();
                    //Daten aus File einlesen
                    inFile = new BufferedReader(new FileReader(fcSpeichernUnter.getSelectedFile()));
                    String line;
                    int index = 0;
                    //while((line = inFile.readLine()) != null){
                    //    eingeleseneDaten += line+", ";
                    //}
                    //Daten Splitten
                    eingeleseneDaten = inFile.readLine();
                    gesplittet = eingeleseneDaten.split(";;");
                    speichern.setEnabled(true);
                    speichernUnter.setEnabled(true);
                    inFile.close();
                    //Daten in die Arraylist kopieren
                    anzEintraege = gesplittet.length / 12;
                    for(int i=0; i<anzEintraege; i++){
                        String tempAuftragsnummer = gesplittet[index];
                        String tempDatum = gesplittet[index+1];
                        String tempName = gesplittet[index+2];
                        double tempInnenaussen = Double.parseDouble(gesplittet[index+3]);
                        double tempAd1Ad2 = Double.parseDouble(gesplittet[index+4]);
                        double tempTrennstelle = Double.parseDouble(gesplittet[index+5]);
                        double tempKabellaenge = Double.parseDouble(gesplittet[index+6]);
                        double tempAd1 = Double.parseDouble(gesplittet[index+7]);
                        double tempAd2 = Double.parseDouble(gesplittet[index+8]);
                        double tempAussen = Double.parseDouble(gesplittet[index+9]);
                        double tempInnen = Double.parseDouble(gesplittet[index+10]);
                        String tempKommentar = gesplittet[index+11];
                        index += 12;
                        
                        Trennen.safeTrennstelle(new Kabelwerte(tempAuftragsnummer, tempDatum, tempName, tempInnenaussen, tempAd1Ad2, tempTrennstelle, tempKabellaenge, tempAd1, tempAd2, tempAussen, tempInnen, tempKommentar)); 
                        lstAuswahl.add(String.format("%5s %10s %9s m %9s m", tempDatum, tempAuftragsnummer, f.format(tempKabellaenge), h.format(tempTrennstelle)));
                    }
                }
            }
            catch(IOException x){
                JOptionPane.showMessageDialog(this, "Datei fehlerhaft", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        //Aktion I Löschen der kompletten Liste
        if(e.getSource() == allesLoeschen){
            lstAuswahl.removeAll();
            Trennen.clearTrennstelle();
            speichern.setEnabled(false);
            speichernUnter.setEnabled(false);
            allesLoeschen.setEnabled(false);
            speichernUnterOK = false;
        }
        //Aktion J Informationen zum Programm
        if(e.getSource() == ueberDasProgramm){
            DUeberDasProgramm = new JOptionPane();
            DUeberDasProgramm.showMessageDialog(this, String.format("Autor: Nico Korn%nVersion: 1.2 vom 12.09.2013%nProgrammiert mit BlueJ%nGewrappt mit JSmooth"), "Über das Programm", JOptionPane.PLAIN_MESSAGE);
        }
        repaint();
    }
    
    /**
     * ItemListener
     */
    public void itemStateChanged(ItemEvent e){
        if(e.getSource() == lstAuswahl){
            if(lstAuswahl.getSelectedIndexes().length == 0){
                btnLoeschen.setEnabled(false);
                btnDrucken.setEnabled(false);
            }else{
                btnLoeschen.setEnabled(true);
                btnDrucken.setEnabled(true);
            }
            if(lstAuswahl.getSelectedIndexes().length == 0 || lstAuswahl.getSelectedIndexes().length > 1 ){
                btnAendern.setEnabled(false);
            }else{
                btnAendern.setEnabled(true);
            }
        }
    }
    
    /**
     * Paint
     */
    public void paintComponent(Graphics g){
        g.drawRect(160,5,480,20);
    }
    
    /**
     * WindowListener Methoden
     */
    public void windowClosing(WindowEvent event){
        System.exit(0);
    }
    public void windowIconified(WindowEvent event){ 
    }
    public void windowOpened(WindowEvent event){
    }
    public void windowClosed(WindowEvent event){
    }
    public void windowActivated(WindowEvent event){
    }
    public void windowDeiconified(WindowEvent event){
    }
    public void windowDeactivated(WindowEvent event){
    }
}