package com.demoinsta.notiftestapp;

import static com.demoinsta.notiftestapp.MyNotifClass.CHANNEL_1_ID;
import static com.demoinsta.notiftestapp.MyNotifClass.CHANNEL_2_ID;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private NotificationManagerCompat notificationManager;
    private PendingIntent pendingIntent;
    private EditText editTextTitle;
    private EditText editTextMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notificationManager = NotificationManagerCompat.from(this);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextMessage = findViewById(R.id.edit_text_message);
// cheking if device is android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            CheckUserPermissions();
        }
        //used for when notification is clicked
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

    }
    
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void sendOnChannel1(View v) {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setAutoCancel(true)
                .setTimeoutAfter(36000000)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                            Manifest.permission.POST_NOTIFICATIONS},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        notificationManager.notify(1, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void sendOnChannel2(View v) {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_two)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                            Manifest.permission.POST_NOTIFICATIONS},
                    REQUEST_CODE_ASK_PERMISSIONS);
        return;
    }
        notificationManager.notify(2,notification);
}

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    void CheckUserPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                            Manifest.permission.POST_NOTIFICATIONS},
                    REQUEST_CODE_ASK_PERMISSIONS);
        }

    }

    final private int REQUEST_CODE_ASK_PERMISSIONS = 333;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                // Permission Denied
                Toast.makeText(this, "To receive updates about Live " +
                        "Matches allow notifications in settings", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}