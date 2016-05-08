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
// Callback function for alarm manager event
BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        RegisterAlarmBroadcast();

        // Set up OK button functionality
        Button OKBttn = (Button) findViewById(R.id.confirm_button);
        OKBttn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onClickSetAlarm(view);
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
        // This is the call back function(BroacastReceiver) which will be called when the alarm time is reached
        mReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                Toast.makeText(context, "Alarm Time Reached", Toast.LENGTH_LONG).show();
            }
        };

        // Register the alarm broadcast here
        registerReceiver(mReceiver, new IntentFilter("helloandroid.brighton.uk.ac.alarmquiz"));
        pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("helloandroid.brighton.uk.ac.alarmquiz"), 0);
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
        //finish();
    }

    private void UnregisterAlarmBroadcast()
    {
        alarmManager.cancel(pendingIntent);
        getBaseContext().unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy()
    {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }


}
