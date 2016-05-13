package helloandroid.brighton.uk.ac.alarmquiz;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.widget.Toast;
import android.media.MediaPlayer;

public class PersistentAlarm extends Service
{
    IBinder mBinder;

    public PersistentAlarm()
    {
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // Play the default ringtone
        MediaPlayer player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        player.start();

        // Vibrate
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for the duration of the ringtone playback
        vibrator.vibrate(player.getDuration());

        Toast.makeText(this, "Alarm Time Reached", Toast.LENGTH_LONG).show();
        //onDestroy();
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

}
