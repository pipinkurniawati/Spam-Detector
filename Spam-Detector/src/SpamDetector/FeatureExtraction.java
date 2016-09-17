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
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Acer
 */
public class FeatureExtraction {
    private final static String ARFF_PATH = "data.arff";
    
    public ArrayList<String> generateAttribute (ArrayList<ArrayList<String>> spam, ArrayList<ArrayList<String>> notSpam) {
        ArrayList<String> merged = new ArrayList<>();
        for (ArrayList<String> s : spam) {
            merged.addAll(s);
        }
        for (ArrayList<String> s : notSpam) {
            merged.addAll(s);
        }
        
        Set<String> uniqueAttr = new HashSet<>(merged);
        ArrayList<String> attr = new ArrayList<>(uniqueAttr);
        return attr;
    }
    
    public ArrayList<ArrayList<Integer>> generateData(ArrayList<String> attributes, ArrayList<ArrayList<String>> messages, boolean spam) {
        ArrayList<ArrayList<Integer>> dataList = new ArrayList<>();
        for (ArrayList<String> msg : messages ){
            ArrayList<Integer> data = new ArrayList<>();
            for (String word : msg) {
                for (String attr : attributes) {
                    if (attr.equals(msg)) {
                        data.add(1);
                    } else {
                        data.add(0);
                    }
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
                bw.write("@attribute " + attr.get(i) + " {0,1}\n");
            }
            bw.write("@attribute spam {yes, no}");

            bw.write("\n\n@data\n");
            for (int i=0; i<data.size(); i++) {
                for (int j=0; j<data.get(i).size()-1; j++) {
                    bw.write(data.get(i).get(j) + ",");
                }
                if (data.get(i).get(data.get(i).size()-1) == 1) {
                    bw.write("yes\n");
                } else {
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
}
