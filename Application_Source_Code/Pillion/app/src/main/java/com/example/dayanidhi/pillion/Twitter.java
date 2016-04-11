package com.example.dayanidhi.pillion;

import android.app.Activity;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by dayanidhi on 30/03/16.
 */
public class Twitter extends Activity {
    @InjectView(R.id.user_avatar)
    ImageView avatar;
    @InjectView(R.id.user_id)
    TextView userId;
    @InjectView(R.id.user_name)
    TextView userName;
    @InjectView(R.id.btn_login_tw)
    View btnLoginTwitter;
    @InjectView(R.id.data_layout)
    ViewGroup dataLayout;
    @OnClick(R.id.btn_logout)

    void onLogoutClick() {
        logout();
    }

    @OnClick(R.id.btn_login_tw)
    void onLoginClick() {
        if (isConnectingToInternet()) {
            new GetTwitterTokenTask(this).execute();
            Log.i("Activity", "login");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter);
        ButterKnife.inject(this);
    }
    //twitter
    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public void callBackDataFromAsyncTask(User user) {
        userId.setText(String.valueOf(user.getId()));
        userName.setText(user.getName());
         try
        {
            // Creates a trace file in the primary external storage space of the
            // current application.
            // If the file does not exists, it is created.
            File traceFile = new File((this).getExternalFilesDir(null), "TraceFile.txt");
            if (!traceFile.exists())
                traceFile.createNewFile();
            // Adds a line to the trace file
            BufferedWriter writer = new BufferedWriter(new FileWriter(traceFile, true /*append*/));
            writer.write("Twitter:"+user.getStatus().getId()+":"+user.getStatus().getText());
            writer.close();
            // Refresh the data so it can seen when the device is plugged in a
            // computer. You may have to unplug and replug the device to see the
            // latest changes. This is not necessary if the user should not modify
            // the files.
            MediaScannerConnection.scanFile((this),
                    new String[]{traceFile.toString()},
                    null,
                    null);

        }
        catch (IOException e)
        {
            Log.e("Error", "Not able to create file...");
        }

        //end of streamin twitter

                //loading User Avatar by Picasso
                Picasso.with(this)
                        .load(user.getBiggerProfileImageURL())
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(avatar);
    }


    private void logout() {
        Log.i("Activity", "logout");
        dataLayout.setVisibility(View.GONE);
        userId.setText("");
        userName.setText("");
        btnLoginTwitter.setVisibility(View.VISIBLE);
    }



    //twitter
}
