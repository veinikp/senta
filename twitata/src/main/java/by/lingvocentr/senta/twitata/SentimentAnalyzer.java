package by.lingvocentr.senta.twitata;

import edu.stanford.nlp.classify.ColumnDataClassifier;
import edu.stanford.nlp.classify.LinearClassifier;
import edu.stanford.nlp.ling.Datum;
import edu.stanford.nlp.stats.Counter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jauhien Klimovich
 * @date Jan 19, 2013
 */
public class SentimentAnalyzer {

    private static ColumnDataClassifier cdc = new ColumnDataClassifier("/home/local/FREEDOMGS/pveinik/projects/senta/maxent.props");
    private static LinearClassifier<String, String> classifier;

    static {
        try {
            System.out.println("loading sentiment model");
            long time = System.currentTimeMillis();
            ObjectInputStream is;
            is = new ObjectInputStream(new FileInputStream("/home/local/FREEDOMGS/pveinik/projects/senta/all.content.model"));
            classifier = (LinearClassifier<String, String>) is.readObject();
            is.close();
            System.out.println("loaded sentiment model in " + (System.currentTimeMillis() - time) + "mls");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SentimentAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SentimentAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public SentimentTag getAnalysisOfQuery(String text) throws IOException, ClassNotFoundException {
        Datum<String, String> d = cdc.makeDatumFromLine("negative\t" + text, 0);
        String c = classifier.classOf(d);
        Counter<String> counter = classifier.probabilityOf(d);
        System.out.println(counter.getCount(c));
        if ("positive".equals(c)) {
            if (counter.getCount(c) <.98) {
                c = "neutral";
            }
        } else if (counter.getCount(c) < .7) {
            c = "neutral";
        }
        return SentimentTag.valueOf(c.toUpperCase());
    }
}
