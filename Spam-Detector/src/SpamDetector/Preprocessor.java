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
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        message = message.toLowerCase();
        ArrayList<String> result = new ArrayList<String>();
        result = tokenizer.tokenizeSentence(
            formalizer.deleteStopword(
                stemmer.stemSentence(
                    formalizer.normalizeSentence(message))));
        for (int i=0; i<result.size(); i++) {
            if (isPhoneNumber(result.get(i))) {
                result.set(i, "_phone_");
            } else if (isJuta(result.get(i))) {
                result.set(i, "1 juta");
            } else if (isRibu(result.get(i))) {
                result.set(i, "1 ribu");
            } else if (isSingleLetter(result.get(i))) {
                result.remove(i);
            }
        }
        result.removeAll(Arrays.asList("", null, "-", ".", ":", ";", "+", "(", ")", "*", "!", "?", ",", "/", "\\", "[", "]", "<", ">", "=", "_", "\""));
        return result;
    }
    
    private static boolean isPhoneNumber (String token) {
        String regex = "([0-9]{8,20})";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(token);
        return (m.matches());
    }
    
    public boolean isJuta(String token) {
        String regex = "([a-z0-9]+)(jt|jta)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(token);
        return (m.matches());
    }
    
    public boolean isRibu(String token) {
        String regex = "([a-z0-9]+)(rb|rbu)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(token);
        return (m.matches());
    }
    
    public boolean isSingleLetter(String token) {
        String regex = "[a-z]|[0-9]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(token);
        return (m.matches());
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
