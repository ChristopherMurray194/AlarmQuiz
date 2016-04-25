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
import android.widget.Button;
import android.widget.AnalogClock;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.TextClock;

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

        Button ToggleClock = (Button) findViewById(R.id.toggleClock);
        Digital = (TextClock)findViewById(R.id.digitalClock);
        Analog = (AnalogClock)findViewById(R.id.analogClock);
        ToggleClock.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // If the digtial clock is not visible
                if(Digital.getVisibility() == TextClock.GONE)
                {
                    // Make it visible
                    Digital.setVisibility(TextClock.VISIBLE);
                    // Hide the analog clock
                    Analog.setVisibility(AnalogClock.GONE);
                }// If the analog clock is not visible
                else
                {
                    // Hide the text clock
                    Digital.setVisibility(TextClock.GONE);
                    // Mkae the analog clock visible
                    Analog.setVisibility(AnalogClock.VISIBLE);
                }
            }
        });

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

    /** Called when the user clicks the send button */
    public void sendMessage(View view)
    {
        // Create a new intent to start an activity
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        // Get the EditText element
        EditText editText = (EditText) findViewById(R.id.edit_message);
        // Store the message
        String message = editText.getText().toString();
        // Add the message to the intent
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
