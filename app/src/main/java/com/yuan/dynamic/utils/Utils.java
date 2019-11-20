package com.yuan.dynamic.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.yuan.dynamic.MainActivity;

/**
 * Created by yuan on 2019-11-15.
 * Email:yuanwb@yiche.com
 */
public class Utils {
    public static void restartApp(Activity activity) {
        Intent startActivity = new Intent(activity, MainActivity.class);
        int pendingIntentId = 123456;
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, pendingIntentId, startActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        if(alarmManager != null) {
            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
            activity.finish();
            System.exit(0);
        }
    }
}
