/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SpamDetector;
import java.io.FileReader;
import java.util.Arrays;
 
import com.opencsv.CSVReader;

/**
 *
 * @author jessica
 */
public class SpamDetector {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {


      //Build reader instance
      //Read data.csv
      //Default seperator is comma
      //Default quote character is double quote
      //Start reading from line number 2 (line numbers start from zero)
      CSVReader reader = new CSVReader(new FileReader("notspam.csv"), ',' , '"' , 3);
       
      //Read CSV line by line and use the string array as you want
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null) {
         if (nextLine != null) {
            //Verifying the read data here
            System.out.println(Arrays.toString(nextLine));
         }
       }
   }
    
}
