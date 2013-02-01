package com.xxoo.android.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import com.xxoo.android.demo.app.Installation;
import com.xxoo.android.demo.app.push.MyUtil;
import com.xxoo.android.demo.app.push.PushSetActivity;

public class MainDemo extends InstrumentedActivity implements View.OnClickListener
{

    private Button mInit;
   	private Button mSetting;
   	private Button mStopPush;
   	private Button mResumePush;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        String channelID = Installation.getJuChannelId(getApplicationContext());
        TextView v = (TextView)findViewById(R.id.main_tv_channel_id);
        v.setText("ttid = " + channelID);
        initView();
    }

    private void initView(){
   		TextView mImei = (TextView) findViewById(R.id.tv_imei);
   		String udid =  JPushInterface.getUdid(getApplicationContext());
           if (null != udid) mImei.setText("IMEI: " + udid);

   		TextView mAppKey = (TextView) findViewById(R.id.tv_appkey);
   		String appKey = MyUtil.getAppKey(getApplicationContext());
   		if (null == appKey) appKey = "AppKey异常";
   		mAppKey.setText("AppKey: " + appKey);

   		String packageName =  getPackageName();
   		TextView mPackage = (TextView) findViewById(R.id.tv_package);
   		mPackage.setText("PackageName: " + packageName);

   		String versionName =  MyUtil.GetVersion(getApplicationContext());
   		TextView mVersion = (TextView) findViewById(R.id.tv_version);
   		mVersion.setText("Version: " + versionName);

   	    mInit = (Button)findViewById(R.id.init);
   		mInit.setOnClickListener(this);

   		mStopPush = (Button)findViewById(R.id.stopPush);
   		mStopPush.setOnClickListener(this);

   		mResumePush = (Button)findViewById(R.id.resumePush);
   		mResumePush.setOnClickListener(this);

   		mSetting = (Button)findViewById(R.id.setting);
   		mSetting.setOnClickListener(this);

   	}

   	@Override
   	public void onClick(View v) {
   		switch (v.getId()) {
   		case R.id.init:
   			init();
   			break;
   		case R.id.setting:
   			Intent intent = new Intent(MainDemo.this, PushSetActivity.class);
   			startActivity(intent);
   			break;
   		case R.id.stopPush:
   			JPushInterface.stopPush(getApplicationContext());
   			break;
   		case R.id.resumePush:
   			JPushInterface.resumePush(getApplicationContext());
   			break;
   		}
   	}

   	// 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
   	private void init(){
   		JPushInterface.init(getApplicationContext());
   	}
}
