package com.example.dayanidhi.pillion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FindARide2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public TextView dateandtime,time1,time2,startheader,textview13,textview12,textview15,costofride,ridercomment,distance,detour,ownerinfo;
    public Button confirm,chat;
    public Schedule sch;
    private ProgressDialog progressDialog;

    public Account acc;
    public String trip_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_aride2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        acc = (Account)this.getIntent().getSerializableExtra("Account");
        sch = (Schedule)this.getIntent().getSerializableExtra("Schedule");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_find_aride2, navigationView, false);
        navigationView.addHeaderView(headerView);
        chat=(Button)findViewById(R.id.chat);

        dateandtime=(TextView)findViewById(R.id.dateandtime);
        time1=(TextView)findViewById(R.id.time1);
        time2=(TextView)findViewById(R.id.time2);
        startheader=(TextView)findViewById(R.id.startheader);
        textview13=(TextView)findViewById(R.id.textView13);
        textview12=(TextView)findViewById(R.id.textView12);
        textview15=(TextView)findViewById(R.id.textView15);
        costofride=(TextView)findViewById(R.id.costofride);
        ridercomment=(TextView)findViewById(R.id.ridercomment);
        distance=(TextView)findViewById(R.id.distance);
        detour=(TextView)findViewById(R.id.detour);
        ownerinfo=(TextView)findViewById(R.id.ownerinfo);
        confirm=(Button)findViewById(R.id.confirm);
        trip_id=this.getIntent().getExtras().getString("trip_id");
        System.out.println(trip_id);

        System.out.println(acc._id);
        new MyTask3().execute();

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FindARide2.this,ChatWithHim.class);
                i.putExtra("usera",acc._id);
                i.putExtra("userb",sch.mobile);
                startActivity(i);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new BookMyRide().execute();
            }
        });

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
     //   getMenuInflater().inflate(R.menu.find_aride2, menu);
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


        if (id == R.id.yourrides) {
            Intent a = new Intent(FindARide2.this,HomeScreenActivity.class);
            a.putExtra("Account",acc);
            startActivity(a);
        } else if (id == R.id.offerride) {
            Intent a = new Intent(FindARide2.this,OfferARide.class);
            a.putExtra("Account",acc);
            startActivity(a);
        } else if (id == R.id.findride) {
            Intent a = new Intent(FindARide2.this,FindARide1.class);
            a.putExtra("Account",acc);
            startActivity(a);
        } else if (id == R.id.Ratings) {

        }else if (id == R.id.wallet) {
            Intent a = new Intent(FindARide2.this,Wallet.class);
            a.putExtra("Account",acc);
            startActivity(a);
        }
         else if (id == R.id.logout) {
            Intent a = new Intent(FindARide2.this,LoginActivity.class);
            a.putExtra("dp","");

            startActivity(a);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






    private class MyTask3 extends AsyncTask<String, Integer, String> {


        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(FindARide2.this, "Message", "Fetching Info...");

        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            // get the string from params, which is an array
           // String ride_id=sch.trip_id;
           // String given_pass =params[1];
            try {
                CloudantClient client = ClientBuilder.account("7f1f486a-1104-47bd-add4-62663474ac15-bluemix")
                        .username("7f1f486a-1104-47bd-add4-62663474ac15-bluemix")
                        .password("93b4b2805dcaa6ecbe5277cd7baf4b9bd67d78750f78398a242cf1afabd0486b")
                        .build();

                Database db = client.database("pillion", false);
                sch = db.find(Schedule.class, trip_id);


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
        dateandtime.setText(sch.date_1+" \t\t"+sch.time_1);
        time1.setText(sch.time_1);
        time2.setText(sch.time_2);
        startheader.setText(sch.start.substring(0,sch.start.indexOf(" ",10)));
        textview13.setText(sch.start);
        textview12.setText(sch.stop.substring(0,sch.stop.indexOf(" ",10)));
        textview15.setText(sch.stop);
        costofride.setText(sch.costofride);
        ridercomment.setText(sch.ride_details);
        distance.setText(sch.calculated_distance);
        detour.setText((sch.detour)?"Detour : Acceptable":"Detour : Not Acceptable Sry !!");
        ownerinfo.setText("Mobile :"+sch.mobile);
            // System.out.println(result);
            progressDialog.dismiss();
            // Do things like hide the progress bar or change a TextView
            if(sch.fixed){
                confirm.setText("Offer Over");
                confirm.setEnabled(false);
                confirm.setClickable(false);
            }
            try {
                Calendar c = Calendar.getInstance();
                // System.out.println("Current time => " + c.getTime());
               // c.add(Calendar.DATE, -1);
                Date current = new Date();
                DateFormat df = new SimpleDateFormat("d-M-yyyy");
                String temp = df.format(current);
                Date current_date = df.parse(temp);



                String dd = sch.date_1;
                DateFormat df2 = new SimpleDateFormat("d-M-yyyy");
                Date startDate = df2.parse(dd);

                if((startDate.compareTo(current_date))<0){
                    confirm.setText("Offer Over");
                    confirm.setEnabled(false);
                    confirm.setClickable(false);
                }

            }catch(Exception e){}
        }
    }














    private class BookMyRide extends AsyncTask<String, Integer, String> {


        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(FindARide2.this, "Message", "Booking...");

        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            // get the string from params, which is an array
            // String ride_id=sch.trip_id;
            // String given_pass =params[1];
            try {
                CloudantClient client = ClientBuilder.account("7f1f486a-1104-47bd-add4-62663474ac15-bluemix")
                        .username("7f1f486a-1104-47bd-add4-62663474ac15-bluemix")
                        .password("93b4b2805dcaa6ecbe5277cd7baf4b9bd67d78750f78398a242cf1afabd0486b")
                        .build();

                Database db = client.database("pillion", false);
                sch = db.find(Schedule.class, trip_id);
                Account biker,pillion;
                biker=db.find(Account.class,sch.mobile);
                pillion = db.find(Account.class,acc._id);
                sch.fixed=true;
                sch.co_traveller_id=acc._id;
                float cost = Float.parseFloat(sch.costofride.substring(3));
                System.out.println("COST OF TRAVEL:"+cost);
                biker.balance+=cost;
                System.out.println("Updated Biker Balance:"+biker.balance);
                pillion.balance-=cost;
                System.out.println("Updated Pillion Balance:"+pillion.balance);
                biker.addAssociatedUsers(pillion._id);
                db.update(sch);
                db.update(biker);
                db.update(pillion);




                try {
                    // Construct data
                    String user = "username=" + "arvind.patricks@gmail.com";
                    String hash = "&hash=" + "68e48827b3baa6569d89fb57d34b598ac95c040f";
                    String message = "&message=" + "Hi "+pillion.fname+" !! You have booked a ride using Pillion. Transaction Amt :"+sch.costofride+" Trip Id:"+sch.trip_id+"\nNew Balance:"+pillion.balance;
                    System.out.println(message);
                    String sender = "&sender=" + "TXTLCL";
                    String numbers = "&numbers=" + pillion._id;
                    // Send data
                    HttpURLConnection conn = (HttpURLConnection) new URL("http://api.textlocal.in/send/?").openConnection();
                    String data = user + hash + numbers + message + sender;
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
                    conn.getOutputStream().write(data.getBytes("UTF-8"));
                    final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    final StringBuffer stringBuffer = new StringBuffer();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                    rd.close();


                } catch (Exception e) {
                    return "Please Deregister From DND !!";
                    //System.out.println("Error SMS "+e);

                }


                try {
                    // Construct data
                    String user = "username=" + "arvind.patricks@gmail.com";
                    String hash = "&hash=" + "68e48827b3baa6569d89fb57d34b598ac95c040f";
                    String message = "&message=" + "Hi "+biker.fname+" !! Your ride with "+pillion.fname+" has been confirmed - Pillion. Transaction Amt :"+sch.costofride+" Trip Id:"+sch.trip_id+"\nNew Balance:"+biker.balance;
                    System.out.println(message);
                    String sender = "&sender=" + "TXTLCL";
                    String numbers = "&numbers=" + biker._id;
                    // Send data
                    HttpURLConnection conn = (HttpURLConnection) new URL("http://api.textlocal.in/send/?").openConnection();
                    String data = user + hash + numbers + message + sender;
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
                    conn.getOutputStream().write(data.getBytes("UTF-8"));
                    final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    final StringBuffer stringBuffer = new StringBuffer();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                    rd.close();


                } catch (Exception e) {
                    return "Please Deregister From DND !!";
                    //System.out.println("Error SMS "+e);

                }


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
            Toast.makeText(getApplicationContext(),"Transaction complete..",Toast.LENGTH_LONG).show();
            Intent homepage = new Intent(FindARide2.this,HomeScreenActivity.class);
            homepage.putExtra("Account",acc);
            homepage.putExtra("mobile",acc._id);
            startActivity(homepage);
            // Do things like hide the progress bar or change a TextView
        }
    }
}
