package by.lingvocentr.senta.twitata;

import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 *
 * @author Jauhien Klimovich
 * @date Jan 20, 2013
 */
public class Runner {


    public static void main(String[] args) throws JsonSyntaxException, IOException, ClassNotFoundException {
        QueryController controller = new QueryController();
        final Map<SentimentTag, List<ClassifiedBean>> processRequest = controller.processRequest("футбол");
        
        for (SentimentTag sentimentTag : processRequest.keySet()) {
            System.out.println(sentimentTag.name());
            for (ClassifiedBean classifiedBean : processRequest.get(sentimentTag)) {
                System.out.println(classifiedBean.getContent());
            }
        }
        System.out.println(processRequest.values().toArray());
    }
}
