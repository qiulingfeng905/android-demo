package com.xxoo.android.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.xxoo.android.demo.app.Installation;

public class MainDemo extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        String channelID = Installation.getJuChannelId(getApplicationContext());
        TextView v = (TextView)findViewById(R.id.main_tv_channel_id);
        v.setText("ttid = " + channelID);
    }
}
