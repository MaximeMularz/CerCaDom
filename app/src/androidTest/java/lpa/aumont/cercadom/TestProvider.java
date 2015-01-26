package lpa.aumont.cercadom;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;
import android.util.Log;

import lpa.aumont.cercadom.model.Quiz;
import lpa.aumont.cercadom.model.QuizContract;
import lpa.aumont.cercadom.model.QuizContract.QuizEntry;

/**
 * Created by max on 14/12/2014.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();
    public static final String QUIZ_NAME = "dog";


    public void testGetType() {
        String type = mContext.getContentResolver().getType(QuizEntry.buildQuizUri(1));

        assertEquals(QuizEntry.CONTENT_ITEM_TYPE, type);

    }

    public void testInsertReadDb() {

        ContentValues values = TestDb.createQuiz("dog");
        long locationRowId = ContentUris.parseId(mContext.getContentResolver().insert(QuizEntry.CONTENT_URI, values));
        // Verify we got a row back.
        Log.d(LOG_TAG, "New row id: " + locationRowId);

    }

    public void testQuery() {
        Cursor cursor = mContext.getContentResolver().query(QuizEntry.buildQuizName("dog"), null, null, null, null);

        assertTrue(cursor.getCount() > 0);


        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_QUIZ_NAME);
            int jsonIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_QUIZ_JSON);
            int key_spreadSheetIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_KEY_SPREADSHEET);
            String name = cursor.getString(nameIndex);
            String json = cursor.getString(jsonIndex);
            String spreadSheetIndex = cursor.getString(key_spreadSheetIndex);

            assertEquals(json, DataTest.json);
            assertEquals(name, "dog");
            assertEquals(spreadSheetIndex, DataTest.quiz_spreadSheet);

            Quiz quiz = new Quiz(name, json, 5);

            assertEquals(name, quiz.getName());
        }

    }

    public void testDelete() {
        int id = mContext.getContentResolver().delete(QuizEntry.buildQuizName("dog"), null, null);
        Log.d(LOG_TAG, "Delete row id: " + id);
        assertTrue(id == 0);

        Cursor cursor = mContext.getContentResolver().query(QuizEntry.buildQuizName("dog"), null, null, null, null);

        assertTrue(cursor.getCount() == 0);
    }

    public void testUpdate() {
        ContentValues values = TestDb.createUpdateQuiz("json","spreadsheetkey");

        int id = mContext.getContentResolver().update(QuizEntry.buildQuizName(DataTest.quiz_name), values, null, null);
        Log.d(LOG_TAG, "Updated row id: " + id);

        Cursor cursor = mContext.getContentResolver().query(QuizEntry.buildQuizName(DataTest.quiz_name), null, null, null, null);

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_QUIZ_NAME);
            int jsonIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_QUIZ_JSON);
            int key_spreadSheetIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_KEY_SPREADSHEET);
            String name = cursor.getString(nameIndex);
            String json = cursor.getString(jsonIndex);
            String spreadSheetIndex = cursor.getString(key_spreadSheetIndex);


            assertEquals(json, "json");
            assertEquals(name, DataTest.quiz_name);
            assertEquals(spreadSheetIndex, "spreadsheetkey");

        }


    }
}
