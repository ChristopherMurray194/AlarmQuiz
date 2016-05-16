package helloandroid.brighton.uk.ac.alarmquiz;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AnalogClock;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextClock;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
public final static String EXTRA_MESSAGE = "helloandroid.brighton.uk.ac.alarmquiz.MESSAGE";
static final int ALARM_TIME_REQUEST = 1;    // The request code to obtain the alarm time
LinearLayout PopupLayout;
private static TextClock Digital;
private static AnalogClock Analog;
// Create a dynamic array list of
public ArrayList<String> alarmsArr = new ArrayList<String>();
// The ListView that lists the alarms
ListView list;
private int ClickedAlarmIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Digital = (TextClock) findViewById(R.id.digitalClock);
        Analog = (AnalogClock) findViewById(R.id.analogClock);

        SetupListView();
        OnAlarmListItemClick();

        AddAlarmBttnAction();
    }

    private void SetupListView()
    {
        list = (ListView) findViewById(R.id.alarmList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,           // Context for the activity
                R.layout.alarm, // Layout to use
                alarmsArr       // Views to be displayed
        );
        list.setAdapter(adapter);

        // Add it to the list of alarms
        AddNewAlarm("14:30");
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
                AddNewAlarm("11:30");
                // Configure the alarm's settings
                ConfigureAlarm(view);
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
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // Save the alarm position of the new alarm, to be used in onActivityResult
                ClickedAlarmIndex = position;
                ConfigureAlarm(view);
            }
        });

        PopupLayout = new LinearLayout(this);

        // When the item is clicked for a long time (hold)
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) list.getAdapter();
                // Remove the item from the list
                adapter.remove(adapter.getItem(position));

                return true;
            }
        });
    }

    /** Called when the user clicks an item in the ListView */
    public void ConfigureAlarm(View view)
    {
        Intent intent = new Intent(this, ConfigAlarmActivity.class);
        // Pass the current time of the alarm view to the intent
        intent.putExtra("CURRENT_TIME", alarmsArr.get(ClickedAlarmIndex));
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
        if(requestCode == ALARM_TIME_REQUEST)
        {
            if(resultCode == RESULT_OK)
            {
                String timeStr = data.getStringExtra("ALARM_TIME");
                // Change the string for the alarm to the chosen time
                alarmsArr.set(ClickedAlarmIndex, timeStr);
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) list.getAdapter();
                // Update the adapter
                adapter.notifyDataSetChanged();
            }
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
