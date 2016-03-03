/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eindopdracht;

/**
 * Geimporeerde modules voor het gebruiken van het programma.
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.*;

/**
 * @author Charlotte
 * @version 1.0
 * 2-3-2016
 */
public class AnalysePubMed {

    /**
     * Files voor de in te lezen bestanden.
     */
    public File file1 = new File("C:/Users/Charlotte/Desktop/taxID_to_organism_names.txt"); // Organisme namen en taxid's
    public File file2 = new File("C:/Users/Charlotte/Desktop/gene2pubmed"); // Taxid's met pubmedid's en genid's 
    public File file3 = new File("C:/Users/Charlotte/Desktop/geninfo.txt"); // File met volledige geninfo 

    public HashMap ot = new HashMap<String, Integer>(); // Hashmap met organismenaam als key en taxid als value
    public HashMap tp = new HashMap<Integer, HashSet<Integer>>(); // Hashmap met taxid als key en pubmedid als value
    public HashMap pg = new HashMap<Integer, HashSet<Integer>>(); //Hashmap met pubmedid als key en genid als value
    public HashMap geninfo = new HashMap<Integer, Gene>(); // Hashmap met genid als key en de gene objecten als value
    public PMCApp gui = new PMCApp(); // Aanroepen van GUI uit de class PMCApp

    public Graphics paper;
    public Gene gene;
    public String keuze1;
    public String keuze2;
    public String keuze3;
    public String pubidstring1;
    public String pubidstring2;
    public int taxidprint1;
    public int taxidprint2;
    public float aantal1;
    public float aantal2;
    public String overlapstring;
    public float overlap;
    public float totaal;
    public float uniek1percent;
    public float uniek2percent;
    public float overlappercent;
    public String taxstring1;
    public String taxstring2;
    public String[] overlapids;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        AnalysePubMed analyse = new AnalysePubMed();
        analyse.taxpubgen(); // Aanroepen van de eerste methode
    }
/**
 * Taxpubgen leest file 2 en maakt de hashmaps tp en pg.
 */
    public void taxpubgen() {
        HashSet tempList = null;
        HashSet tempList2 = null;
        System.out.println("Pubmedid's en genid's worden geladen...");
        BufferedReader lezen2 = null;
        try {
            lezen2 = new BufferedReader(new FileReader(file2)); // Lezen van de file 2, met taxids, genids en pubids
            String line2;
            int count = 0;
            while ((line2 = lezen2.readLine()) != null) {
                if (count != 0) { // Eerste regel overslaan
                    String[] gesplit2 = line2.split("\t");
                    int taxid = Integer.parseInt(gesplit2[0]);
                    int pubid = Integer.parseInt(gesplit2[2]);
                    int genid = Integer.parseInt(gesplit2[1]);
                    if (tp.containsKey(taxid)) { // Aanmaken van de hashset voor meerdere values voor één key voor hashmap tp
                        tempList = (HashSet) tp.get(taxid);
                        if (tempList == null) {
                            tempList = new HashSet<Integer>();
                        }
                        tempList.add(pubid);
                    } else {
                        tempList = new HashSet<Integer>();
                        tempList.add(pubid);
                        tp.put(taxid, tempList);
                    }
                    if (pg.containsKey(pubid)) { //Aanmaken van de hashset voor meerdere values voor één key voor hashmap pg
                        tempList2 = (HashSet) pg.get(pubid);
                        if (tempList2 == null) {
                            tempList2 = new HashSet<Integer>();
                        }
                        tempList2.add(genid);
                    } else {
                        tempList2 = new HashSet<Integer>();
                        tempList2.add(genid);
                        pg.put(pubid, tempList2);
                    }
                }
                count++;
            }
            lezen2.close();

        } catch (FileNotFoundException e) {
            System.out.println("File niet gevonden!");
        } catch (IOException i) {
            System.out.println("Onbekende fout, probeer het opnieuw!");
        }
        orgtax();
    }
