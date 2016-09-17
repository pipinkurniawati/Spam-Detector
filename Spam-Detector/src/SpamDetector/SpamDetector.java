/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpamDetector;
import java.io.FileReader;
import java.util.Arrays;
import com.opencsv.CSVReader;
import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;
import weka.classifiers.trees.SimpleCart;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;

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
    public static void main(String[] args) throws Exception {
      CSVReader reader = new CSVReader(new FileReader("notspam.csv"), ',' , '"' , 2);
       
      //Read all rows at once
      List<String[]> allRows = reader.readAll();
      
      Preprocessor prepocessor = new Preprocessor();
      
      ArrayList<ArrayList<String>> notSpam = new ArrayList<>();
      

     for(String[] row : allRows){
        //Add processed sentences
        notSpam.add(
            prepocessor.processSentence(Arrays.toString(row)));
        
        //Print
        //System.out.println(notSpam.get(notSpam.size()-1));
     }
     
     BufferedReader br = new BufferedReader(
                              new FileReader("weather.numeric.arff"));

     ArffReader arff = new ArffReader(br);
     Instances data = arff.getData();
     data.setClassIndex(data.numAttributes() - 1);
     
     SimpleCart tree = new SimpleCart();
     tree.buildClassifier(data);
     
     
   }
    
}
