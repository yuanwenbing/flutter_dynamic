package com.yuan.dynamic;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by yuan on 2019-11-15.
 * Email:yuanwb@yiche.com
 */
public class Utils {
    public static void restartApp(Activity activity) {
        Intent startActivity = new Intent(activity, MainActivity.class);
        int pendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(activity, pendingIntentId, startActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        assert mgr != null;
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        activity.finish();
        System.exit(0);
    }
}
