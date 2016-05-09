package helloandroid.brighton.uk.ac.alarmquiz;

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
            finish(); // Finish activity
          }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tp = (TimePicker)findViewById(R.id.timePicker); // Get the time picker resource
    }

    private void GetAlarmTime()
    {
        // Get the hour and time from the time picker and assign to a string
        String timeHourStr = String.format((getString(R.string.AlarmHour)), String.valueOf(tp.getHour()));
        String timeMinuteStr = String.format((getString(R.string.AlarmMinute)), String.valueOf(tp.getMinute()));
    }

    private void RegisterAlarmBroadcast()
    {
        Intent intent = new Intent(this, PersistentAlarm.class);
        pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        alarmManager = (AlarmManager)(this.getSystemService(Context.ALARM_SERVICE));
    }

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
        // Alarm is set, exit activity
        finish();
    }

    private void UnregisterAlarmBroadcast()
    {
        alarmManager.cancel(pendingIntent);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }


}
