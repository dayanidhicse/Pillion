package com.example.dayanidhi.pillion;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by Hong Thai
 */
public class GetTwitterTokenTask extends AsyncTask<String, Void, String> {

    private com.example.dayanidhi.pillion.Twitter activity;
    private String oauthURL, verifier;
    private Dialog dialog;
    private WebView webView;
    ProgressDialog progressBar;
    // Twitter variables
    private static Twitter twitter;
    private static RequestToken requestToken;
    private static AccessToken accessToken;
    private static final String TWITTER_CONSUMER_KEY = "9tHKDKGrZorLiNo1Cy9NFJn5P";
    private static final String TWITTER_CONSUMER_SECRET = "sXIYvUxecB5vsOCf9mfGmKHU0CopRXRWOHR1Mjx4he4wzSq6wZ";

    public GetTwitterTokenTask(com.example.dayanidhi.pillion.Twitter activity) {
        this.activity = activity;
        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //showing a progress dialog
        progressBar = new ProgressDialog(activity);
        progressBar.setMessage("Connecting...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setCancelable(false);
        progressBar.show();
    }

    @Override
    protected String doInBackground(String... args) {

        try {
            requestToken = twitter.getOAuthRequestToken();
            oauthURL = requestToken.getAuthorizationURL();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return oauthURL;
    }

    @Override
    protected void onPostExecute(String oauthUrl) {
        if (oauthUrl != null) {
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.setContentView(R.layout.auth_dialog);
            webView = (WebView) dialog.findViewById(R.id.webv);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(oauthUrl);

            webView.setWebViewClient(new WebViewClient() {
                boolean authComplete = false;

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (url.contains("oauth_verifier") && authComplete == false) {
                        authComplete = true;
                        Log.e("AsyncTask", url);
                        Uri uri = Uri.parse(url);
                        verifier = uri.getQueryParameter("oauth_verifier");

                        dialog.dismiss();
                        new AccessTokenGetTask().execute();
                    } else if (url.contains("denied")) {
                        dialog.dismiss();
                        Toast.makeText(activity, "Permission is Denied", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.show();
            dialog.setCancelable(true);
            progressBar.dismiss(); //dismiss progress dialog when task finished.
        } else {
            Toast.makeText(activity, "Network Error!", Toast.LENGTH_SHORT).show();
        }
    }

    private class AccessTokenGetTask extends AsyncTask<String, String, User> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = new ProgressDialog(activity);
            progressBar.setMessage("Fetching Data ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setCancelable(false);
            progressBar.show();
        }

        @Override
        protected User doInBackground(String... args) {
            User user = null;
            try {
                accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
                user = twitter.showUser(accessToken.getUserId());
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return user;
        }

        @Override
        protected void onPostExecute(User response) {
            if (response == null) {
                Log.e("AsyncTask", "null user");
            } else {
                activity.callBackDataFromAsyncTask(response);
            }
            progressBar.dismiss();
        }
    }
}
