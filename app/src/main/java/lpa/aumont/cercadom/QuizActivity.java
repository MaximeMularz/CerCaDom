package lpa.aumont.cercadom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.content.CursorLoader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import lpa.aumont.cercadom.model.Quiz;
import lpa.aumont.cercadom.model.QuizContract;
import lpa.aumont.cercadom.service.QuizTask;


public class QuizActivity extends ActionBarActivity implements FragmentQuizInteractionListener,
        LoaderManager.LoaderCallbacks<Cursor> {


    private static final int CONTENT_PROVIDER_ID = 1;

    private final String LOG_TAG = QuizActivity.class.getSimpleName();

    // Fields Dynamics
    private Quiz mQuiz;
    private QuizTask mQuizTask;
    private int mNumberQuestion = 0;
    private int mScore = 0;
    private String mQuizName;
    private int mLastPointValue;
    private String mLastGoodAnswer;

    // Member Views
    private ImageView mImageViewQuestion;
    private TextView mTextViewQuestion;
    private TextView mTexViewScore;
    private TextView mTextViewProgression;
    private AlertDialog mAlertDialog;
    private TextView mAlertDialogTextPoint;
    private RelativeLayout mSpinnerRelativeLayout;

    // Number of questions
    private static final int QUESTION_SIZE = 5;

    // Key to saveState
    private static final String LAST_POINT_KEY = "LAST_POINT_KEY";
    private static final String DIALOGBOX_STATUS_KEY = "DIALOGBOX_STATUS_KEY";
    private static final String SCORE_KEY = "SCORE";
    private static final String PROGRESSION_KEY = "PROGRESSION";
    private static final String QUIZ_NAME_KEY = "quiz_name";
    private static final String RESPONSES_KEY = "RESPONSES_KEY";
    private static final String POSITION_QUESTION_KEY = "POSITION_QUESTION_KEY";
    private static final String LAST_GOOD_RESPONSE_KEY = "LAST_GOOD_RESPONSE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        mSpinnerRelativeLayout = (RelativeLayout) findViewById(R.id.loadingPanel);
        mSpinnerRelativeLayout.setVisibility(View.VISIBLE);
        initDialogBox();

        SelectAnswerTypeFragment mSelectAnswerTypeFragment = new SelectAnswerTypeFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mSelectAnswerTypeFragment)
                    .commit();
            mNumberQuestion = 0;
            mScore = 0;
        } else {
            mScore = savedInstanceState.getInt(SCORE_KEY);
            mNumberQuestion = savedInstanceState.getInt(PROGRESSION_KEY);
            mLastPointValue = savedInstanceState.getInt(LAST_POINT_KEY);
            mLastGoodAnswer = savedInstanceState.getString(LAST_GOOD_RESPONSE_KEY);

            if (savedInstanceState.getBoolean(DIALOGBOX_STATUS_KEY)) {
                upDateDialogBox();
            }
        }

        Bundle args = getIntent().getExtras();

        if (args != null && args.containsKey(QUIZ_NAME_KEY)) {
            mQuizName = args.getString(QUIZ_NAME_KEY);
        }

        // Init Views
        mTextViewQuestion = (TextView) findViewById(R.id.textViewQuestion);
        mTexViewScore = (TextView) findViewById(R.id.score);
        mTextViewProgression = (TextView) findViewById(R.id.progression);
        mImageViewQuestion = (ImageView) findViewById(R.id.questionImage);
        mImageViewQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.property_animator);

                mImageViewQuestion.startAnimation(animation);
            }
        });
        //Set Parameters
        updateScore();
        updateProgression();

        getSupportLoaderManager().initLoader(CONTENT_PROVIDER_ID, null, this);

    }

    private void upDateDialogBox() {
        // Test last Value
        if (mLastPointValue > 0) {
            // Display Score
            mAlertDialogTextPoint.setText(Utility.dialogBoxGoodAnswer(this, mLastPointValue));
        } else {
            // Display Answer
            mAlertDialogTextPoint.setText(Utility.dialogBoxBadAnswer(this, mLastGoodAnswer));
        }
        mAlertDialog.show();
    }

    private void initDialogBox() {

        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.fragment_display_score, null);
        mAlertDialogTextPoint = (TextView) alertDialogView.findViewById(R.id.result);
        //Création de l'AlertDialog
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
        adb.setView(alertDialogView);

        //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
        adb.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                loadNextQuestion();
            }
        });

        adb.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                loadNextQuestion();
            }
        });
        mAlertDialog = adb.create();
    }

    private void updateProgression() {
        mTextViewProgression.setText(Utility.formatProgression(this, mNumberQuestion + 1, QUESTION_SIZE));
    }

    private void updateScore() {
        mTexViewScore.setText(Utility.formatScore(this, mScore));
    }

    // Hide Spinner and display question + Image
    private void loadQuestion() {

        mSpinnerRelativeLayout.setVisibility(View.VISIBLE);

        mTextViewQuestion.setText(mQuiz.getQuestion(mNumberQuestion).getQuestion());

        Picasso.with(this).load(mQuiz.getQuestion(mNumberQuestion).getUrlImage()).into(mImageViewQuestion, new Callback() {
            @Override
            public void onSuccess() {
                mSpinnerRelativeLayout.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                mImageViewQuestion.setImageResource(R.drawable.ic_launcher);
            }
        });


    }

    // To save State
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt(PROGRESSION_KEY, mNumberQuestion);
        savedInstanceState.putInt(SCORE_KEY, mScore);
        savedInstanceState.putString(QUIZ_NAME_KEY, mQuizName);
        savedInstanceState.putBoolean(DIALOGBOX_STATUS_KEY, mAlertDialog.isShowing());
        savedInstanceState.putInt(LAST_POINT_KEY, mLastPointValue);
        savedInstanceState.putString(LAST_GOOD_RESPONSE_KEY, mQuiz.getQuestion(mNumberQuestion).getGoodResponse());

        super.onSaveInstanceState(savedInstanceState);
    }


    //Implemantation of Duo, Square and Got it Fragment.
    @Override
    public void responseFromFragment(String response, int valueResponse) {

        mLastGoodAnswer = mQuiz.getQuestion(mNumberQuestion).getGoodResponse();
        if (mLastGoodAnswer.trim().toLowerCase().equals(response.trim().toLowerCase())) {
            mScore += valueResponse;
            mLastPointValue = valueResponse;

        } else {
            mLastPointValue = 0;
        }
        upDateDialogBox();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Dismiss AlertDialogBox, to prevent memories lake
        mAlertDialog.dismiss();

    }

    private void loadNextQuestion() {
        // End of Quiz
        if (mNumberQuestion == mQuiz.getQuizSize() - 1) {
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            intent.putExtra(SCORE_KEY, mScore).putExtra(QUIZ_NAME_KEY, mQuizName);
            startActivity(intent);
            return;
        }

        mNumberQuestion++;
        loadQuestion();
        updateScore();
        updateProgression();

        SelectAnswerTypeFragment selectAnswerTypeFragment;
        selectAnswerTypeFragment = (SelectAnswerTypeFragment) getSupportFragmentManager().findFragmentByTag(SelectAnswerTypeFragment.TAG);

        if (null == selectAnswerTypeFragment) {
            selectAnswerTypeFragment = new SelectAnswerTypeFragment();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, selectAnswerTypeFragment, SelectAnswerTypeFragment.TAG);
        ft.addToBackStack(null);
        ft.commit();
    }

    // init Quiz after Request
    public void setQuiz(Quiz quiz) {
        mQuiz = quiz;
        // Display first question
        loadQuestion();
        // Hidden Image loading
    }


    public void duo(View view) {
        // Init Value for responses
        String[] responses = {mQuiz.getQuestion(mNumberQuestion).getGoodResponse(),
                mQuiz.getQuestion(mNumberQuestion).getBadResponses()[1]};

        // Shuffle Responses
        Utility.shuffleArray(responses);

        //Retrieve Fragment by TAG
        DuoFragment duoFragment;
        duoFragment = (DuoFragment) getSupportFragmentManager().findFragmentByTag(DuoFragment.TAG);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // Set args
        Bundle args = new Bundle();
        args.putStringArray(RESPONSES_KEY, responses);

        // Check if Fragment exist
        if (null == duoFragment) {
            duoFragment = new DuoFragment();
            duoFragment.setArguments(args);
            ft.addToBackStack(null);
        } else {
            duoFragment.updateFragmentTexView(args);
        }
        //Replace and show Fragment
        ft.replace(R.id.container, duoFragment, DuoFragment.TAG);
        ft.show(duoFragment).commit();
    }

    public void square(View view) {

        String[] responses = new String[4];
        responses[0] = mQuiz.getQuestion(mNumberQuestion).getGoodResponse();
        responses[1] = mQuiz.getQuestion(mNumberQuestion).getBadResponses()[0];
        responses[2] = mQuiz.getQuestion(mNumberQuestion).getBadResponses()[1];
        responses[3] = mQuiz.getQuestion(mNumberQuestion).getBadResponses()[2];

        Utility.shuffleArray(responses);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        SquareFragment squareFragment;
        squareFragment = (SquareFragment) getSupportFragmentManager().findFragmentByTag(SquareFragment.TAG);

        Bundle args = new Bundle();
        args.putStringArray(RESPONSES_KEY, responses);

        if (null == squareFragment) {
            squareFragment = new SquareFragment();
            squareFragment.setArguments(args);
            ft.addToBackStack(null);
        } else {
            squareFragment.updateFragmentTexView(args);
        }

        ft.replace(R.id.container, squareFragment, SquareFragment.TAG);
        ft.show(squareFragment).commit();
    }

    public void gotIt(View view) {
        GotItFragment gotItFragment;
        gotItFragment = (GotItFragment) getSupportFragmentManager().findFragmentByTag(GotItFragment.TAG);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (null == gotItFragment) {
            gotItFragment = new GotItFragment();
            ft.addToBackStack(null);
        }
        ft.replace(R.id.container, gotItFragment, GotItFragment.TAG);
        ft.show(gotItFragment).commit();
    }

    @Override
    public void onBackPressed() {

        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.fragment_display_score, null);

        TextView et = (TextView) alertDialogView.findViewById(R.id.result);
        et.setText(this.getApplicationContext().getString(R.string.quitQuiz));

        //Création de l'AlertDialog
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
        adb.setView(alertDialogView);

        //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
        adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });


        adb.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        adb.show();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, QuizContract.QuizEntry.buildQuizName(mQuizName), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_QUIZ_NAME);
            int jsonIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_QUIZ_JSON);
            String name = cursor.getString(nameIndex);
            String json = cursor.getString(jsonIndex);
            setQuiz(new Quiz(name, json, QUESTION_SIZE));
        } else {
            if (Utility.isOnline(this)) {
                // Do Task to add Quiz to Datastore
                mQuizTask = new QuizTask(this);
                mQuizTask.execute(mQuizName);
            } else {
                displayNoConnection();
            }

        }
    }

    private void displayNoConnection() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.fragment_display_score, null);

        TextView et = (TextView) alertDialogView.findViewById(R.id.result);
        et.setText("No Connection, Back to Main Page");

        //Création de l'AlertDialog
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        //On affecte la vue personnalisé que l'on a crée à notre AlertDialog
        adb.setView(alertDialogView);

        //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
        adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });


        adb.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });

        adb.show();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