/**
 * Orgtax leest file 1 en zorgt er voor dat de hashmap ot wordt aangemaakt en de organismenamen in de eerste twee dropdownmenu's worden gezet. 
 */
    public void orgtax() {
        System.out.println("Het scherm wordt geladen...");
        BufferedReader lezen1 = null;
        try {
            lezen1 = new BufferedReader(new FileReader(file1)); // Lezen van file 1 met taxid en organismenamen
            String line1;
            System.out.println("Organismen laden...");
            while ((line1 = lezen1.readLine()) != null) {
                String[] gesplit1 = line1.split("\t");
                if (tp.containsKey(Integer.parseInt(gesplit1[0]))) { // Alleen de organismen inladen die ook voorkomen in file 2
                    int taxid = Integer.parseInt(gesplit1[0]);
                    ot.put(gesplit1[1], taxid);
                    PMCApp.choice1.add(gesplit1[1]); // Organismenamen toevoegen aan de dropdownmenu's 
                    PMCApp.choice2.add(gesplit1[1]);
                }

            }
            lezen1.close();

        } catch (FileNotFoundException e) {
            System.out.println("File niet gevonden!");
        } catch (IOException i) {
            System.out.println("Onbekende fout, probeer het opnieuw!");
        }
        System.out.println("Organismen geladen. Het scherm wordt geopend");
        run();
    }
/**
 * Run zorgt ervoor dat het venster van de GUI opent en er meegewerkt kan worden.
 */
    public void run() {
        gui.setVisible(true);
        keuze();
    }
/**
 * Keuze verkrijgt de keuze van de eerste twee dropdownmenu's en verkrijgt vervolgens de taxid's en de pubmedid's.
 */
    public void keuze() {
        System.out.println("Id's geladen.");
        PMCApp.choice1.addItemListener(new ItemListener() { // Wanneer het eerste dropdownmenu veranderd van keuze wordt de keuze doorgegeven
            public void itemStateChanged(ItemEvent evt1) {
                try { // Via de keuze worden de taxid en pubid uit de hashmaps gehaald
                    keuze1 = PMCApp.choice1.getSelectedItem();
                    taxstring1 = ot.get(keuze1).toString();
                    taxidprint1 = Integer.parseInt(taxstring1);
                    pubidstring1 = tp.get(taxidprint1).toString();

                } catch (NullPointerException np1) {
                    System.out.println("Dit organisme is niet opgenomen in een PubMed artikel. Probeer een ander organisme.");
                }

            }
        });
        PMCApp.choice2.addItemListener(new ItemListener() { // Wanneer het tweede dropdownmenu veranderd van keuze wordt de keuze doorgegeven
            public void itemStateChanged(ItemEvent evt2) {
                try { // Via de keuze worden de taxid en pubid uit de hashmaps gehaald
                    keuze2 = PMCApp.choice2.getSelectedItem();
                    taxstring2 = ot.get(keuze2).toString();
                    taxidprint2 = Integer.parseInt(taxstring2);
                    pubidstring2 = tp.get(taxidprint2).toString();

                } catch (NullPointerException np2) {
                    System.out.println("Dit organisme is niet opgenomen in een PubMed artikel. Probeer een ander organisme.");
                }
            }

        });
        berekenen();

    }

    /**
     * Berekenen zorgt voor het uitrekenen van de unieke artikelen en de overlap. Ook tekent het een Venn-diagram.
     */
    public void berekenen() {
        PMCApp.jButton1.addActionListener(new ActionListener() { // Na het indrukken van de "Berekenen" button worden er de volgende acties uitgevoerd
            public void actionPerformed(ActionEvent evt3) {
                paper = PMCApp.jPanel1.getGraphics(); // De Venn-diagram wordt getekent, helaas gaat deze na het ophalen van geninfo weer weg, wat niet de bedoeling is
                paper.clearRect(0, 0, 300, 300);
                paper.drawOval(0, 20, 150, 150);
                paper.drawOval(75, 20, 150, 150);
                aantal1 = pubidstring1.split("\\w+").length - 1; // Berkenen van het aantal artikelen
                aantal2 = pubidstring2.split("\\w+").length - 1;

                String[] pubids1 = pubidstring1.replaceAll("[.,]", "").split(" ");
                Set<String> overlaphash = new HashSet<String>();
                String tweedezin = "";
                for (String word : pubids1) { // Overlap tussen organismen bepalen
                    if (tweedezin.isEmpty()) {
                        tweedezin = word;
                    } else {
                        tweedezin = tweedezin + " " + word;
                    }
                    if (pubidstring2.contains(word)) {
                        overlaphash.add(word);
                        if (pubidstring2.contains(tweedezin)) {
                            overlaphash.add(tweedezin);
                        }
                    } else {
                        tweedezin = "";
                    }
                    overlapstring = overlaphash.toString();

                }
                overlap = overlapstring.split("\\w+").length - 1; // Aantal artikelen met overlap berekenen

                PMCApp.jTextArea2.setText(
                        "Aantal artikelen in Pubmed met:" + "\n" + keuze1 + " : " + aantal1
                        + "\n" + keuze2 + " : " + aantal2 + "\nOverlap: " + overlap);

                totaal = aantal1 + aantal2 + overlap;
                uniek1percent = (aantal1 / totaal * 100);
                uniek2percent = (aantal2 / totaal * 100);
                overlappercent = (overlap / totaal * 100);

                paper.setColor(Color.red);
                paper.drawString(keuze1 + ": ", 0, 10);
                paper.drawString(" " + aantal1, 30, 85);
                paper.drawString(" " + uniek1percent + "%", 1, 100);
                paper.setColor(Color.BLUE);
                paper.drawString(keuze2 + ": ", 90, 190);
                paper.drawString(" " + aantal2, 170, 85);
                paper.drawString(" " + uniek2percent + "%", 150, 100);
                paper.setColor(Color.BLACK);
                paper.drawString(" " + overlap, 105, 85);
                paper.drawString(" " + overlappercent + "%", 75, 100);

                geninf();
            }
        }
        );
    }
