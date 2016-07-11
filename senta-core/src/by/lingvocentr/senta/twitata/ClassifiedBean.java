package by.lingvocentr.senta.twitata;

import java.io.Serializable;

/**
 *
 * @author Jauhien Klimovich
 * @date Jan 20, 2013
 */
public class ClassifiedBean implements Serializable{

    private SentimentTag tag;
    private String socialNetType;
    private String content;
    private String urlContent;
    private String urlUserImage;
    private String score;

    public SentimentTag getTag() {
        return tag;
    }

    public void setTag(SentimentTag tag) {
        this.tag = tag;
    }

    public String getSocialNetType() {
        return socialNetType;
    }

    public void setSocialNetType(String socialNetType) {
        this.socialNetType = socialNetType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrlContent() {
        return urlContent;
    }

    public void setUrlContent(String urlContent) {
        this.urlContent = urlContent;
    }

    public String getUrlUserImage() {
        return urlUserImage;
    }

    public void setUrlUserImage(String urlUserImage) {
        this.urlUserImage = urlUserImage;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

}
