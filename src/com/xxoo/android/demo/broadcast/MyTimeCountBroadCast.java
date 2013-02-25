package com.xxoo.android.demo.broadcast;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * User: qinmiao.caoqm
 * Date: 13-2-25
 * Time: 上午11:28
 */
public class MyTimeCountBroadCast  extends Service {
    public static final String COUNT_ACTION = "com.xxoo.android.demo.count";

    public void sendTimeCountBroadcast(Context context) {

        Intent intent = new Intent(COUNT_ACTION);
        //API 17
        context.sendOrderedBroadcast(intent, null);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
