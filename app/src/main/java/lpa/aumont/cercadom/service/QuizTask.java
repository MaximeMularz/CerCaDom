package lpa.aumont.cercadom.service;

/**
 * Created by max on 11/12/2014.
 */

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import lpa.aumont.cercadom.QuizActivity;
import lpa.aumont.cercadom.model.Quiz;
import lpa.aumont.cercadom.model.QuizContract;


/**
 * Created by vincent on 27/10/2014.
 * android.os.AsyncTask<Params, Progress, Result>
 */
public class QuizTask extends AsyncTask<String, Void, Void> {
    private final String LOG_TAG = QuizTask.class.getSimpleName();
    private final QuizActivity mContext;
    private Quiz mQuiz;

    // KEY SPREADSHEET
    private final static String CAT_QUIZ_KEY = "1wxxGr_8C1C-1Tmmw2979akk-N3uwVG504j1_rHQ7AZU";
    private final static String DOG_QUIZ_KEY = "0AibCmA8qlS-PdFJ0bTV0SXc4ZHo5aGlqS2t6SUZMTXc";
    private final static String FISH_QUIZ_KEY = "0AibCmA8qlS-PdGxmSjlhdndPZ19KSGtJazdaQ0pmVWc";
    private final static String BIRD_QUIZ_KEY = "12mg6h7ubQGZtb1-8JJwzInZsunjd5GO4Iz9SovYxEsg";

    private final static String CAT_QUIZ_NAME = "cat";
    private final static String DOG_QUIZ_NAME = "dog";
    private final static String FISH_QUIZ_NAME = "fish";
    private final static String BIRD_QUIZ_NAME = "bird";

    private final static int QUESTIONS_SIZE = 5;

    public QuizTask(QuizActivity context) {
        this.mContext = context;
    }

    @Override
    protected Void doInBackground(String... params) {

        // If there's no zip code, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }
        String name = params[0];
        String key = getKeyFromName(params[0]);

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String questionsJson = null;

        String format = "json";

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            final String FORECAST_BASE_URL = "https://spreadsheets.google.com/feeds/list/" + key + "/od6/public/values?";
            final String FORMAT_PARAM = "alt";

            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(FORMAT_PARAM, format)
                    .build();

            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            questionsJson = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

            mQuiz = new Quiz(name, questionsJson, QUESTIONS_SIZE);

            // Add Quiz to Database
            ContentValues values = new ContentValues();
            values.put(QuizContract.QuizEntry.COLUMN_QUIZ_NAME, name);
            values.put(QuizContract.QuizEntry.COLUMN_QUIZ_JSON, questionsJson);
            values.put(QuizContract.QuizEntry.COLUMN_KEY_SPREADSHEET, key);

            long locationRowId = ContentUris.parseId(mContext.getContentResolver().insert(QuizContract.QuizEntry.CONTENT_URI, values));
            // Verify we got a row back.
            Log.d(LOG_TAG, "New row id: " + locationRowId);

        return null;
    }

    private String getKeyFromName(String name) {
        switch (name) {
            case CAT_QUIZ_NAME:
                return CAT_QUIZ_KEY;
            case DOG_QUIZ_NAME :
                return DOG_QUIZ_KEY;
            case BIRD_QUIZ_NAME :
                return BIRD_QUIZ_KEY;
            case FISH_QUIZ_NAME :
                return FISH_QUIZ_KEY;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        mContext.setQuiz(mQuiz);
    }
}
