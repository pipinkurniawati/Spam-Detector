/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpamDetector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Acer
 */
public class FeatureExtraction {
    private final static String ARFF_PATH = "data.arff";
    
    public ArrayList<String> generateAttribute (ArrayList<ArrayList<String>> spam, ArrayList<ArrayList<String>> notSpam) {        
        HashMap<String, Double> tfidfSpam = new HashMap<>(); 
        for (ArrayList<String> msg : spam) {
            for (String term: msg) {
                tfidfSpam.put(term, tfidf(spam, msg, term));
            }
        }
        HashMap<String, Double> tfidfNotSpam = new HashMap<>(); 
        for (ArrayList<String> msg : notSpam) {
            for (String term: msg) {
                tfidfNotSpam.put(term, tfidf(notSpam, msg, term));
            }
        }
        
        Map<String, Double> sortedSpam = sortByValue(tfidfSpam);
        Map<String, Double> sortedNotSpam = sortByValue(tfidfNotSpam);
        ArrayList<String> spamAttr = getNAttribute(sortedSpam, 500);
        ArrayList<String> notSpamAttr = getNAttribute(sortedNotSpam, 500);

        for (int i=0; i<spamAttr.size(); i++) {
            for (int j=0; j<notSpamAttr.size(); j++) {
                if (spamAttr.get(i).equals(notSpamAttr.get(j))) {
                    spamAttr.remove(i);
                    break;
                }
            }
        }
        
        return spamAttr;
    }
    
    public ArrayList<ArrayList<Integer>> generateData(ArrayList<String> attributes, ArrayList<ArrayList<String>> messages, boolean spam) {
        ArrayList<ArrayList<Integer>> dataList = new ArrayList<>();
        for (ArrayList<String> msg : messages ){
            ArrayList<Integer> data = new ArrayList<>();
            for (String attr : attributes) {
                boolean found = false;
                for (String word : msg) {
                    if (attr.equals(word)) {
                        found = true;
                        break;
                    }
                }
                if (found){
                    data.add(1);
                }
                else {
                    data.add(0);
                }
            }
            if (spam) {
                data.add(1);
            } else {
                data.add(0);
            }
            dataList.add(data);
        }
        return dataList;
    }
    
    public void generateArff(ArrayList<ArrayList<String>> spam, ArrayList<ArrayList<String>> notSpam) {
        ArrayList<String> attr = generateAttribute(spam, notSpam);
        ArrayList<ArrayList<Integer>> data = generateData(attr, notSpam, false);
        data.addAll(generateData(attr, spam, true));
        
        try {
            File file = new File(ARFF_PATH);
            if (!file.exists()) {
                    file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("@relation spamfilter\n\n");

            for (int i=0; i<attr.size(); i++) {
                bw.write("@attribute " + attr.get(i) + " {no,yes}\n");
            }
            bw.write("@attribute spam {yes, no}");

            bw.write("\n\n@data\n");
            for (int i=0; i<data.size(); i++) {
                for (int j=0; j<data.get(i).size()-1; j++) {
                    if (data.get(i).get(j)==1){
                        bw.write("yes,");
                    }
                    else {
                        bw.write("no,");
                    }
                }
                if (data.get(i).get(data.get(i).size()-1)==1) {
                    bw.write("yes\n");
                }
                else {
                    bw.write("no\n");
                }
                
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double occurenceCounter(ArrayList<String> msg, String term) {
        double counter= 0;
        for (String word : msg) {
           if (term.equals(word)) {
                  counter++;
           }
        }
        return counter;
    }
    
    public double termFrequency(ArrayList<String> msg, String term) {
        return (double)occurenceCounter(msg, term)/msg.size();
    }
    
    public double inverseDocumentFrequency(ArrayList<ArrayList<String>> messages, String term) {
        double counter = 0;
        for (ArrayList<String> msg : messages) {
            for (String word : msg) {
                if (term.equals(word)) {
                    counter++;
                    break;
                }
            }
        }
        return Math.log(messages.size()/counter);
    }
    
    public double tfidf(ArrayList<ArrayList<String>> messages, ArrayList<String> msg, String term) {
        return termFrequency(msg, term)*inverseDocumentFrequency(messages, term);
    }
    
    private static Map<String, Double> sortByValue(Map<String, Double> unsortMap) {

        // 1. Convert Map to List of Map
        List<Map.Entry<String, Double>> list =
                new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1,
                               Map.Entry<String, Double> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
    
    public ArrayList<String> getNAttribute(Map<String, Double> map, int n) {
        ArrayList<String> attr = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            attr.add(entry.getKey());
            i++;
            if (i > n) break;
        }
        return attr;
    }
    
    public static <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey()
                    + " Value : " + entry.getValue());
        }
    }
}
