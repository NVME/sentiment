/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlpmodelgen;

import edu.stanford.nlp.sentiment.BuildBinarizedDataset;
import edu.stanford.nlp.sentiment.SentimentTraining;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luya
 */
public class NLPModelGen {

    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            SentenceGen.build(args);
        } catch (Exception ex) {
            Logger.getLogger(NLPModelGen.class.getName()).log(Level.SEVERE, null, ex);
        }
        String inputFile = "binarizedDataset_input.csv";
        String[]  input = {"-input", inputFile};
        BuildBinarizedDatasetGen.build(input);
        String[] input1= {"-numHid","25","-trainPath", "BinaryDataset.txt","-train","-model","hpnnmodel.ser.gz"};
          SentimentTraining.main(input1);
    }
}
