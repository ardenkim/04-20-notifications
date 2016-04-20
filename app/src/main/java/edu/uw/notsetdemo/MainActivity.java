package edu.uw.notsetdemo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "**Main**";

    public static final String ACTION_SMS_SENT = "edu.uw.notsetdemo.ACTION_SMS_SENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View launchButton = findViewById(R.id.btnLaunch);
        launchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v(TAG, "Launch button pressed");

                //explicit intent
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("edu.uw.notsetdemo.message", "Hello from MainActivity!");

                startActivity(intent);
            }
        });

        //shared preferences
        // get the file xml
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // tell editors to do the following
        editor.putString("message", "Hello World");
        // in order to save those changes
        editor.commit();

        Log.v(TAG, prefs.getString("message", ""));
    }

    public void callNumber(View v) {
        Log.v(TAG, "Call button pressed");

        //implicit intent
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:206-685-1622"));

        if(intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);

    }

    private static int REQUEST_PICTURE_CODE = 1;

    public void takePicture(View v) {
        Log.v(TAG, "Camera button pressed");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_PICTURE_CODE);
        }

    }

    private static final int SMS_SEND_CODE = 2;
    public void sendMessage(View v) {
        Log.v(TAG, "Message button pressed");

        SmsManager smsManager = SmsManager.getDefault();

        Intent smsIntent = new Intent(ACTION_SMS_SENT);    //implicit intent

        //wrap it up in the pending intent
        // wrap a letter in a envelop with a envelop to give it to someone who will write me a letter
        // Actually gets sent off when the manager says its okay
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, SMS_SEND_CODE, smsIntent, 0);

        smsManager.sendTextMessage("5554", null, "This is a message!", null, null);
    }

    private static final int NOTIFY_CODE = 0;
    private int notifyCount = 0;

    public void notify(View v){
        Log.v(TAG, "Notify button pressed");

        notifyCount++;

        // last thing we did in lecture (if and else)
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(true);
        boolean showPopup = prefs.getBoolean("pref_show_notification", true);
        if(showPopup) {

            // copied code from Android Developer
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle("You're on notice")
                            .setContentText("This is notification " + notifyCount);

            // what should be considered as MAX, HIGH, DEFAULT, LOW, or MIN priority
            // also used for sorting dropdown menu
            // giving a high priority
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
            // if we want it to pop up, it should either vibrate the phone or play a sound
            builder.setVibrate(new long[]{0, 500, 500, 500});
            // if we want to set a sound
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);


            // creating intent
            // Why?
            // Notification actions
            // When notifications appear on your phone, in order to do something, we click on it, and intent gets sent
            // When I click on a notification, here's what I want to do
            Intent intent = new Intent(this, SecondActivity.class);

            // when I hit back, I want to go to first activity --> fake history
            // artificial back stack needed!
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

            // adding parents, adding hierarchy history
            stackBuilder.addParentStack(SecondActivity.class);

            // take the stack and add what intent we want to happend
            stackBuilder.addNextIntent(intent);

            // grab us the pending intent
            // prev intent standing a line, kick out the guy used to be in line --> instead of 2 new emails, it says 1 new email
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntent); // what to happen when clicked

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // id code (NOTIFY_CODE) allows you to update the notification later on.
            // still replace prev notification because of NOTIFY_CODE
            mNotificationManager.notify(NOTIFY_CODE, builder.build()); // issue a new notification
        } else {
            Toast.makeText(this, "Popup!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_PICTURE_CODE && resultCode == RESULT_OK){
            //I got picture!!
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap)extras.get("data");

            ImageView imageView = (ImageView)findViewById(R.id.imgThumbnail);
            imageView.setImageBitmap(imageBitmap);

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_item_notify:
                notify(null);
                return true;
            case R.id.menu_item_prefs:
                Log.v(TAG, "Settings button pressed");

                Intent intent = new Intent(MainActivity.this, SettingsActivity);
                startActivity(intent);
                return true;
            case R.id.menu_item_click:
                Log.v(TAG, "Extra button pressed");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
