package helloandroid.brighton.uk.ac.alarmquiz;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DestroyAlarm extends AppCompatActivity {
    MediaPlayer player;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destroy_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        textView.setText(R.string.square_root_eighty_one);

        // Get the EditText view to obtain the user's answer
        final EditText userInput = (EditText) findViewById(R.id.answerField);

        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_SEND)
                {
                    // If the user's input equals the correct answer
                    if (userInput.getText().toString().equals("nine") ||
                            userInput.getText().toString().equals(Integer.toString(9)))
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
