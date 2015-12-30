package com.spectralsoftware;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.brickred.socialauth.android.SocialAuthAdapter;

import java.util.jar.Manifest;

public class CredentialAdapter extends BaseAdapter{
    private final LayoutInflater mInflater;
    private final Context ctx;
    private Bitmap mIcon;
    public boolean clicked ;

    SocialAuthAdapter adapter;

    private final SocialAuthAdapter.Provider[] providers = new SocialAuthAdapter.Provider[] {
            SocialAuthAdapter.Provider.FACEBOOK, SocialAuthAdapter.Provider.TWITTER,
            SocialAuthAdapter.Provider.LINKEDIN, SocialAuthAdapter.Provider.INSTAGRAM
    };

    private final int[] images = new int[] {
            R.drawable.facebook, R.drawable.twitter, R.drawable.linkedin, R.drawable.instagram
    };

    public CredentialAdapter(Context context, SocialAuthAdapter mAdapter) {
        ctx = context;
        mInflater = LayoutInflater.from(ctx);
        adapter = mAdapter;
        clicked = false;
    }

    @Override
    public int getCount() {
        return providers.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unneccessary
        // calls to findViewById() on each row.
        ViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is no
        // need to reinflate it. We only inflate a new View when the convertView
        // supplied by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.providers_list, null);

            // Creates a ViewHolder and store references to the two children
            // views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.providerText);
            holder.icon = (ImageView) convertView.findViewById(R.id.provider);
            holder.signText = (TextView) convertView.findViewById(R.id.signstatus);

            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }

        mIcon = BitmapFactory.decodeResource(ctx.getResources(), images[position]);

        // Bind the data efficiently with the holder.

        String textCase = providers[position].toString();
        textCase = String.valueOf(textCase.charAt(0)).toUpperCase() + textCase.substring(1, textCase.length());

        holder.text.setText(textCase);
        holder.icon.setImageBitmap(mIcon);
        RelativeLayout listitemrelative = (RelativeLayout)convertView.findViewById(R.id.listitemrelative);
        //holder.text
        listitemrelative.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!CredentialsActivity.isClicked) {
                    CredentialsActivity.isClicked = true;
                    CredentialsActivity.pos = position;
                    MainActivity.getInstance().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    if (providers[position].equals(SocialAuthAdapter.Provider.INSTAGRAM))
                        adapter.addCallBack(SocialAuthAdapter.Provider.INSTAGRAM,
                            "http://opensource.brickred.com/socialauthdemo/socialAuthSuccessAction.do");

                    adapter.authorize(ctx, providers[position]);
                }
            }
        });

        holder.signText.setText("Sign In");
        holder.signText.setTag(1);

        holder.signText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String text = (String) ((TextView) v).getText();

                if (text.equalsIgnoreCase("sign out")) {
                    // Sign Out
                    boolean status = adapter.signOut(ctx, providers[position].toString());
                    Log.d("status", String.valueOf(status));
                    if (status) {
                        if (providers[position].equals(SocialAuthAdapter.Provider.FACEBOOK)) {
                            MainActivity.getInstance().fb = false;
                        }
                        else if(providers[position].equals(SocialAuthAdapter.Provider.TWITTER)) {
                            MainActivity.getInstance().twitter = false;
                        }
                        else if (providers[position].equals(SocialAuthAdapter.Provider.LINKEDIN)) {
                            MainActivity.getInstance().linkedin = false;
                        }
                        else if (providers[position].equals(SocialAuthAdapter.Provider.INSTAGRAM)) {
                            MainActivity.getInstance().instagram = false;
                        }

                        ((TextView) v).setText("Sign In");
                    }
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView text;
        ImageView icon;
        TextView signText;
    }
} // End of customAdapter

