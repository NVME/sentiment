/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlpmodelgen;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import java.util.ArrayList;
import java.util.Properties;
import org.ejml.simple.SimpleMatrix;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author luya
 */
public class TryOut {

    static StanfordCoreNLP pipeline;

    public static void init() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse,sentiment");
        props.put("sentiment.model", "hpnnmodel.ser.gz");
        pipeline = new StanfordCoreNLP(props);
    }

    public static void findSentiment(String msg) {


        int mainSentiment = 0;
        if (msg != null && msg.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(msg);
            for (CoreMap sentence : annotation
                    .get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence
                        .get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                int sentimentScore = RNNCoreAnnotations.getPredictedClass(tree);
                String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
                SimpleMatrix sentimentCoefficients = RNNCoreAnnotations.getPredictions(tree);
                Map<String, Double> map = new HashMap<String, Double>();
                double veryNegative = sentimentCoefficients.get(0);
                map.put("veryNegative", veryNegative);

                double negative = sentimentCoefficients.get(1);
                map.put("negative", negative);

                double neutral = sentimentCoefficients.get(2);
                map.put("neutral", neutral);

                double positive = sentimentCoefficients.get(3);
                map.put("positive", positive);

                double veryPositive = sentimentCoefficients.get(4);
                map.put("veryPositive", veryPositive);
                String result = "";
                double score = 0.00;
                for (Map.Entry<String, Double> entry : map.entrySet()) {
                    if (entry.getValue() > score) {
                        score = entry.getValue();
                        result = entry.getKey();
                    }
                    System.out.println(entry.getKey() + ":" + entry.getValue());
                }

                System.out.println("result-->>>>>>" + result);

            }
        }
    }
}
