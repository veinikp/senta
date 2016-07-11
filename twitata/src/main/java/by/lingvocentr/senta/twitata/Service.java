package by.lingvocentr.senta.twitata;

import by.lingvocentr.senta.twitata.topsy.TweetBean;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.*;


public class Service {

    
    public static Map<String, Object> processRequest(String query) throws JsonSyntaxException, IOException, ClassNotFoundException {
        Map<String, Object> returningMap = new HashMap<String, Object>();
        SentimentAnalyzer analyzer = new SentimentAnalyzer();
        List<ClassifiedBean> negative = new ArrayList<ClassifiedBean>();
        List<ClassifiedBean> positive = new ArrayList<ClassifiedBean>();
        List<ClassifiedBean> neutral = new ArrayList<ClassifiedBean>();
        long time = System.currentTimeMillis();
        List<by.lingvocentr.senta.twitata.topsy.List> list = getListFromJSON(query);
        returningMap.put("twit_loading_time", NumberFormat.getInstance().format((System.currentTimeMillis() - time)/1000d));
        List<EnumMap<SentimentTag, Integer>> dateRange = new ArrayList<EnumMap<SentimentTag, Integer>>();
        for (int i = 0; i<10; i++) {
            EnumMap<SentimentTag, Integer> e = new EnumMap<SentimentTag, Integer>(SentimentTag.class);
            e.put(SentimentTag.POSITIVE, 0);
            e.put(SentimentTag.NEUTRAL, 0);
            e.put(SentimentTag.NEGATIVE, 0);
            dateRange.add(i, e);
        }
        System.out.println("classifying twits");
        time = System.currentTimeMillis();
        for (by.lingvocentr.senta.twitata.topsy.List l : list) {
            ClassifiedBean bean = initResultBean(l, analyzer);
            setResultLists(bean, negative, positive, neutral);
            int index = (int) ((new Date()).getTime() - bean.getPublicityDate().getTime()) / (1000 * 60 * 60 * 24);
            index = index % 10;
            if (index < 0) {
                index = -index;
            }
            System.out.println("index =" + index);
            if (index > -1 && index < 10) {
                EnumMap<SentimentTag, Integer> prevValues = dateRange.get(index);
                prevValues.put(bean.getTag(), prevValues.get(bean.getTag())+1);
            }
        }
        returningMap.put("classifying_time", NumberFormat.getInstance().format((System.currentTimeMillis() - time)/1000d));
        returningMap.put("hist", dateRange);
        System.out.println("classified lists");

        returningMap.put(SentimentTag.NEUTRAL.name(), neutral);
        returningMap.put(SentimentTag.POSITIVE.name(), positive);
        returningMap.put(SentimentTag.NEGATIVE.name(), negative);
        return returningMap;
    }

    public static StringBuilder getResponseFromFacebook(String query) throws IOException {
        String url = "https://graph.facebook.com/search?q=" + URLEncoder.encode(query, "UTF-8") + "&type=post";
        long time = System.currentTimeMillis();
        System.out.println("loading " + url);
        URL u = new URL(url);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(u.openStream()));
        StringBuilder loaded = new StringBuilder();
        String s;
        while ((s = in.readLine()) != null) {
            loaded.append(s).append("\n");
        }
        in.close();
        System.out.println("loaded in " + (System.currentTimeMillis() - time) + "mls");
        return loaded;
    }

    protected static StringBuilder getResponseFromTwitter(String query,
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
        System.out.println("loading page " + page + "  for '" + query + "'..." + url);
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


    private static List<by.lingvocentr.senta.twitata.topsy.List> getListFromJSON(String query) throws JsonSyntaxException, IOException {
        System.out.println("loading twitter data");
        Gson gson = new Gson();
        List<by.lingvocentr.senta.twitata.topsy.List> list = new ArrayList<by.lingvocentr.senta.twitata.topsy.List>();
        boolean runNext = true;
        for (int i = 1; i<11 && runNext; i++) {
            boolean toLoad = true;
            while (toLoad) {
                long time = System.currentTimeMillis();
                try {
                    final String testQ = getResponseFromTwitter(query, null, i).toString();
                    TweetBean req = gson.fromJson(testQ, TweetBean.class);
                    list.addAll(req.getResponse().getList());
                    runNext = req.getResponse().getList().size() == 100;
                    toLoad = false;
                } catch (Throwable e) {
                    System.err.println(e.getMessage());
                    toLoad = true;
                }
                System.out.println("loaded page " + i + " in " + (System.currentTimeMillis() - time) + "mls");
            }
        }
        System.out.println("loaded twitter data " + list.size());
        return list;
    }

    private static ClassifiedBean initResultBean(by.lingvocentr.senta.twitata.topsy.List l, SentimentAnalyzer analyzer) throws ClassNotFoundException, IOException {
        ClassifiedBean bean = new ClassifiedBean();
        bean.setContent(l.getContent());
        bean.setScore(l.getScore().toString());
        bean.setSocialNetType("twitter");
        bean.setTag(analyzer.getAnalysisOfQuery(l.getContent()));
        bean.setUrlContent(l.getUrl());
        bean.setUrlUserImage(l.getTopsy_author_img());
        Date date = new Date(l.getFirstpost_date().longValue() * 1000);
        bean.setPublicityDate(date);
        return bean;
    }

    private static void setResultLists(ClassifiedBean bean, List<ClassifiedBean> negative, List<ClassifiedBean> positive, List<ClassifiedBean> neutral) {
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
