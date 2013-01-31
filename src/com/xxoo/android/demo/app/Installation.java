package com.xxoo.android.demo.app;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;


import java.io.*;


public class Installation {

	// 渠道id
	private static String channelId = null;
	private static final String CHANNELIDFILE = "ttid.dat";

	private static final String TAG = "Installation";


	public synchronized static String getJuChannelId(Context context) {

        if (!TextUtils.isEmpty(channelId)) {
            return channelId;
        }
        channelId = "";
		InputStreamReader isr = null;
		InputStream is = null;
		char[] inputBuffer = new char[255];
		StringBuffer buff = new StringBuffer();

		try {
			AssetManager am = context.getAssets();
			is = am.open(CHANNELIDFILE);
			isr = new InputStreamReader(is);
			int i = 0;
			while ((i = isr.read(inputBuffer, 0, 255)) != -1) {
				buff.append(new String(inputBuffer, 0, i));
			}
            channelId = buff.toString();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (isr != null) {
					isr.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        Log.d(TAG,"channelId = " + channelId);
        return channelId;
	}


}
