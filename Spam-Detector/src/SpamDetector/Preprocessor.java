/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpamDetector;
import IndonesianNLP.IndonesianSentenceFormalization;
import IndonesianNLP.IndonesianSentenceTokenizer;
import IndonesianNLP.IndonesianStemmer;
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
    
    public ArrayList<String> processSentence(String sentence){
        return tokenizer.tokenizeSentence(
            formalizer.deleteStopword(
                stemmer.stemSentence(
                    formalizer.normalizeSentence(sentence))));
        
    }
}
