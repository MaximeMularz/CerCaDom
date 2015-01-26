package lpa.aumont.cercadom.model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by max on 12/12/2014.
 */
public class QuizProvider extends ContentProvider {


    private static final String LOG_TAG = QuizProvider.class.getSimpleName();

    private static final int QUIZ_WITH_NAME = 100;
    private static final int QUIZ = 200;
    private QuizDbHelper mDbHelper;

    private static final UriMatcher sURIMatcher = buildUriMatcher();

    @Override
    public boolean onCreate() {
        mDbHelper = new QuizDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor = null;
        switch (sURIMatcher.match(uri)) {
            // "quiz"/*
            case QUIZ_WITH_NAME: {
                retCursor = mDbHelper.getReadableDatabase().query(
                        QuizContract.QuizEntry.TABLE_NAME,
                        projection,
                        QuizContract.QuizEntry.TABLE_NAME + "." + QuizContract.QuizEntry.COLUMN_QUIZ_NAME + " = ?",
                        new String[]{QuizContract.QuizEntry.getQuizNameFromUri(uri)},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sURIMatcher.match(uri);

        switch (match) {
            case QUIZ_WITH_NAME:
                return QuizContract.QuizEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sURIMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case QUIZ: {
                Log.d(LOG_TAG, "Switch values :" + QUIZ);
                long _id = db.insert(QuizContract.QuizEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = QuizContract.QuizEntry.buildQuizUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(QuizContract.QuizEntry.CONTENT_URI, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int id = 0;
        switch (sURIMatcher.match(uri)) {
            // "quiz"/*
            case QUIZ_WITH_NAME: {
                id = mDbHelper.getWritableDatabase().delete(QuizContract.QuizEntry.TABLE_NAME,
                        QuizContract.QuizEntry.TABLE_NAME + "." + QuizContract.QuizEntry.COLUMN_QUIZ_NAME + " = ?",
                        new String[]{QuizContract.QuizEntry.getQuizNameFromUri(uri)});
                break;
            }
        }
        getContext().getContentResolver().notifyChange(QuizContract.QuizEntry.CONTENT_URI, null);
        return id;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int id = 0;
        switch (sURIMatcher.match(uri)) {
            // "quiz"/*
            case QUIZ_WITH_NAME: {
                id = mDbHelper.getWritableDatabase().update(QuizContract.QuizEntry.TABLE_NAME,
                        values, QuizContract.QuizEntry.TABLE_NAME + "." + QuizContract.QuizEntry.COLUMN_QUIZ_NAME + " = ?",
                        new String[]{QuizContract.QuizEntry.getQuizNameFromUri(uri)});
                break;
            }
        }
        getContext().getContentResolver().notifyChange(QuizContract.QuizEntry.CONTENT_URI, null);
        return id;
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = QuizContract.CONTENT_AUTHORITY;
        uriMatcher.addURI(authority, QuizContract.PATH_QUIZ, QUIZ);
        uriMatcher.addURI(authority, QuizContract.PATH_QUIZ + "/*", QUIZ_WITH_NAME);
        return uriMatcher;
    }
}
