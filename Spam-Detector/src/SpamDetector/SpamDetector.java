/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpamDetector;
import java.io.FileReader;
import java.util.Arrays;
import com.opencsv.CSVReader;
import java.util.List;
import IndonesianNLP.IndonesianSentenceFormalization;
import IndonesianNLP.IndonesianSentenceTokenizer;
import IndonesianNLP.IndonesianStemmer;
import java.util.ArrayList;

/**
 *
 * @author jessica
 */
public class SpamDetector {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
      CSVReader reader = new CSVReader(new FileReader("notspam.csv"), ',' , '"' , 2);
       
      //Read all rows at once
      List<String[]> allRows = reader.readAll();
      
      IndonesianSentenceFormalization formalizer = new IndonesianSentenceFormalization();
      formalizer.initStopword();
      IndonesianSentenceTokenizer tokenizer = new IndonesianSentenceTokenizer(); 
      IndonesianStemmer stemmer = new IndonesianStemmer();
      
      ArrayList<ArrayList<String>> notSpam = new ArrayList<>();
      
      //Formalisasi
     for(String[] row : allRows){
        notSpam.add(
            tokenizer.tokenizeSentence(
                stemmer.stemSentence(
                    formalizer.deleteStopword(
                        formalizer.normalizeSentence2(Arrays.toString(row))))));
        /*for (String word : notSpam.get(notSpam.size()-1)){
            
        }*/
        
        System.out.println(notSpam.get(notSpam.size()-1));
     }
     
     
   }
    
}
