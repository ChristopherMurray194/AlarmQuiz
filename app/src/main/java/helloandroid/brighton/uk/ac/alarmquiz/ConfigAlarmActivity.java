package helloandroid.brighton.uk.ac.alarmquiz;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class ConfigAlarmActivity extends AppCompatActivity {
TimePicker tp;
// The hour and minute key constants used for saving the time picker's hour and time
static final String ALARM_HOUR = "Hour";
static final String ALARM_MINUTE = "Minute";
private static int INTENT_ID = 0;
// Used to register alarm manager
public PendingIntent pendingIntent;
// Used to store running alarm manager
AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null)
        {
            // Retrieve the saved alarm time
            tp.setHour(savedInstanceState.getInt(ALARM_HOUR));
            tp.setMinute(savedInstanceState.getInt(ALARM_MINUTE));
        }
        else
        {
            setContentView(R.layout.activity_config_alarm);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            // Unique intent ID
            ++INTENT_ID;

            // Create the alarm pending intent
            RegisterAlarmBroadcast();

            // Set OK button functionality
            Button OKBttn = (Button) findViewById(R.id.confirm_button);
            OKBttn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    onClickSetAlarm(view);
                }
            });

            final Intent intent = getIntent();
            // Get the string currently displayed by the item selected in the ListView from the MainActivity
            final String CurrentTime = intent.getExtras().getString(getString(R.string.CURRENT_TIME));
            // Set Cancel button functionality
            Button CancelBttn = (Button) findViewById(R.id.exit_button);
            CancelBttn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (CurrentTime != null)
                    {
                        // Pass the AlarmTime string back to the previous activity
                        intent.putExtra(getString(R.string.ALARM_TIME), CurrentTime);
                    }
                    setResult(Activity.RESULT_OK, intent);

                    onBackPressed();
                }
            });

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            tp = (TimePicker) findViewById(R.id.timePicker); // Get the time picker resource
            //                           01234
            // Time string formatted as: HH:MM
            int HourStr = Integer.valueOf(CurrentTime.substring(0, CurrentTime.indexOf(':')));
            int MinuteStr = Integer.valueOf(CurrentTime.substring(3));
            tp.setHour(HourStr);
            tp.setMinute(MinuteStr);
        }
    }

    /**
     * Get the chosen time from the user, convert it to a string and return
     * @return - Chosen time by the user converted to a string
     */
    private String GetAlarmTime()
    {
        String Hour = String.valueOf(tp.getHour()), Minute = String.valueOf(tp.getMinute());
        // If hour or minute is less than a single digit, prepend with a 0
        if(tp.getHour() < 10) { Hour = "0" + Hour; }
        if (tp.getMinute() < 10) { Minute = "0" + Minute; }

        // Get the hour and minute from the time picker and assign to a string
        String AlarmTime = String.format(("%s:%s"), Hour, Minute);
        return AlarmTime;
    }

    private void RegisterAlarmBroadcast()
    {
        Intent intent = new Intent(this, DestroyAlarm.class);
        pendingIntent = PendingIntent.getActivity(this, INTENT_ID, intent, 0);
        alarmManager = (AlarmManager)(this.getSystemService(Context.ALARM_SERVICE));
    }

    /**
     * When the OK button is clicked, set the alarm and exit the activity
     * @param view
     */
    public void onClickSetAlarm(View view)
    {
        Date date = new Date(); // Initializes to current time/date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // If the selected hour is earlier than the current hour OR
        // If the selected hour is the current hour AND the selected minute is earlier than or equal
        // to the current minute
        if( (tp.getHour() < calendar.get(Calendar.HOUR_OF_DAY)) ||
                ((tp.getHour() == calendar.get(Calendar.HOUR_OF_DAY)) &&
                        (tp.getMinute() <= calendar.get(Calendar.MINUTE))) )
        {
            // Set the alarm for the next day instead of the current day
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }

        calendar.set(Calendar.HOUR_OF_DAY, tp.getHour());   // Set the hour to the selected hour
        calendar.set(Calendar.MINUTE, tp.getMinute());      // Set the minute to the selected minute
        calendar.set(Calendar.SECOND, 0);                   // Set the seconds to 0
        long alarmTime = calendar.getTimeInMillis();        // Get the time to set the alarm
        Toast.makeText(this, calendar.getTime().toString(), Toast.LENGTH_LONG).show();
        // Get the current time and set alarm
       alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);

        Intent intent = new Intent();
        // Pass the AlarmTime string back to the previous activity
        intent.putExtra(getString(R.string.ALARM_TIME), GetAlarmTime());
        intent.putExtra(getString(R.string.PendingID), INTENT_ID);
        setResult(Activity.RESULT_OK, intent);

        onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        // Save the time for this specific alarm
        savedInstanceState.putInt(ALARM_HOUR, tp.getHour());
        savedInstanceState.putInt(ALARM_MINUTE, tp.getMinute());

        super.onSaveInstanceState(savedInstanceState);
    }
}
