package lpa.aumont.cercadom.model;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by max on 12/12/2014.
 */
public class QuizContract {
    public static final String CONTENT_AUTHORITY = "lpa.aumont.cercadom";


    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_QUIZ = "quiz";

    /* Inner class that defines the table contents of quizes table */
    public static final class QuizEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_QUIZ).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_QUIZ;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_QUIZ;

        public static final String TABLE_NAME = "quiz";

        public static final String COLUMN_QUIZ_NAME = "quiz_name";

        public static final String COLUMN_KEY_SPREADSHEET = "quiz_key_spreadsheet";

        public static final String COLUMN_QUIZ_JSON = "json";

        public static Uri buildQuizUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getQuizFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_QUIZ_NAME);
        }

        public static Uri buildQuizName
                (String quizName) {
            return CONTENT_URI.buildUpon().appendPath(quizName).build();
        }

        public static String getQuizNameFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }

}
