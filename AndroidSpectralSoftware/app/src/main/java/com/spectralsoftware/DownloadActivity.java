package com.spectralsoftware;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.brickred.socialauth.Feed;

public class DownloadActivity extends AppCompatActivity {
    // SocialAuth Components

    List<Feed> feedList;

    public static List<Feed> allfeed = new ArrayList<Feed>();
    // Variables
    boolean status;
    public static int pos;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        getAllFeedList();
        feedList = allfeed;

        ListView list = (ListView) findViewById(R.id.feedList);
        list.setAdapter(new MyCustomAdapter(this, R.layout.feed_list_layout, feedList));
    }

    public static void sortFeeds (List<Feed> unsortFeeds){
        Collections.sort(unsortFeeds, new Comparator<Feed>() {
            public int compare(Feed m1, Feed m2) {
                SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                Date m1date = null;
                Date m2date = null;
                try {
                    m1date = format.parse(m1.getCreatedAt().toString());
                    m2date = format.parse(m2.getCreatedAt().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return m2date.compareTo(m1date);
            }
        });
    }

    private static void getAllFeedList(){
        allfeed.clear();

        if (MainActivity.getInstance().fb){
            if (MainActivity.getInstance().fbfeed != null && MainActivity.getInstance().fbfeed.size() > 0) {
                for (int i = 0; i < MainActivity.getInstance().fbfeed.size(); i++) {
                        allfeed.add(MainActivity.getInstance().fbfeed.get(i));
                    }
            } else {
                Log.d("Facebook", "Feed List Empty");
            }
        }

        if (MainActivity.getInstance().twitter){
            if (MainActivity.getInstance().twitterfeed != null && MainActivity.getInstance().twitterfeed.size() > 0) {

                for (int i = 0; i < MainActivity.getInstance().twitterfeed.size(); i++) {
                    allfeed.add(MainActivity.getInstance().twitterfeed.get(i));
                }
            } else {
                Log.d("Twitter", "Feed List Empty");
            }
        }

        if (MainActivity.getInstance().linkedin){
            if (MainActivity.getInstance().linkedinfeed != null && MainActivity.getInstance().linkedinfeed.size() > 0) {
                for (int i = 0; i < MainActivity.getInstance().linkedinfeed.size(); i++) {
                    allfeed.add(MainActivity.getInstance().linkedinfeed.get(i));
                }
            } else {
                Log.d("LinkedIn", "Feed List Empty");
            }
        }

        if (MainActivity.getInstance().instagram){
            if (MainActivity.getInstance().instagramfeed != null && MainActivity.getInstance().instagramfeed.size() > 0) {
                for (int i = 0; i < MainActivity.getInstance().instagramfeed.size(); i++) {
                    allfeed.add(MainActivity.getInstance().instagramfeed.get(i));
                }
            } else {
                Log.d("Instagram", "Feed List Empty");
            }
        }

        sortFeeds(allfeed);
    }

    private static  List<String> getUrlFromMessage(String value){
            List<String> result = new ArrayList<String>();
            String urlPattern = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
            Pattern p = Pattern.compile(urlPattern,Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(value);
            while (m.find()) {
                result.add(value.substring(m.start(0),m.end(0)));
            }
            return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllFeedList();
        feedList = allfeed;
        ListView list = (ListView) findViewById(R.id.feedList);
        list.setAdapter(new MyCustomAdapter(this, R.layout.feed_list_layout, feedList));
    }

    public class MyCustomAdapter extends ArrayAdapter<Feed> {
        private final LayoutInflater mInflater;
        List<Feed> feeds;

        public MyCustomAdapter(Context context, int textViewResourceId, List<Feed> feeds) {
            super(context, textViewResourceId);
            mInflater = LayoutInflater.from(context);
            this.feeds = feeds;
        }

        @Override
        public int getCount() {
            return feeds.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // return super.getView(position, convertView, parent);
            final Feed bean = feeds.get(position);

            View row = mInflater.inflate(R.layout.feed_list_layout, parent, false);

            TextView label = (TextView) row.findViewById(R.id.fName);
            TextView email = (TextView) row.findViewById(R.id.fMsg);
            TextView date = (TextView) row.findViewById(R.id.fDate);

            label.setText("From : " + bean.getFrom());
            email.setText(bean.getMessage());
            date.setText(" , Created At : " + bean.getCreatedAt().toString());

            RelativeLayout item = (RelativeLayout)row.findViewById(R.id.feedlistrelative);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(DownloadActivity.this, DetailView.class);
                    intent.putExtra("feed", bean);
                    startActivity(intent);
                }
            });

            return row;
        }
    }
}