/**
 * Geninf leest file 3 en maakt een hashmap met genid en gene objecten.
 */
    public void geninf() {
        System.out.println("Geninfo wordt geladen...");
        overlapids = overlapstring.replaceAll("[^a-zA-Z0-9,]+", "").split(",");
        for (String ids : overlapids) { // De pubids met overlap in het derde dropdownmenu plaatsen
            PMCApp.choice3.add(ids);
        }
        System.out.println("Geninfo geladen.");
        BufferedReader lezen3 = null;
        try {
            lezen3 = new BufferedReader(new FileReader(file3)); // Lezen van file 3 met uitgebreide informatie over de genen
            String line3;
            int count = 0;
            while ((line3 = lezen3.readLine()) != null) {
                if (count != 0) { // Overslaan van de eerste regel
                    String[] gesplit3 = line3.split("\t");
                    int taxid = Integer.parseInt(gesplit3[0]);
                    int geneid = Integer.parseInt(gesplit3[1]);
                    String genesymbol = gesplit3[2];
                    String description = gesplit3[8];
                    if (gesplit3[0].equals(taxstring1) || gesplit3[0].equals(taxstring2)) { // Wanneer het taxid van het gekozen organisme overeenkomt met
                        geninfo.put(geneid, new Gene(geneid, genesymbol, taxid, description));// de taxid's in de file zal het worden opgeslagen in een hashmap
                    }

                }
                count++;

            }
            lezen3.close();
        } catch (FileNotFoundException e) {
            System.out.println("File niet gevonden!");
        } catch (IOException i) {
            System.out.println("Onbekende fout, probeer het opnieuw!");
        }
        tabel();
    }
    
    /**
     * Tabel zou keuze3 moeten verkrijgen en vervolgens via de twee hashmaps pf en geninfo de informatie over de genen weer moeten geven 
     * en in de tabel moeten zetten.
     */

    public void tabel() { // De keuze van het derde dropdown menu, met de pubmedids, zou hier aangeroepen moeten worden en vervolgens de gegeven in de tabel moeten zetten
        PMCApp.choice3.addItemListener(new ItemListener() { // Dit werkt echter nog niet
            public void itemStateChanged(ItemEvent evt3) {
                keuze3 = PMCApp.choice3.getSelectedItem();
                System.out.println(keuze3);
                String genstring = pg.get(keuze3).toString();
                int genprint = Integer.parseInt(genstring);
                String geninfprint = geninfo.get(genprint).toString();
                System.out.println(geninfprint);

            }
        });

    }
}
