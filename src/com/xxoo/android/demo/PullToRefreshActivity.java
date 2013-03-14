package com.xxoo.android.demo;

import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.xxoo.android.demo.listview.PullToRefreshListView;


import java.text.SimpleDateFormat;
import java.util.*;

public class PullToRefreshActivity extends ListActivity {    
    private LinkedList<String> mListItems;
    private int pullCnt = 0;
    private SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
    ListviewAdapter adapter;
    //List<String> items = new ArrayList<String>();
    private boolean isHeader;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pull_to_refresh);

        // Set a listener to be invoked when the list should be refreshed.
        ((PullToRefreshListView) getListView()).setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh(boolean isHead) {
                // Do work to refresh the list here.
                new GetDataTask().execute(isHead);
            }
        });

        mListItems = new LinkedList<String>();
        mListItems.addAll(Arrays.asList(mStrings));
//
//        adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, mListItems);
//        items = new LinkedList<String>();
//        items.addAll(Arrays.asList(mStrings));
        adapter = new ListviewAdapter(this);
        getListView().setEmptyView(findViewById(R.id.listview_empty));
        setListAdapter(adapter);
    }

    private class GetDataTask extends AsyncTask<Boolean, Void, String[]> {

        @Override
        protected String[] doInBackground(Boolean ... params) {
            // Simulates a background job.
            isHeader = params[0];
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                ;
            }
            return mStrings;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (isHeader) {
                mListItems.addFirst("refresh header " + String.valueOf(++pullCnt));
            } else {
                mListItems.addLast("refresh footer " + String.valueOf(++pullCnt));
            }
            adapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.
            ((PullToRefreshListView) getListView()).onRefreshComplete("上次更新: " + sdf.format(new Date()), isHeader);


            super.onPostExecute(result);
        }
    }

    private String[] mStrings = {
            "Abbaye de", "Abbaye du ", "Abertam",
            "Abondance", "Ackawi", "Acorn", "Adelost", "Affidelice",
            "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler"};


    public class ListviewAdapter extends BaseAdapter{

        private LayoutInflater mInflater;

        public ListviewAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return mListItems.size();
        }

        public String getItem(int position) {
            return mListItems.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if ( convertView == null ){
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.list_item_view, parent, false);
                viewHolder.text = (TextView) convertView.findViewById(R.id.list_item_image_text);
                viewHolder.icon = (ImageView) convertView.findViewById(R.id.list_item_image);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.text.setText(getItem(position));
            viewHolder.icon.setImageResource(R.drawable.jpush_notification_icon);

            return convertView;
        }
    }

    public static class ViewHolder {
        TextView text;
        ImageView icon;
    }
}
