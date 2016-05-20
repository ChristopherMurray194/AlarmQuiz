package helloandroid.brighton.uk.ac.alarmquiz;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
public final static String EXTRA_MESSAGE = "helloandroid.brighton.uk.ac.alarmquiz.MESSAGE";
static final int ALARM_TIME_REQUEST = 1;    // The request code to obtain the alarm time
static final int PENDING_ID_REQUEST = 2;    // The request code to obtain the pending intent id
private static TextClock Digital;
private static AnalogClock Analog;
// Create a dynamic array list of
private ArrayList<String> alarmsArr = new ArrayList<String>();
// The ListView that lists the alarms
private ListView list;
private int ClickedAlarmIndex;
private final int MAX_ALARMS = 20; // Maximum number of alarms the user can set
private int pendingId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SetupAnalogClock();
        SetupDigitalClock();

        SetupListView();
        OnAlarmListItemClick();

        AddAlarmBttnAction();
    }

    /**
     * Setup the AnalogClock with any desired attributes
     */
    private void SetupAnalogClock()
    {
        Analog = (AnalogClock) findViewById(R.id.analogClock);
        Analog.setVisibility(View.GONE);    // Hide the Analog clock
    }

    /**
     * Setup the Digital/TextClock with any desired attributes
     */
    private void SetupDigitalClock()
    {
        // Get the text clock colour
        final int COLOUR = ContextCompat.getColor(this, R.color.DigitalClockColour);

        Digital = (TextClock) findViewById(R.id.digitalClock);
        Digital.setTextSize(60);        // Set the size of the Text clock text
        Digital.setTextColor(COLOUR);   // Set the text colour
        Digital.setBackgroundColor(ContextCompat.getColor(this, R.color.BLACK));
        Digital.setPadding(100, 50, 100, 50); // L,T,R,B // Center the clock
    }

    /**
     * Setup the ListView to display the user's set alarms in a list form
     */
    private void SetupListView()
    {
        list = (ListView) findViewById(R.id.alarmList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,           // Context for the activity
                R.layout.alarm, // Layout to use
                alarmsArr       // Views to be displayed
        );
        list.setAdapter(adapter);
    }

    /**
     * Handle the AddAlarm button declaration and OnClickEvent
     */
    private void AddAlarmBttnAction()
    {
        FloatingActionButton addAlarmBttn = (FloatingActionButton) findViewById(R.id.addAlarm);
        addAlarmBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayAdapter<String> adapter = (ArrayAdapter<String>) list.getAdapter();
                // A constraint that only allows the user to create 20 alarms maximum
                if (adapter.getCount() < MAX_ALARMS) {
                    // Get the current time
                    Date date = new Date(); // Initializes to the current time
                    Calendar calender = Calendar.getInstance();
                    calender.setTime(date);
                    String CurrentHour = String.valueOf(calender.get(Calendar.HOUR_OF_DAY));   // Get the current hour
                    String CurrentMinute = String.valueOf(calender.get(Calendar.MINUTE));      // Get the current minute

                    // If hour or minute is less than a single digit, prepend with a 0
                    if(calender.get(Calendar.HOUR_OF_DAY) < 10){ CurrentHour = "0" + CurrentHour; }
                    if(calender.get(Calendar.MINUTE) < 10){ CurrentMinute = "0" + CurrentMinute; }

                    // Set the alarm string to the current time
                    AddNewAlarm(String.format("%s:%s", CurrentHour, CurrentMinute));

                    // Configure the alarm's settings
                    ConfigureAlarm(view);
                } else    // The maximum alarm cap has been reached
                {
                    // Display alarm cap reached message to user
                    Toast.makeText(view.getContext(), "You cannot set more than 20 alarms.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Add a new string to the alarm list
     * @param str
     */
    public void AddNewAlarm(String str)
    {
        // Store the ListView adapter locally
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) list.getAdapter();
        // Add to list view
        adapter.add(str);
        // Save the alarm position of the new alarm, to be used in onActivityResult
        ClickedAlarmIndex = adapter.getPosition(str);
    }

    /**
     * When the alarm item in the ListView is clicked
     */
    private void OnAlarmListItemClick()
    {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Save the alarm position of the new alarm, to be used in onActivityResult
                ClickedAlarmIndex = position;
                ConfigureAlarm(view);
            }
        });

        // When the item is clicked for a long time (hold)
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                final int ItemPos = position; // Position of the item in the list

                // Build the dialog box
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                dialogBuilder.setMessage("Delete this alarm?"); // Dialog message to be displayed

                // Setup positive button behaviour
                dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // Cancel the alarm
                        Intent intent = new Intent(MainActivity.this, ConfigAlarmActivity.class);
                        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, pendingId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        pendingIntent.cancel();
                        alarmManager.cancel(pendingIntent);

                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) list.getAdapter();
                        // Remove the item from the list
                        adapter.remove(adapter.getItem(ItemPos));
                    }
                });

                // Setup negative button behaviour
                dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // Cancel the dialog and do not delete the alarm
                        dialog.cancel();
                    }
                });

                // Create the dialog box
                AlertDialog alert = dialogBuilder.create();
                // Show the dialog box
                alert.show();

                return true;
            }
        });
    }

    /** Called when the user clicks an item in the ListView or fab */
    public void ConfigureAlarm(View view)
    {
        Intent intent = new Intent(this, ConfigAlarmActivity.class);
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) list.getAdapter();
        // Pass the current time of the alarm view to the intent
        intent.putExtra(getString(R.string.CURRENT_TIME), adapter.getItem(ClickedAlarmIndex).toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent, ALARM_TIME_REQUEST);
    }

    /**
     * Overrides the onActivityResult function, called when a result is received from
     * a created activity.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // Check the request code
        if( (requestCode == ALARM_TIME_REQUEST) && (resultCode == RESULT_OK) )
        {
            String timeStr = data.getStringExtra(getString(R.string.ALARM_TIME));
            // Change the string for the alarm to the chosen time
            alarmsArr.set(ClickedAlarmIndex, timeStr);
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) list.getAdapter();
            // Update the adapter
            adapter.notifyDataSetChanged();
        }

        if( (requestCode == PENDING_ID_REQUEST) && (resultCode == RESULT_OK) )
        {
            pendingId = data.getExtras().getInt(getString(R.string.PendingID));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
