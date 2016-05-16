package helloandroid.brighton.uk.ac.alarmquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.PopupWindow;

import java.util.ArrayList;

/**
 * Created by ChristopherMurray on 16/05/2016.
 */
public class QuestionsDBHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "QuestionsDB.db";
    public static final String QUESTIONS_TABLE_NAME = "questions";
    public static final String QUESTION_COLUMN_QUESTION = "question";
    public static final String QUESTION_COLUMN_ANSWER = "answer";

    public QuestionsDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null ,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Drop the table if it already exists
        db.execSQL("DROP TABLE" + QUESTIONS_TABLE_NAME);

        // Create the database from scratch
        db.execSQL("create table questions" +
                        // Create a table of questions consisting of an id number, question, and the corresponding answer.
                        "(id integer primary key, question text, answer text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // If the questions table already exists, remove it
        db.execSQL("DROP TABLE IF EXISTS questions");
        // Create a new table
        onCreate(db);
    }

    public boolean insertQuestion(String question, String answer)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        // Add the passed question to the database
        contentValues.put(QUESTION_COLUMN_QUESTION, question);
        // Add the passed answer to the database
        contentValues.put(QUESTION_COLUMN_ANSWER, answer);
        // Add the above content values to the questions database table
        db.insert(QUESTIONS_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public Cursor getData(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        // Select the element from the database table with the given id.
        Cursor res = db.rawQuery("select * from questions where id="+id+"", null);
        res.moveToFirst();
        return res;
    }

    public int numberOfRows()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        // Get the number of rows based on the number of entries in the questions table
        int numRows = (int) DatabaseUtils.queryNumEntries(db, QUESTIONS_TABLE_NAME);
        return numRows;
    }

    public boolean updateQuestion(Integer id, String question, String answer)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(QUESTION_COLUMN_QUESTION, question);
        contentValues.put(QUESTION_COLUMN_ANSWER, answer);
        // Update the questions database entry of the given id number, with the given content values
        db.update(QUESTIONS_TABLE_NAME, contentValues, "id = ? ", new String[] {Integer.toString(id)} );
        return true;
    }

    public Integer deleteQuestion(Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete the question of the given id
        return db.delete(QUESTIONS_TABLE_NAME, "id = ? ", new String[] {Integer.toString(id) });
    }

    public void clearDatabase()
    {
        String clearDB = "DELETE FROM " + QUESTIONS_TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(clearDB);
    }

    public ArrayList<String> getAllQuestions()
    {
        ArrayList<String> arrList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from questions", null);
        res.moveToFirst();

        while(res.isAfterLast() == false)
        {
            arrList.add(res.getString(res.getColumnIndex(QUESTION_COLUMN_QUESTION)));
            res.moveToNext();
        }
        return arrList;
    }

    public ArrayList<String> getAllAnswers()
    {
        ArrayList<String> arrList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from questions", null);
        res.moveToFirst();

        while(res.isAfterLast() == false)
        {
            arrList.add(res.getString(res.getColumnIndex(QUESTION_COLUMN_ANSWER)));
            res.moveToNext();
        }
        return arrList;
    }

}
