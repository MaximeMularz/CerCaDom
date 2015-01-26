package lpa.aumont.cercadom.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import lpa.aumont.cercadom.model.QuizContract.QuizEntry;

/**
 * Created by max on 12/12/2014.
 */
public class QuizDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cercadom.db";

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_QUIZ_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuizEntry.TABLE_NAME);
        onCreate(db);
    }

    private final String SQL_CREATE_QUIZ_TABLE = "CREATE TABLE " + QuizEntry.TABLE_NAME + " (" +
            QuizEntry._ID + " INTEGER PRIMARY KEY," +
            QuizEntry.COLUMN_QUIZ_NAME + " TEXT UNIQUE NOT NULL, " +
            QuizEntry.COLUMN_KEY_SPREADSHEET + " TEXT NOT NULL, " +
            QuizEntry.COLUMN_QUIZ_JSON + " TEXT NOT NULL);";

}
