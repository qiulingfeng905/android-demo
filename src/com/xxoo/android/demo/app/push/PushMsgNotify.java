package com.xxoo.android.demo.app.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.taobao.ju.android.R;
import com.taobao.ju.android.activity.WelcomeActivity;
import com.taobao.ju.android.common.AppHolder;
import com.taobao.ju.android.common.Log;

/**
 * User: qinmiao.caoqm
 * Date: 13-1-9
 * Time: 上午9:29
 */
public class PushMsgNotify {
    public static int notifyId = 0;

    public static void showNotification(Context context, String title, String content){
        try {
            Notification notification = new Notification(R.drawable.ic_launcher, title, System.currentTimeMillis());
            NotificationManager manager = (NotificationManager) AppHolder.getContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setClass(context, WelcomeActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pIntent = PendingIntent.getActivity(context, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT
            );

            notification.setLatestEventInfo(context, title, content, pIntent);

            //采用默认提醒
            notification.defaults = Notification.DEFAULT_ALL;
            //点击消失
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            manager.notify(R.layout.push_msg_notification + notifyId++, notification);
        } catch (Exception e) {
            e.printStackTrace();
            if(Log.isPrintable()) Log.e("","消息通知栏异常...");
        }

    }

    public static void showNotificationBySetting(Context context, String title, String content) {
        if (PushMsgManager.isNoticeTime()) {
            showNotification(context, title, content);
        } else {
            if (Log.isPrintable()) Log.d("", "用户设置不显示通知...");
        }
    }
}
