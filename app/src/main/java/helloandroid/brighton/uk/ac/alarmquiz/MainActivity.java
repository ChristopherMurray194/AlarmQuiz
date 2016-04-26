package helloandroid.brighton.uk.ac.alarmquiz;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.AnalogClock;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
public final static String EXTRA_MESSAGE = "helloandroid.brighton.uk.ac.alarmquiz.MESSAGE";
private static TextClock Digital;
private static AnalogClock Analog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        Digital = (TextClock)findViewById(R.id.digitalClock);
        Analog = (AnalogClock)findViewById(R.id.analogClock);

        SetupListView();
        OnAlarmClick();
    }

    private void SetupListView()
    {
        // Create a dynamic array list of
        ArrayList<String> alarmsArr = new ArrayList<String>();
        // Add it to the list of alarms
        alarmsArr.add("14:30");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,           // Context for the activity
                R.layout.alarm, // Layout to use
                alarmsArr       // Views to be displayed
        );

        ListView list = (ListView) findViewById(R.id.alarmList);
        list.setAdapter(adapter);
    }

    private void OnAlarmClick()
    {
        ListView list = (ListView) findViewById(R.id.alarmList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                ConfigureAlarm(view);
            }
        });
    }

    /** Called when the user clicks an item in the ListView */
    public void ConfigureAlarm(View view)
    {
        Intent intent = new Intent(this, ConfigAlarmActivity.class);

        startActivity(intent);
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
