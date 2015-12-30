package com.spectralsoftware;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.brickred.socialauth.Feed;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;
import java.util.ArrayList;
import java.util.List;

public class CredentialsActivity extends AppCompatActivity {
    // SocialAuth Components
    private static SocialAuthAdapter adapter;
    private ListView listview;
    private ProgressDialog progressDialog;

    // Variables
    private String providerName;
    public static int pos;
    public static boolean isClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);

        adapter = new SocialAuthAdapter(new ResponseListener(CredentialsActivity.this));

        listview = (ListView) findViewById(R.id.listview);
        listview.setDivider(null);

        listview.setAdapter(new CredentialAdapter(this, adapter));
        MainActivity.getInstance().allfeed = new ArrayList<Feed>();
        MainActivity.getInstance().fbfeed = new ArrayList<Feed>();
        MainActivity.getInstance().twitterfeed = new ArrayList<Feed>();
        MainActivity.getInstance().linkedinfeed = new ArrayList<Feed>();
        MainActivity.getInstance().instagramfeed = new ArrayList<Feed>();
        isClicked = false;
        progressDialog = null;
     }

    // To receive the response after authentication
    private final class ResponseListener implements DialogListener {

        CredentialsActivity cActivity;

        public ResponseListener(Context ctx){
            cActivity = (CredentialsActivity)ctx;
        }
        @Override
        public void onComplete(Bundle values) {

            // Changing Sign In Text to Sign Out
            View v = listview.getChildAt(pos - listview.getFirstVisiblePosition());
            TextView pText = (TextView) v.findViewById(R.id.signstatus);
            pText.setText("Sign Out");

            // Get the provider
            providerName = values.getString(SocialAuthAdapter.PROVIDER);

            if (providerName == "facebook")
                MainActivity.getInstance().fb = true;
            else if(providerName == "twitter")
                MainActivity.getInstance().twitter = true;
            else if (providerName == "linkedin")
                MainActivity.getInstance().linkedin = true;
            else if (providerName == "instagram")
                MainActivity.getInstance().instagram = true;

            adapter.getFeedsAsync(new FeedDataListener(cActivity));

            if (progressDialog == null) {
                SpannableString msg=  new SpannableString("Downloading");
                msg.setSpan(new RelativeSizeSpan(2f), 0, msg.length(), 0);
                msg.setSpan(new ForegroundColorSpan(Color.BLACK), 0, msg.length(), 0);

                progressDialog = new ProgressDialog(CredentialsActivity.this);
                progressDialog.setMessage(msg);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
            Toast.makeText(CredentialsActivity.this, providerName + " connected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SocialAuthError error) {
    //        cActivity.showToast("Check Internet Connection");
            isClicked = false;
            MainActivity.getInstance().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            error.printStackTrace();
        }

        @Override
        public void onCancel() {
            isClicked = false;
            MainActivity.getInstance().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        @Override
        public void onBack() {
            isClicked = false;
            MainActivity.getInstance().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private final class FeedDataListener implements SocialAuthListener<List<Feed>> {
        CredentialsActivity cActivity;
        public FeedDataListener(Context ctx){
            cActivity = (CredentialsActivity)ctx;
        }
        @Override
        public void onExecute(String provider, List<Feed> t) {
            List<Feed> feedList = t;

            if (feedList == null)
                Toast.makeText(CredentialsActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();

            if (provider == "facebook")
                MainActivity.getInstance().fbfeed = feedList;
            else if(provider == "twitter")
                MainActivity.getInstance().twitterfeed = feedList;
            else if (provider == "linkedin")
                MainActivity.getInstance().linkedinfeed = feedList;
            else if (provider == "instagram")
                MainActivity.getInstance().instagramfeed = feedList;

            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
                isClicked = false;
                MainActivity.getInstance().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(CredentialsActivity.this, "Downloading Finished", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SocialAuthError e) {
            isClicked = false;
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
            MainActivity.getInstance().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            cActivity.showToast("Downloading Failed");
            e.printStackTrace();
        }
    }

    public void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}



