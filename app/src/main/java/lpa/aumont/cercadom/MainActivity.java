package lpa.aumont.cercadom;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.UserDictionary;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.sample.castcompanionlibrary.cast.VideoCastManager;
import com.google.sample.castcompanionlibrary.cast.exceptions.CastException;

import java.util.Dictionary;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String CAT_QUIZ_VALUE = "cat";
    private static final String DOG_QUIZ_VALUE = "dog";
    private static final String FISH_QUIZ_VALUE = "fish";
    private static final String BIRD_QUIZ_VALUE = "bird";

    private static final String QUIZ_NAME = "quiz_name";


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //UserDictionary.Words.addWord(this,"Paracheirodon innesi",1,null,null);
        //getContentResolver().delete(UserDictionary.Words.CONTENT_URI,UserDictionary.Words.WORD + " = ?",new String[] {"Paracheirodon innesi"});

        setContentView(R.layout.activity_main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_main, new FragmentMainActivity())
                    .commit();
        }
    }

    // Check Button Clicked
    public void goQuiz(View v) {
        Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
        String quiz_name_value = null;

        switch (v.getId()) {
            case R.id.catQuiz:

                quiz_name_value = CAT_QUIZ_VALUE;
                break;

            case R.id.dogQuiz:
                quiz_name_value = DOG_QUIZ_VALUE;
                break;

            case R.id.birdQuiz:
                quiz_name_value = BIRD_QUIZ_VALUE;
                break;

            case R.id.fishQuiz:
                quiz_name_value = FISH_QUIZ_VALUE;
                break;
        }
        intent.putExtra(QUIZ_NAME, quiz_name_value);
        startActivity(intent);
    }

    /**
     * FragmentMainActivity fragment containing the MainView.
     */
    public static class FragmentMainActivity extends Fragment {

        public FragmentMainActivity() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_main_activity, container, false);
        }
    }
}
