package com.spectralsoftware;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import org.brickred.socialauth.Feed;

import java.util.List;

public class MainActivity extends TabActivity {
    private static final String CREDENTIAL = "Credential";
    private static final String DOWNLOAD = "Download";
    private static MainActivity instance = null;
    public List<Feed> allfeed;
    public boolean fb = false, twitter=false, linkedin = false, instagram = false;
    public List<Feed> fbfeed, twitterfeed, linkedinfeed, instagramfeed;
    public static MainActivity getInstance(){
        return instance;
    }

      @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = getTabHost();

        // Inbox Tab
        TabSpec credentialSpec = tabHost.newTabSpec(CREDENTIAL);
        // Tab Icon
        credentialSpec.setIndicator(CREDENTIAL, getResources().getDrawable(R.drawable.button_gradient));
        Intent inboxIntent = new Intent(this, CredentialsActivity.class);
        // Tab Content
        credentialSpec.setContent(inboxIntent);

        // Outbox Tab
        TabSpec downloadSpec = tabHost.newTabSpec(DOWNLOAD);
        downloadSpec.setIndicator(DOWNLOAD, getResources().getDrawable(R.drawable.button_gradient));
        Intent outboxIntent = new Intent(this, DownloadActivity.class);
        downloadSpec.setContent(outboxIntent);
          instance = this;
        // Adding all TabSpec to TabHost
        tabHost.addTab(credentialSpec); // Adding Inbox tab
        tabHost.addTab(downloadSpec); // Adding Outbox tab

    }
}


