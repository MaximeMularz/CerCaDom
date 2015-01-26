package lpa.aumont.cercadom;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import lpa.aumont.cercadom.model.QuizContract;
import lpa.aumont.cercadom.model.QuizDbHelper;

/**
 * Created by max on 12/12/2014.
 */
public class TestDb extends AndroidTestCase {
    private static final String LOG_TAG = TestDb.class.getSimpleName();

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(QuizDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new QuizDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertAndReadQuiz() {

        QuizDbHelper quizDbHelper = new QuizDbHelper(mContext);
        SQLiteDatabase db = quizDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys.
        ContentValues values = createQuiz();

        long quizRowId;
        quizRowId = db.insert(QuizContract.QuizEntry.TABLE_NAME, null, values);

        // Verify we got a row back
        assertTrue(quizRowId != -1);
        Log.d(LOG_TAG, "New row id : " + quizRowId);


        // Specify COLUMNS you want
        String[] columns = {QuizContract.QuizEntry.COLUMN_QUIZ_NAME, QuizContract.QuizEntry.COLUMN_KEY_SPREADSHEET,
                QuizContract.QuizEntry.COLUMN_QUIZ_JSON};


        String[] args = {"cat"};
        // Cursor is the Query Result
        Cursor cursor = db.query(QuizContract.QuizEntry.TABLE_NAME,
                columns,
                "quiz_name = ?",// columns for the "where" clause
                args,// values for the "where" clause
                null,// columns to group by
                null,// columns to filter by row groups
                null // sort order
        );


        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_QUIZ_NAME);
            int jsonIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_QUIZ_JSON);
            int key_spreadSheetIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_KEY_SPREADSHEET);
            String name = cursor.getString(nameIndex);
            String json = cursor.getString(jsonIndex);
            String spreadSheetIndex = cursor.getString(key_spreadSheetIndex);

            assertEquals(json, DataTest.json);
            assertEquals(name, DataTest.quiz_name);
            assertEquals(spreadSheetIndex, DataTest.quiz_spreadSheet);

        }


    }

    public void testDelete() {
        QuizDbHelper quizDbHelper = new QuizDbHelper(mContext);
        SQLiteDatabase db = quizDbHelper.getWritableDatabase();
        int deleteId = db.delete(QuizContract.QuizEntry.TABLE_NAME, "quiz_name = ?", new String[]{"cat"});

        //assertTrue(deleteId>0);


    }

    public static ContentValues createQuiz() {
        return createQuiz(DataTest.quiz_name);
    }

    public static ContentValues createQuiz(String quizName) {
        ContentValues values = new ContentValues();
        values.put(QuizContract.QuizEntry.COLUMN_QUIZ_NAME, quizName);
        values.put(QuizContract.QuizEntry.COLUMN_QUIZ_JSON, DataTest.json);
        values.put(QuizContract.QuizEntry.COLUMN_KEY_SPREADSHEET, DataTest.quiz_spreadSheet);
        return values;
    }

    public static ContentValues createUpdateQuiz(String json,String spreadSheetKey) {
        ContentValues values = new ContentValues();
        values.put(QuizContract.QuizEntry.COLUMN_QUIZ_JSON, json);
        values.put(QuizContract.QuizEntry.COLUMN_KEY_SPREADSHEET, spreadSheetKey);
        return values;
    }
}
