package by.lingvocentr.senta.twitata;

import com.google.gson.JsonSyntaxException;

import java.io.IOException;
/**
 *
 * @author Jauhien Klimovich
 * @date Jan 20, 2013
 */
public class Runner {


    public static void main(String[] args) throws JsonSyntaxException, IOException, ClassNotFoundException {
        Service controller = new Service();
//        final Map<SentimentTag, List<ClassifiedBean>> processRequest = controller.processRequest("футбол");
//
//        for (SentimentTag sentimentTag : processRequest.keySet()) {
//            System.out.println(sentimentTag.name());
//            for (ClassifiedBean classifiedBean : processRequest.get(sentimentTag)) {
//                System.out.println(classifiedBean.getContent());
//            }
//        }
//        System.out.println(processRequest.values().toArray());
    }
}
