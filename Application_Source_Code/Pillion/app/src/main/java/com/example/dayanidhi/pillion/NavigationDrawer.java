package com.example.dayanidhi.pillion;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.nineoldandroids.animation.AnimatorInflater;
import com.nineoldandroids.animation.AnimatorSet;


public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
TextView username;
     AnimatorSet setRightOut;
    AnimatorSet setLeftIn;
    public String mobile,name;
    public ProgressDialog progressDialog;
    public Account obj;
    ImageView imgFront;
    ImageView imgBack;
    public void changePic(){
        setRightOut.setTarget(imgFront);
        setLeftIn.setTarget(imgBack);
        setRightOut.start();
        setLeftIn.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        username=(TextView)findViewById(R.id.usernameee);
        imgFront = (ImageView)findViewById(R.id.imgFront);
        imgBack = (ImageView)findViewById(R.id.imgBack);
        mobile=this.getIntent().getExtras().getString("mobile");
        obj = (Account)this.getIntent().getSerializableExtra("Account");
        username.setText(username.getText().toString()+" "+obj.fname.toString()+" !");
        setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),R.animator.flight_right_out);

        setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.flight_left_in);



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
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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

    private class MyTask4 extends AsyncTask<String, Integer, String> {


        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(NavigationDrawer.this, "Message", "Getting Ready For a Ride...");

        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            // get the string from params, which is an array
           String mob = params[0];
            try {
                CloudantClient client = ClientBuilder.account("7f1f486a-1104-47bd-add4-62663474ac15-bluemix")
                        .username("7f1f486a-1104-47bd-add4-62663474ac15-bluemix")
                        .password("93b4b2805dcaa6ecbe5277cd7baf4b9bd67d78750f78398a242cf1afabd0486b")
                        .build();

                Database db = client.database("pillion", false);
              //  Account doc = db.find(Account.class, given_uname);
               // if( doc.password.equals(given_pass)){return "SUCCESS";}
            } catch (Exception e) {
                return e.toString();
                //System.out.println("Error SMS "+e);

            }


            return "FAILED";
        }

        // This is called from background thread but runs in UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //  progressDialog.setProgress(Integer.parseInt("" + values));
            // Do things like update the progress bar
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            // Do things like hide the progress bar or change a TextView
        }
    }
}
