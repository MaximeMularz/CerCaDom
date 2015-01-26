package lpa.aumont.cercadom;

import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;


public class ResultActivity extends ActionBarActivity {

    private static final String LOG_TAG = ResultActivity.class.getSimpleName();

    private static final String SCORE_KEY = "SCORE";
    private static final int MAX_SCORE = 25;
    private static final String QUIZ_NAME = "quiz_name";

    private static final int NO_STAR = 0;
    private static final int ONE_STAR = 1;
    private static final int TWO_STARS = 2;
    private static final int THREE_STARS = 3;
    private static final int FOUR_STARS = 4;
    private static final int FIVE_STARS = 5;

    private RatingBar mRatingBar;


    private int mScore;
    private String mQuizName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        if (savedInstanceState != null) {
            mScore = savedInstanceState.getInt(SCORE_KEY);
            mQuizName = savedInstanceState.getString(QUIZ_NAME);
        }

        Bundle args = getIntent().getExtras();

        if (args != null && args.containsKey(SCORE_KEY) && args.containsKey(QUIZ_NAME)) {
            mScore = args.getInt(SCORE_KEY);
            mQuizName = args.getString(QUIZ_NAME);
        }

        TextView mScoreTextView = (TextView) findViewById(R.id.result);
        mScoreTextView.setText(Utility.displayScore(this.getApplicationContext(), mScore, MAX_SCORE));
        upDateRating();

    }

    private void upDateRating() {
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        if (mScore == 25) {
            mRatingBar.setRating(FIVE_STARS);
            return;
        }

        if(mScore == 0) {
            mRatingBar.setRating(NO_STAR);
            return;
        }

        if (mScore<=5) {
            mRatingBar.setRating(ONE_STAR);
            return;
        }

        if (mScore<=10) {
            mRatingBar.setRating(TWO_STARS);
            return;
        }

        if (mScore<=15) {
            mRatingBar.setRating(THREE_STARS);
            return;
        }

        if (mScore<25) {
            mRatingBar.setRating(FOUR_STARS);
            return;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        mShareActionProvider.setShareIntent(createShareScore());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Intent createShareScore() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Can you do better than me " + mScore + " " + " in " + mQuizName + " quiz category #CerCaDom");
        return shareIntent;
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(SCORE_KEY, mScore);
        savedInstanceState.putString(QUIZ_NAME, mQuizName);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        // Return to main activity
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}

