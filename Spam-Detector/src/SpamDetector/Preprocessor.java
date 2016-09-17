/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpamDetector;
import IndonesianNLP.IndonesianSentenceFormalization;
import IndonesianNLP.IndonesianSentenceTokenizer;
import IndonesianNLP.IndonesianStemmer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
/**
 *
 * @author jessica
 */
public class Preprocessor {
    private final IndonesianSentenceFormalization formalizer = new IndonesianSentenceFormalization();
    private final IndonesianSentenceTokenizer tokenizer = new IndonesianSentenceTokenizer(); 
    private final IndonesianStemmer stemmer = new IndonesianStemmer();
    
    public Preprocessor(){
        formalizer.initStopword();
    }  
    
    public ArrayList<String> processSentence(String message){
        return tokenizer.tokenizeSentence(
            formalizer.deleteStopword(
                stemmer.stemSentence(
                    formalizer.normalizeSentence(message))));
        
    }
    
    public void writeToFile(ArrayList<String> data, String filepath) {
        try {
            File file = new File(filepath);
            file.getParentFile().mkdirs();
            System.out.println("Writing to " + file.getCanonicalPath());
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter writer = new BufferedWriter(fw);
            for(String d : data) { 
                writer.write(d);
                writer.write(" ");
            }
            writer.close();
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
}
