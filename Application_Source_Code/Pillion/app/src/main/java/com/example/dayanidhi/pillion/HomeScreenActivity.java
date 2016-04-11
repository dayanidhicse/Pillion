package com.example.dayanidhi.pillion;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.MessengerThreadParams;
import com.facebook.messenger.ShareToMessengerParams;
import com.firebase.client.Firebase;
import com.squareup.picasso.Picasso;

import br.liveo.interfaces.OnItemClickListener;
import br.liveo.interfaces.OnPrepareOptionsMenuLiveo;
import br.liveo.model.HelpLiveo;
import br.liveo.navigationliveo.NavigationLiveo;
import butterknife.InjectView;
import butterknife.OnClick;
import twitter4j.User;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class HomeScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 1;

    private static final String TAG = "RecyclerViewExample";
    private List<FeedItem> feedsList;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter adapter;
    private ProgressBar progressBar;
    private MessengerThreadParams mThreadParams;
    private boolean mPicking;

    private HelpLiveo mHelpLiveo;

    public Account obj;
    FloatingActionButton offer,findaride;
    Button current,history;
    ImageView profile;
    String mobile;
    TextView usern;
    Bitmap bitmap;
    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ButterKnife.inject(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        offer=(FloatingActionButton)findViewById(R.id.offeraride);
        findaride =(FloatingActionButton)findViewById(R.id.findaride);
        current=(Button)findViewById(R.id.current);
        history=(Button)findViewById(R.id.history);
        Firebase.setAndroidContext(this);

        offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(HomeScreenActivity.this,OfferARide.class);
                a.putExtra("Account",obj);
                startActivity(a);
            }
        });
        findaride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(HomeScreenActivity.this,FindARide1.class);
                a.putExtra("Account",obj);
                startActivity(a);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_home_screen, navigationView, false);
        navigationView.addHeaderView(headerView);
        TextView Name = (TextView) headerView.findViewById(R.id.usern);
        TextView Mail = (TextView) headerView.findViewById(R.id.mailid);
        //  Name.setText("Myname");
       profile=(ImageView) headerView.findViewById(R.id.dp);
        mobile=this.getIntent().getExtras().getString("mobile");
        obj = (Account)this.getIntent().getSerializableExtra("Account");
        Name.setText(obj.fname);
        Mail.setText(obj.email);


        String url="http://graph.facebook.com/"+obj.userid+"/picture?type=large";
         //  new LoadImage().execute(url);

        Picasso.with(getApplicationContext()).load(url).into(profile);

        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(20));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        // Downloading data from below url
        new AsyncHttpTask().execute();



        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar = (ProgressBar) findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.VISIBLE);

                new AsyncHttpTask().execute();
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar = (ProgressBar) findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.VISIBLE);

                new AsyncHttpTask2().execute();
            }
        });


        Intent intent = getIntent();
        if (Intent.ACTION_PICK.equals(intent.getAction())) {
            mThreadParams = MessengerUtils.getMessengerThreadParamsForIntent(intent);
            mPicking = true;

            // Note, if mThreadParams is non-null, it means the activity was launched from Messenger.
            // It will contain the metadata associated with the original content, if there was content.
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.yourrides) {
            Intent a = new Intent(HomeScreenActivity.this,HomeScreenActivity.class);
            a.putExtra("Account",obj);
            startActivity(a);
        } else if (id == R.id.offerride) {
            Intent a = new Intent(HomeScreenActivity.this,OfferARide.class);
            a.putExtra("Account",obj);
            startActivity(a);
        } else if (id == R.id.findride) {
            Intent a = new Intent(HomeScreenActivity.this,FindARide1.class);
            a.putExtra("Account",obj);
            startActivity(a);
        } else if (id == R.id.Ratings) {
            Intent a = new Intent(HomeScreenActivity.this,RatingsView.class);
            a.putExtra("Account",obj);
            startActivity(a);

        }else if (id == R.id.wallet) {
            Intent a = new Intent(HomeScreenActivity.this,Wallet.class);
            a.putExtra("Account",obj);
            startActivity(a);
        }
         else if (id == R.id.logout) {
            Intent a = new Intent(HomeScreenActivity.this,LoginActivity.class);
            a.putExtra("dp","");

            startActivity(a);
        }
        else if(id == R.id.twitter){
            Intent i= new Intent(HomeScreenActivity.this, com.example.dayanidhi.pillion.Twitter.class);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }





    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Integer doInBackground(String... params) {

            try {
                feedsList = new ArrayList<>();
                CloudantClient client = ClientBuilder.account("7f1f486a-1104-47bd-add4-62663474ac15-bluemix")
                        .username("7f1f486a-1104-47bd-add4-62663474ac15-bluemix")
                        .password("93b4b2805dcaa6ecbe5277cd7baf4b9bd67d78750f78398a242cf1afabd0486b")
                        .build();

                Database db = client.database("pillion", false);
                obj = db.find(Account.class,obj._id);                //   System.out.println(db.listIndices());
                //    List al = new ArrayList<Schedule>();
                // ArrayList<Schedule> new ArrayList<Schedule>();
                //   String arr[]=;
                // int c=0;
                // Date mTime = new Date();
                // mTime.setDate(mTime.getDay()-1);
                Calendar c = Calendar.getInstance();
                // System.out.println("Current time => " + c.getTime());
                c.add(Calendar.DATE, -1);

                SimpleDateFormat df = new SimpleDateFormat("d-M-yyyy");
                String formattedDate = df.format(c.getTime());



                //String current_date = new SimpleDateFormat("d-M-yyyy").format(mTime);
                System.out.println(formattedDate);
                List<Schedule> al=  db.findByIndex("{\"selector\":{\"mobile\":\"" + obj._id + "\"}}", Schedule.class);
                for(Schedule a:al){
                    System.out.println(a.trip_id);
                    System.out.println(a.co_traveller_id);


                    try {
                        // System.out.println("Current time => " + c.getTime());
                        // c.add(Calendar.DATE, -1);
                        Date current = new Date();
                        DateFormat df23 = new SimpleDateFormat("d-M-yyyy");
                        String temp = df23.format(current);
                        Date current_date = df23.parse(temp);



                        String dd = a.date_1;
                        DateFormat df2 = new SimpleDateFormat("d-M-yyyy");
                        Date startDate = df2.parse(dd);

                        if((startDate.compareTo(current_date))>=0){
                            FeedItem item = new FeedItem();
                            item.setTrip_id(a.trip_id);
                            item.setThumbnail(a.userid);
                            item.setTitle("Date:"+a.date_1+"\n"+a.start+" >> "+a.stop);
                            feedsList.add(item);
                        }

                    }catch(Exception e){}



                    // FeedItem item = new FeedItem();
                    //item.setTitle("Arvind\nSuba");
                    //item.setThumbnail("http://design.ubuntu.com/wp-content/uploads/ubuntu-logo32.png");

                    //feedsList.add(item);

                }
                List<Schedule> al2=  db.findByIndex("{\"selector\":{\"co_traveller_id\":\"" + obj._id + "\"}}", Schedule.class);
                for(Schedule a:al2){
                    System.out.println(a.trip_id);
                    System.out.println(a.co_traveller_id);
                    // FeedItem item = new FeedItem();
                    //item.setTitle("Arvind\nSuba");
                    //item.setThumbnail("http://design.ubuntu.com/wp-content/uploads/ubuntu-logo32.png");
                    try {
                        // System.out.println("Current time => " + c.getTime());
                        // c.add(Calendar.DATE, -1);
                        Date current = new Date();
                        DateFormat df23 = new SimpleDateFormat("d-M-yyyy");
                        String temp = df23.format(current);
                        Date current_date = df23.parse(temp);



                        String dd = a.date_1;
                        DateFormat df2 = new SimpleDateFormat("d-M-yyyy");
                        Date startDate = df2.parse(dd);

                        if((startDate.compareTo(current_date))>=0){
                            FeedItem item = new FeedItem();
                            item.setTrip_id(a.trip_id);
                            item.setThumbnail(a.userid);
                            item.setTitle("Date:"+a.date_1+"\n"+a.start+" >> "+a.stop);
                            feedsList.add(item);
                        }

                    }catch(Exception e){}

                    //feedsList.add(item);

                }
            }catch(Exception e){System.out.println(e.toString());}
            return 1; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            progressBar.setVisibility(View.GONE);

            if (result == 1) {

                adapter = new MyRecyclerAdapter(HomeScreenActivity.this, feedsList,obj);
                mRecyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(HomeScreenActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class AsyncHttpTask2 extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Integer doInBackground(String... params) {

            try {
                feedsList = new ArrayList<>();
                CloudantClient client = ClientBuilder.account("7f1f486a-1104-47bd-add4-62663474ac15-bluemix")
                        .username("7f1f486a-1104-47bd-add4-62663474ac15-bluemix")
                        .password("93b4b2805dcaa6ecbe5277cd7baf4b9bd67d78750f78398a242cf1afabd0486b")
                        .build();

                Database db = client.database("pillion", false);
                obj = db.find(Account.class,obj._id);                //   System.out.println(db.listIndices());
                //    List al = new ArrayList<Schedule>();
                // ArrayList<Schedule> new ArrayList<Schedule>();
                //   String arr[]=;
                // int c=0;
                // Date mTime = new Date();
                // mTime.setDate(mTime.getDay()-1);
                Calendar c = Calendar.getInstance();
                // System.out.println("Current time => " + c.getTime());
                // c.add(Calendar.DATE, -1);

                SimpleDateFormat df = new SimpleDateFormat("d-M-yyyy");
                String formattedDate = df.format(c.getTime());



                //String current_date = new SimpleDateFormat("d-M-yyyy").format(mTime);
                System.out.println(formattedDate);
                List<Schedule> al=  db.findByIndex("{\"selector\":{\"mobile\":\"" + obj._id + "\"}}", Schedule.class);
                for(Schedule a:al){
                    System.out.println(a.trip_id);
                    System.out.println(a.co_traveller_id);
                    // FeedItem item = new FeedItem();
                    //item.setThumbnail("http://design.ubuntu.com/wp-content/uploads/ubuntu-logo32.png");
                    try {
                        // System.out.println("Current time => " + c.getTime());
                        // c.add(Calendar.DATE, -1);
                        Date current = new Date();
                        DateFormat df23 = new SimpleDateFormat("d-M-yyyy");
                        String temp = df23.format(current);
                        Date current_date = df23.parse(temp);



                        String dd = a.date_1;
                        DateFormat df2 = new SimpleDateFormat("d-M-yyyy");
                        Date startDate = df2.parse(dd);

                        if((startDate.compareTo(current_date))<0){
                            FeedItem item = new FeedItem();
                            item.setTrip_id(a.trip_id);
                            item.setThumbnail(a.userid);
                            item.setTitle("Date:"+a.date_1+"\n"+a.start+" >> "+a.stop);
                            feedsList.add(item);
                        }

                    }catch(Exception e){}

                    //feedsList.add(item);

                }
                List<Schedule> al2=  db.findByIndex("{\"selector\":{\"co_traveller_id\":\"" + obj._id + "\"}}", Schedule.class);
                for(Schedule a:al2){
                    System.out.println(a.trip_id);
                    System.out.println(a.co_traveller_id);
                    // FeedItem item = new FeedItem();
                    //item.setTitle("Arvind\nSuba");
                    //item.setThumbnail("http://design.ubuntu.com/wp-content/uploads/ubuntu-logo32.png");
                    try {
                        // System.out.println("Current time => " + c.getTime());
                        // c.add(Calendar.DATE, -1);
                        Date current = new Date();
                        DateFormat df23 = new SimpleDateFormat("d-M-yyyy");
                        String temp = df23.format(current);
                        Date current_date = df23.parse(temp);



                        String dd = a.date_1;
                        DateFormat df2 = new SimpleDateFormat("d-M-yyyy");
                        Date startDate = df2.parse(dd);

                        if((startDate.compareTo(current_date))<0){
                            FeedItem item = new FeedItem();
                            item.setTrip_id(a.trip_id);
                            item.setThumbnail(a.userid);
                            item.setTitle("Date:"+a.date_1+"\n"+a.start+" >> "+a.stop);
                            feedsList.add(item);
                        }

                    }catch(Exception e){}

                    //feedsList.add(item);

                }
            }catch(Exception e){System.out.println(e.toString());}
            return 1; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            progressBar.setVisibility(View.GONE);

            if (result == 1) {

                adapter = new MyRecyclerAdapter(HomeScreenActivity.this, feedsList,obj);
                mRecyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(HomeScreenActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    ProgressDialog pDialog;
    //image

    //image

}


class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        if(parent.getChildPosition(view) == 0)
            outRect.top = space;
    }



}
