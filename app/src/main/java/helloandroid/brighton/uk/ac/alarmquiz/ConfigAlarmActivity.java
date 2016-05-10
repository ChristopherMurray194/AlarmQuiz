package helloandroid.brighton.uk.ac.alarmquiz;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
// Used to register alarm manager
public PendingIntent pendingIntent;
// Used to store running alarm manager
AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_config_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        // Set Cancel button functionality
        Button CancelBttn = (Button) findViewById(R.id.exit_button);
        CancelBttn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                Bundle extras = getIntent().getExtras();
                if(extras != null)
                {
                    // Pass the AlarmTime string back to the previous activity
                    intent.putExtra("ALARM_TIME", extras.getString("CURRENT_TIME"));
                }
                setResult(Activity.RESULT_OK, intent);
                finish(); // Finish activity
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tp = (TimePicker) findViewById(R.id.timePicker); // Get the time picker resource
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
        Intent intent = new Intent(this, PersistentAlarm.class);
        pendingIntent = PendingIntent.getService(this, 0, intent, 0);
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
        calendar.set(Calendar.HOUR_OF_DAY, tp.getHour());   // Set the hour to the selected hour
        calendar.set(Calendar.MINUTE, tp.getMinute());      // Set the minute to the selected minute
        calendar.set(Calendar.SECOND, 0);                   // Set the seconds to 0
        long alarmTime = calendar.getTimeInMillis();        // Get the time to set the alarm
        Toast.makeText(this, calendar.getTime().toString(), Toast.LENGTH_LONG).show();
        // Get the current time and set alarm after 10 seconds from current time
        // so here we get
       alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        Intent intent = new Intent();
        // Pass the AlarmTime string back to the previous activity
        intent.putExtra("ALARM_TIME", GetAlarmTime());
        setResult(Activity.RESULT_OK, intent);
        // Alarm is set, exit activity
        finish();
    }

    private void UnregisterAlarmBroadcast()
    {
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        // Save the time for this specific alarm
        savedInstanceState.putInt(ALARM_HOUR, tp.getHour());
        savedInstanceState.putInt(ALARM_MINUTE, tp.getMinute());

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        // Retrieve the saved alarm time
        tp.setHour(savedInstanceState.getInt(ALARM_HOUR));
        tp.setMinute(savedInstanceState.getInt(ALARM_MINUTE));
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }


}
