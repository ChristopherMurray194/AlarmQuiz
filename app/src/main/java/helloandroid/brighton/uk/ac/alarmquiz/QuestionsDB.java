package helloandroid.brighton.uk.ac.alarmquiz;

import android.content.Context;

/**
 * Created by ChristopherMurray on 16/05/2016.
 */
public class QuestionsDB
{
    private static QuestionsDBHelper QuestionsDB;

    public QuestionsDB(Context context)
    {
        QuestionsDB = new QuestionsDBHelper(context);
    }

    /**
     * Populate the database with questions
     */
    public void PopulateDB()
    {
        QuestionsDBHelper dbHelper = QuestionsDB;
        dbHelper.insertQuestion("What is the square root of 81?", "nine");
        dbHelper.insertQuestion("What is the capital of England", "London");
        dbHelper.insertQuestion("What is 4 multiplied by 4?", "sixteen");
        dbHelper.insertQuestion("Star Wars the _____ Awakens.", "Force");
        dbHelper.close();
    }

    public QuestionsDBHelper getDatabase()
    {
        return QuestionsDB;
    }
}
