package lpa.aumont.cercadom.model;

/**
 * Created by max on 11/12/2014.
 *
 * // Query URL : https://spreadsheets.google.com/feeds/list/PUT-KEY-HERE/od6/public/values?alt=json
 */
public class Question {
    private String question;
    private String[] badResponses;
    private String goodResponse;
    private String urlImage;

    public Question(String question, String goodResponse, String[] badResponses,String urlImage) {

        this.question = question;
        this.goodResponse = goodResponse;
        this.badResponses = badResponses;
        this.urlImage = urlImage;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getBadResponses() {
        return badResponses;
    }

    public String getGoodResponse() {
        return goodResponse;
    }

    public String getUrlImage() {
        return urlImage;
    }
}
