package by.lingvocentr.senta.twitata;

/**
 *
 * @author Jauhien Klimovich
 * @date Jan 19, 2013
 */
public enum SentimentTag {

    NEGATIVE("negative"),
    NEUTRAL("neutral"),
    POSITIVE("positive"),
    ;

    String code;
    
    private SentimentTag() {
    }

    private SentimentTag(String code) {
        this.code = code;
    }
    
}
