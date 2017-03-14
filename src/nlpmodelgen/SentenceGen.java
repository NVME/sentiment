/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlpmodelgen;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author luya
 */
public class SentenceGen {
    
    	public static void build(String[] args) throws Exception{

        System.out.print("Processing. This will take about 30 mins or os...");

        // Input
        String dataFileName = "train.csv";
        BufferedReader bReader = new BufferedReader(
                new FileReader(dataFileName));

        // Output
        File fout = new File("binarizedDataset_input.csv");
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        HashMap allPhrases = new HashMap();
        HashMap localPhrases = new HashMap();

        String line;

        // skip the first line.
        bReader.readLine();

        Integer currentSentenceId = 0;
        int countP = 0;


        boolean skip = false;
        String sentence = "";
        while ((line = bReader.readLine()) != null) {

            // Splitting the content of tabbed separated line
            // PhraseId	SentenceId	Phrase	Sentiment
            String datavalue[] = line.split("\t");
            String phraseId = datavalue[0];
            Integer sentenceId = Integer.parseInt(datavalue[1]);
            String phrase = datavalue[2];
            Integer sentiment = Integer.parseInt(datavalue[3]);

            if(!currentSentenceId.equals(sentenceId)){

                // new sentence starting.
                Iterator it = allPhrases.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pairs = (Map.Entry) it.next();

                    String key = pairs.getKey().toString().toLowerCase();
                    String[] words = sentence.split(" ");

                    HashMap doneWords = new HashMap();
                    if(key.split(" ").length == 1) {

                        for (String w : words) {

                            String wor = w.toLowerCase();
                            if (wor.equals(key)) {
                                if (!doneWords.containsKey(w)) {
                                    doneWords.put(w, null);
                                    if (!localPhrases.containsKey(key)) {
                                        bw.write(pairs.getValue() + " " + w);
                                        bw.newLine();
                                    }
                                }

                            }
                        }
                    }
                    else if(key.split(" ").length > 1){
                        if(sentence.contains(key)){
                            if(!localPhrases.containsKey(key)){
                                if(pairs.getKey()==" "){
                                    System.out.println();
                                }

                                bw.write(pairs.getValue() + " " + pairs.getKey());
                                bw.newLine();
                            }
                        }
                    }

                }

                sentence = phrase;
                localPhrases = new HashMap();

                if(skip==false){
                    bw.newLine();
                }
            }


            currentSentenceId = sentenceId;


            if(skip==false) {

                allPhrases.put(phrase,sentiment);
                if (!phrase.equals(" ")) {


                    localPhrases.put(phrase, sentiment);
                    bw.write(sentiment + " " + phrase);
                    bw.newLine();
                    countP++;
                }

            }

        }

        bReader.close();
        bw.close();
    }
    
}
