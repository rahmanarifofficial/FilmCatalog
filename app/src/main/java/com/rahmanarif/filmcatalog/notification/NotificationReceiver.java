package com.rahmanarif.filmcatalog.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.rahmanarif.filmcatalog.MainActivity;
import com.rahmanarif.filmcatalog.R;
import com.rahmanarif.filmcatalog.model.Movie;
import com.rahmanarif.filmcatalog.ui.activity.DetailActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.rahmanarif.filmcatalog.ui.activity.DetailActivity.EXTRA_MOVIE_ID;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String TYPE_DAILY_REMINDER = "Daily Reminder";
    public static final String TYPE_RELEASE_REMINDER = "Release Reminder";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_ID_MOVIE = "movie_id";
    public static final String EXTRA_TYPE = "type";
    private final int ID_DAILY_REMINDER = 101;
    private int ID_RELEASE_REMINDER = 100;

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = type.equalsIgnoreCase(TYPE_DAILY_REMINDER) ? context.getString(R.string.daily_notif_message, context.getString(R.string.app_name)) :
                context.getString(R.string.daily_notif_message, intent.getStringExtra(EXTRA_MESSAGE));
        String movieId = intent.getStringExtra(EXTRA_ID_MOVIE);
        int notifId = type.equalsIgnoreCase(TYPE_DAILY_REMINDER) ? ID_DAILY_REMINDER : ID_RELEASE_REMINDER;

        showAlarmNotification(context, context.getString(R.string.app_name), message, notifId, movieId);
    }

    public void setDailyNotification(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra(EXTRA_TYPE, type);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY_REMINDER, intent, 0);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            );
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );
        }

        Toast.makeText(context, context.getString(R.string.daily_notif_on), Toast.LENGTH_SHORT).show();
    }

    public void setReleaseNotification(Context context, String type, List<Movie> movies) {
        int delay = 0;
        for (Movie movie : movies) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, NotificationReceiver.class);
            intent.putExtra(EXTRA_MESSAGE, movie.getTitle());
            intent.putExtra(EXTRA_ID_MOVIE, movie.getId().toString());
            intent.putExtra(EXTRA_TYPE, type);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE_REMINDER, intent, 0);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                alarmManager.setInexactRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis() + delay,
                        AlarmManager.INTERVAL_DAY,
                        pendingIntent
                );
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis() + delay,
                        pendingIntent
                );
            }

            ID_RELEASE_REMINDER++;
            delay += 3000;
        }
    }

    public void cancelNotification(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_DAILY_REMINDER) ? ID_DAILY_REMINDER : ID_RELEASE_REMINDER;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        if (type.equalsIgnoreCase(TYPE_DAILY_REMINDER)) {
            Toast.makeText(context, context.getString(R.string.daily_notif_off), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getString(R.string.release_notif_off), Toast.LENGTH_SHORT).show();
        }
    }

    private void showAlarmNotification(Context context, String title, String message, int notifId, String movieId) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent toMain = new Intent(context, MainActivity.class);
        Intent toDetail = new Intent(context, DetailActivity.class);
        toDetail.putExtra(EXTRA_MOVIE_ID, movieId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifId,
                notifId == ID_DAILY_REMINDER ? toMain : toDetail, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_movie_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(alarmSound);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);
            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }
        Notification notification = builder.build();
        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }
    }
}
