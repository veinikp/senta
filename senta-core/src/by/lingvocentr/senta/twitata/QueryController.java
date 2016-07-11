package by.lingvocentr.senta.twitata;

import by.lingvocentr.senta.twitata.topsy.TweetBean;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


public class QueryController {

    
    protected Map<SentimentTag, List<ClassifiedBean>> processRequest(String query) throws JsonSyntaxException, IOException, ClassNotFoundException {

        
        SentimentAnalyzer analyzer = new SentimentAnalyzer();

        List<ClassifiedBean> negative = new ArrayList<ClassifiedBean>();
        List<ClassifiedBean> positive = new ArrayList<ClassifiedBean>();
        List<ClassifiedBean> neutral = new ArrayList<ClassifiedBean>();

        List<by.lingvocentr.senta.twitata.topsy.List> list = getListFromJSON(query);

        for (by.lingvocentr.senta.twitata.topsy.List l : list) {
            ClassifiedBean bean = initResultBean(l, analyzer);
            setResultLists(bean, negative, positive, neutral);
        }
        
        Map<SentimentTag, List<ClassifiedBean>> returningMap = new EnumMap<SentimentTag, List<ClassifiedBean>>(SentimentTag.class);
        
        returningMap.put(SentimentTag.NEUTRAL, neutral);
        returningMap.put(SentimentTag.POSITIVE, positive);
        returningMap.put(SentimentTag.NEGATIVE, negative);
        
        return returningMap;
    }

    public static StringBuilder getResponseFromTwitter(String query,
            String apiKey,
            int page) throws IOException {

        long unixTimeStampToday = new Date().getTime();

        // Use the Calendar class to subtract one day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -10);

        // Use the date formatter to produce a formatted date string
        Date previousDate = calendar.getTime();
        long unixTimeStamp10DaysBefore = previousDate.getTime();// - 10*24*60*60;

        String url = "http://otter.topsy.com/"
                + "search.json?"
                + "apikey=" + "4BA6FE4ED5824F958A9C7264457EFFF8"
                + "&q=" + URLEncoder.encode(query, "UTF-8")
                + "&mintime=" + unixTimeStamp10DaysBefore
                + "&maxtime=" + unixTimeStampToday + "&type=tweet&perpage=100&nohidden=0&allow_lang=ru&page=" + page;
        URL u = new URL(url);
        System.out.print("loading page " + page + "  for '" + query + "'..." + url);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(u.openStream()));
        StringBuilder loaded = new StringBuilder();
        String s;
        while ((s = in.readLine()) != null) {
            loaded.append(s).append("\n");
        }
        in.close();
        return loaded;
    }


    private List<by.lingvocentr.senta.twitata.topsy.List> getListFromJSON(String query) throws JsonSyntaxException, IOException {
        Gson gson = new Gson();
        final String testQ = getResponseFromTwitter(query, null, 1).toString();
        TweetBean req = gson.fromJson(testQ, TweetBean.class);
        final List<by.lingvocentr.senta.twitata.topsy.List> list = req.getResponse().getList();
        return list;
    }

    private ClassifiedBean initResultBean(by.lingvocentr.senta.twitata.topsy.List l, SentimentAnalyzer analyzer) throws ClassNotFoundException, IOException {
        ClassifiedBean bean = new ClassifiedBean();
        bean.setContent(l.getContent());
        bean.setScore(l.getScore().toString());
        bean.setSocialNetType("twitter");
        bean.setTag(analyzer.getAnalysisOfQuery(l.getContent()));
        bean.setUrlContent(l.getUrl());
        bean.setUrlUserImage(l.getTopsy_author_img());
        return bean;
    }

    private void setResultLists(ClassifiedBean bean, List<ClassifiedBean> negative, List<ClassifiedBean> positive, List<ClassifiedBean> neutral) {
        switch (bean.getTag()) {
            case NEGATIVE: {
                negative.add(bean);
                break;
            }
            case POSITIVE: {
                positive.add(bean);
                break;
            }
            case NEUTRAL: {
                neutral.add(bean);
                break;
            }
        }
    }
}
