package com.spectralsoftware;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import org.brickred.socialauth.Feed;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        Feed feed;
        feed = (Feed)getIntent().getSerializableExtra("feed");

        TextView from = (TextView)findViewById(R.id.name);
        TextView date = (TextView)findViewById(R.id.date);
        TextView message = (TextView)findViewById(R.id.message);
        WebView webView = (WebView)findViewById(R.id.webview);

        from.setText ( feed.getFrom());
        date.setText ((CharSequence) feed.getCreatedAt().toString());
        message.setText(feed.getMessage());

        List<String> Url = getUrlFromMessage((feed.getMessage()));
        for (int i = 0; i < Url.size(); i++)
            webView.loadUrl(Url.get(i));
    }

    private static List<String> getUrlFromMessage(String value){
        List<String> result = new ArrayList<String>();
        String urlPattern = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlPattern,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(value);
        while (m.find()) {
            result.add(value.substring(m.start(0),m.end(0)));
        }
        return result;
    }
}
