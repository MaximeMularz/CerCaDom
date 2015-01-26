package lpa.aumont.cercadom.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

/**
 * Quiz Entity, a quiz is a series of questions
 */
public class Quiz {
    private static final String DEFAULT_URL = "http://upload.wikimedia.org/wikipedia/commons/thumb/f/fd/Udacity_Logo.svg/396px-Udacity_Logo.svg.png";
    // All Question
    private Vector<Question> questions = new Vector<>();
    private String name;
    private int numberOfQuestions;
    private final String LOG_TAG = Quiz.class.getSimpleName();

    public Quiz(String name, String questionsJsonString, int numberOfQuestions) {

        this.name = name;
        this.numberOfQuestions = numberOfQuestions;
        try {
            setQuestions(questionsJsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // init Questions Vector
    private void setQuestions(String questionsJsonString) throws JSONException {
        JSONArray questionsJSONArray = new JSONObject(questionsJsonString).getJSONObject("feed").getJSONArray("entry");
        for (int i = 0; i < numberOfQuestions; i++) {

            try {
                JSONObject questionObject = questionsJSONArray.getJSONObject(i);
                String question = questionObject.getJSONObject("title").getString("$t");

                String goodResponse = questionObject.getJSONObject("gsx$a").getString("$t");

                String[] badResponses = {
                        questionObject.getJSONObject("gsx$b").getString("$t"),
                        questionObject.getJSONObject("gsx$c").getString("$t"),
                        questionObject.getJSONObject("gsx$d").getString("$t")};


                String url = questionObject.getJSONObject("gsx$url").getString("$t");
                if(url == null) {
                    url = DEFAULT_URL;
                }
                questions.add(new Question(question, goodResponse, badResponses,url));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public Question getQuestion(int position) {
        return this.questions.get(position);
    }

    public int getQuizSize() {
        return questions.size();
    }

    public String getName() {
        return this.name;
    }

}
