/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpamDetector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Acer
 */
public class FeatureExtraction {
    
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
        for (ArrayList<
                String> msg : messages ){
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
}
