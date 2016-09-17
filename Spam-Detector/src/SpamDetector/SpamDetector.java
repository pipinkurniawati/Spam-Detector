/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpamDetector;
import java.io.FileReader;
import java.util.Arrays;
import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author jessica
 */
public class SpamDetector {
    private static String SPAM_PATH;
    private static String NOT_SPAM_PATH;
    private static String ARFF_PATH;
    
    public SpamDetector() {
        SPAM_PATH = new String("spam.csv");
        NOT_SPAM_PATH = new String("notspam.csv");
        ARFF_PATH = new String("data.arff");
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        ArrayList<ArrayList<String>> notSpam = processCSV("notspam.csv");
        ArrayList<ArrayList<String>> spam = processCSV("spam.csv");
     
        // Cobain doang
        FeatureExtraction fe = new FeatureExtraction();
        ArrayList<String> attr = fe.generateAttribute(notSpam, spam);
        ArrayList<ArrayList<Integer>> data = fe.generateData(attr, notSpam, false);
        data.addAll(fe.generateData(attr, spam, true));
        System.out.println(attr.size());
   }
    
   public static ArrayList<ArrayList<String>> processCSV(String path) throws FileNotFoundException, IOException {
        CSVReader reader = new CSVReader(new FileReader(path), ',' , '"' , 2);

        //Read all rows at once
        List<String[]> allRows = reader.readAll();
        Preprocessor prepocessor = new Preprocessor();
        ArrayList<ArrayList<String>> msg = new ArrayList<>();

        for(String[] row : allRows){
           //Add processed sentences
           msg.add(
               prepocessor.processSentence(Arrays.toString(row)));

           //Print
           System.out.println(msg.get(msg.size()-1));
        }

        return msg;
   }
    
}
