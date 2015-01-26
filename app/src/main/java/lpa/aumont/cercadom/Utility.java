package lpa.aumont.cercadom;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Random;

/**
 * Utility Class
 */
public class Utility {

    public static String formatScore(Context context, int score) {
        return context.getString(R.string.format_score, score);
    }

    public static String formatProgression(Context context, int questionNumber, int questionsSize) {
        return context.getString(R.string.format_progression, questionNumber, questionsSize);
    }

    public static String displayScore(Context context, int score, int maxScore) {
        return context.getString(R.string.format_score_final, score, maxScore);
    }

    public static String dialogBoxGoodAnswer(Context context, int score) {
        return context.getString(R.string.format_dialogbox_good_answer, score);
    }

    public static String dialogBoxBadAnswer(Context context, String response) {
        return context.getString(R.string.format_dialogbox_bad_answer, response);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    // Implementing shuffle
    static void shuffleArray(String[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

}
