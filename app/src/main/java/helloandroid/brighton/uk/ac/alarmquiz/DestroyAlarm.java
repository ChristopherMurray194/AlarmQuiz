package helloandroid.brighton.uk.ac.alarmquiz;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class DestroyAlarm extends AppCompatActivity {
    MediaPlayer player;
    Vibrator vibrator;
    QuestionsDB Database;
    int SelectedQuestionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destroy_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create an instance of the questions database
        Database = new QuestionsDB(this);
        Database.getDatabase().clearDatabase();
        Database.PopulateDB();

        // Play the default ringtone
        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        // Loop playback
        player.setLooping(true);
        player.start();

        // Vibrate
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for the duration of the ringtone playback
        vibrator.vibrate(player.getDuration());

        // Get the TextView to display the question
        TextView textView = (TextView) findViewById(R.id.question_view);
        textView.setTextSize(40);

        int min = 0;    // Minimum bound of database
        int max = Database.getDatabase().getAllQuestions().size()-1; // Max bound of database
        // Generate a random integer within the bounds of the database size
        SelectedQuestionID = ThreadLocalRandom.current().nextInt(min, max + 1);
        ArrayList<String> arr = Database.getDatabase().getAllQuestions();
        // Get the randomly selected question
        String question = Database.getDatabase().getAllQuestions().get(SelectedQuestionID);
        // Display the question
        textView.setText(question);

        // Handle the input from the user (their answer)
        HandleUserInput();
    }

    private void HandleUserInput()
    {
        // Get the EditText view to obtain the user's answer
        final EditText userInput = (EditText) findViewById(R.id.answerField);
        // Get the answer corresponding to the selected question
        final String answer = Database.getDatabase().getAllAnswers().get(SelectedQuestionID);

        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                boolean handled = false;
                /* When the user presses the send/submit button on the keyboard
                   submitting their answer... */
                if(actionId == EditorInfo.IME_ACTION_SEND)
                {
                    // If the user's input equals the correct answer
                    if (userInput.getText().toString().equalsIgnoreCase(answer))
                    {
                        // Turn off the alarm
                        EndAlarm();
                    }
                    else
                    {
                        // Display incorrect answer message
                        Toast.makeText(DestroyAlarm.this, R.string.wrong_answer, Toast.LENGTH_LONG).show();
                    }
                    handled = true;
                }
                return handled;
            }
        });
    }

    private void EndAlarm()
    {
        // Stop the alarm playback
        player.setLooping(false);
        player.stop();

        // Stop vibrating
        vibrator.cancel();

        // Finish the activity
        finish();
    }

}
