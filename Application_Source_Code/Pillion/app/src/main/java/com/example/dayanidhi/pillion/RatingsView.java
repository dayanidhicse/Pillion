package com.example.dayanidhi.pillion;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.facebook.messenger.MessengerThreadParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RatingsView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 1;

    private static final String TAG = "RecyclerViewExample";
    private List<FeedItem> feedsList;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter2 adapter;
    private ProgressBar progressBar;
    private MessengerThreadParams mThreadParams;
    private boolean mPicking;

    public Account obj;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
   /*     NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view1);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_home_screen, navigationView, false);
        navigationView.addHeaderView(headerView);
        TextView Name = (TextView) headerView.findViewById(R.id.usern);
        //  Name.setText("Myname");*/
        obj = (Account)this.getIntent().getSerializableExtra("Account");
      //  Name.setText(obj.fname);





        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(20));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        // Downloading data from below url
        new AsyncHttpTask().execute();


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.ratings_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       // int id = item.getItemId();

        //noinspection SimplifiableIfStatement
     //   if (id == R.id.action_settings) {
     //       return true;
      //  }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



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

                            FeedItem item = new FeedItem();
                            item.setTrip_id(a.trip_id);
                            item.setThumbnail(a.userid);
                            item.setTitle("Date:"+a.date_1+"\n"+a.start+" >> "+a.stop);
                            feedsList.add(item);


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

                            FeedItem item = new FeedItem();
                            item.setTrip_id(a.trip_id);
                            item.setThumbnail(a.userid);
                            item.setTitle("Date:"+a.date_1+"\n"+a.start+" >> "+a.stop);
                            feedsList.add(item);

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

                adapter = new MyRecyclerAdapter2(RatingsView.this, feedsList,obj);
                mRecyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(RatingsView.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
