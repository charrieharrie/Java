/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eindopdracht;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author Charlotte
 */
public class AnalysePubMed {

    public static File file1 = new File("C:/Users/Charlotte/Desktop/taxorgklein.txt"); // Organisme namen en taxid's
    public static File file2 = new File("C:/Users/Charlotte/Desktop/gene2pub.txt"); // Taxid's met pubmedid's en genid's 
    public static File file3 = new File("C:/Users/Charlotte/Desktop/geninfo.txt"); // File met volledige geninfo 
    public static HashMap ot = new HashMap<String, Integer>(); // Hashmap met organismenaam als key en taxid als value
    public static HashMap tp = new HashMap<Integer, HashSet<Integer>>(); // Hashmap met taxid als key en pubmedid als value
    public static HashMap pg = new HashMap<Integer, HashSet<Integer>>(); // Hashmep met pubmedid als key en genid als value
    public static Gene gene;
    public static String keuze1;
    public static String keuze2;
    public static PMCApp gui = new PMCApp();
    public static int taxidprint1;
    public static int taxidprint2;
    public static int pubidprint1;
    public static int pubidprint2;

    public static void main(String[] args) throws FileNotFoundException, IOException {

        orgtax();
        run();
        taxpubgen();
        keuze();
        berekenen();

    }

    public static void orgtax() {
        System.out.println("Het scherm wordt geladen...");
        BufferedReader lezen1 = null;
        try {
            lezen1 = new BufferedReader(new FileReader(file1));
            String line1;
            System.out.println("Organismen laden...");
            while ((line1 = lezen1.readLine()) != null) {
                String[] gesplit1 = line1.split("\t");
                int taxid = Integer.parseInt(gesplit1[0]);
                ot.put(gesplit1[1], taxid);
                PMCApp.choice1.add(gesplit1[1]);
                PMCApp.choice2.add(gesplit1[1]);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File niet gevonden!");
        } catch (IOException i) {
            System.out.println("Onbekende fout, probeer het opnieuw!");
        }
        System.out.println("Organismen geladen. Het scherm wordt geopend");
    }

    public static void run() {
        gui.setVisible(true);
    }

    public static void taxpubgen() {
        System.out.println("Pubmedid's en genid's worden geladen...");
        BufferedReader lezen2 = null;
        try {
            lezen2 = new BufferedReader(new FileReader(file2));
            String line2;
            while ((line2 = lezen2.readLine()) != null) {
                String[] gesplit2 = line2.split("\t");
                int taxid = Integer.parseInt(gesplit2[0]);
                int genid = Integer.parseInt(gesplit2[1]);
                int pubid = Integer.parseInt(gesplit2[2]);
                tp.put(taxid, pubid);
                pg.put(pubid, genid);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File niet gevonden!");
        } catch (IOException i) {
            System.out.println("Onbekende fout, probeer het opnieuw!");
        }

    }

    public static void keuze() {
        System.out.println("Id's geladen.");
        PMCApp.choice1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt1) {
                keuze1 = PMCApp.choice1.getSelectedItem();
                System.out.println("Organisme 1: " + keuze1);
                String taxstring1 = ot.get(keuze1).toString();
                int taxidprint1 = Integer.parseInt(taxstring1);
                System.out.println("Taxid: " + taxidprint1);
                String pubstring1 = tp.get(taxidprint1).toString();
                int pubidprint1 = Integer.parseInt(pubstring1);
                System.out.println("Pubmedid 1: " + pubidprint1);

            }
        });

        PMCApp.choice2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt2) {
                keuze2 = PMCApp.choice2.getSelectedItem();
                System.out.println("Organisme 2: " + keuze2);
                String taxstring2 = ot.get(keuze2).toString();
                int taxidprint2 = Integer.parseInt(taxstring2);
                String pubstring2 = tp.get(taxidprint2).toString();
                int pubidprint2 = Integer.parseInt(pubstring2);
                System.out.println("Pubmedid 2: " + pubidprint2);
            }
        });
    }

    public static void berekenen() {

//        pubidprint1 = tp.get(taxidprint1).toString();
//        pubidprint2 = tp.get(taxidprint2).toString();
    }

    public static Gene geninf() {
        BufferedReader lezen3 = null;
        try {
            lezen3 = new BufferedReader(new FileReader(file3));
            String line3;
            while ((line3 = lezen3.readLine()) != null) {
                String[] gesplit3 = line3.split("\t");
                int taxid = Integer.parseInt(gesplit3[0]);
                int genid = Integer.parseInt(gesplit3[1]);
                Gene gene = new Gene(taxid, gesplit3[2], genid, gesplit3[8]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File niet gevonden!");
        } catch (IOException i) {
            System.out.println("Onbekende fout, probeer het opnieuw!");
        }
        return gene;
    }
}
