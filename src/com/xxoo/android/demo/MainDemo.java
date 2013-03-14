package com.xxoo.android.demo;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import com.xxoo.android.demo.app.Installation;
import com.xxoo.android.demo.app.push.MyUtil;
import com.xxoo.android.demo.app.push.PushSetActivity;
import com.xxoo.android.demo.broadcast.MyTimeCountBroadCast;
import com.xxoo.android.demo.fragment.MyFragment;

import java.util.Timer;
import java.util.TimerTask;

public class MainDemo extends InstrumentedActivity implements View.OnClickListener {
    private static final String TAG = "MyActivity";
    private Button mSetting;
    private TextView mTimeCount;
    private MyBroadCastReceiver myBroadCastReceiver;
    MyTimeCountBroadCast timeCountBroadCast = new MyTimeCountBroadCast();
    long curValue = 0L;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, TAG + "--->onCreate");
        setContentView(R.layout.main);
        String channelID = Installation.getJuChannelId(getApplicationContext());
        TextView v = (TextView) findViewById(R.id.main_tv_channel_id);
        mTimeCount = (TextView) findViewById(R.id.tv_time_count);
        v.setText("ttid = " + channelID);
        mTimeCount.setText(String.valueOf(curValue++));
        initView();
        findViewById(R.id.btn_iat_demo).setOnClickListener(this);
        findViewById(R.id.fragment).setOnClickListener(this);
        findViewById(R.id.listview).setOnClickListener(this);
    }

    private void initView() {
        TextView mImei = (TextView) findViewById(R.id.tv_imei);
        String udid = JPushInterface.getUdid(getApplicationContext());
        if (null != udid) mImei.setText("IMEI: " + udid);

        TextView mAppKey = (TextView) findViewById(R.id.tv_appkey);
        String appKey = MyUtil.getAppKey(getApplicationContext());
        if (null == appKey) appKey = "AppKey异常";
        mAppKey.setText("AppKey: " + appKey);

        String packageName = getPackageName();
        TextView mPackage = (TextView) findViewById(R.id.tv_package);
        mPackage.setText("PackageName: " + packageName);

        String versionName = MyUtil.GetVersion(getApplicationContext());
        TextView mVersion = (TextView) findViewById(R.id.tv_version);
        mVersion.setText("Version: " + versionName + "\r\n" + stringFromJNI());

        mSetting = (Button) findViewById(R.id.setting);
        mSetting.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_iat_demo:
                intent = new Intent(MainDemo.this, IatDemoActivity.class);
                startActivity(intent);
                break;
            case R.id.setting:
                intent = new Intent(MainDemo.this, PushSetActivity.class);
                startActivity(intent);
                break;
            case R.id.listview:
                intent = new Intent(MainDemo.this, PullToRefreshActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment:
//                MyFragment dialogFragment = MyFragment.newInstance(
//                        "Are you sure you want to do this?");
//                          dialogFragment.show(getFragmentManager(), "dialog");
                showDialog();
                break;
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(MyTimeCountBroadCast.COUNT_ACTION);
        filter.setPriority(1);
        myBroadCastReceiver = new MyBroadCastReceiver();
        registerReceiver(myBroadCastReceiver, filter);


        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                timeCountBroadCast.sendTimeCountBroadcast(getApplicationContext());
            }
        }, 10 * 1000, 1000
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(myBroadCastReceiver);
    }

    //广播处理
    public class MyBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MyTimeCountBroadCast.COUNT_ACTION)) {
                abortBroadcast();
                mTimeCount.setText(String.valueOf(curValue++));
            }
        }
    }

    //.so方法调用
    /* A native method that is implemented by the
     * 'hello-jni' native library, which is packaged
     * with this application.
     */
    public native String  stringFromJNI();


    /* this is used to load the 'hello-jni' library on application
     * startup. The library has already been unpacked into
     * /data/data/com.xxoo.android.demo/lib/libhello-jni.so at
     * installation time by the package manager.
     */
    static {
        System.loadLibrary("hello-jni");
    }

    public void showDialog() {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        MyFragment newFragment = MyFragment.newInstance("dialog");
        //这里会完成 commit 和 add Tag
        newFragment.show(ft, "dialog");
    }
}
