package com.example.dayanidhi.pillion;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class OfferARide_FINAL extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public Account obj;
    private ProgressDialog progressDialog;
    static final LatLng TutorialsPoint = new LatLng(21 , 57);
    private GoogleMap googleMap;

    public TextView textView8,textView9;
    public String start,stop,date_1,time_1,costperkm,date_2,time_2,calculated_distance,calculated_time,total_cost,bikemodel,ridedetails;
    public boolean two_way;
    Button confirm;



    ArrayList<LatLng> directionPositionList= new ArrayList<>();
    Polyline line;
    Context context;

    // Static LatLng
    LatLng startLatLng = new LatLng(11.9300, 79.1300);
    LatLng endLatLng = new LatLng(13.0827, 80.2707);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_aride__final);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        confirm =(Button)findViewById(R.id.Confirm);
        context = OfferARide_FINAL.this;


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_offer_aride__final, navigationView, false);
        navigationView.addHeaderView(headerView);
        TextView Name = (TextView) headerView.findViewById(R.id.usern);
        //  Name.setText("Myname");
        textView8=(TextView)findViewById(R.id.textView8);
        textView9=(TextView)findViewById(R.id.textView9);
        start=this.getIntent().getExtras().getString("start");
        stop=this.getIntent().getExtras().getString("stop");
        two_way=this.getIntent().getExtras().getBoolean("two_way");
        date_1=this.getIntent().getExtras().getString("date_1");
        time_1=this.getIntent().getExtras().getString("time_1");
        date_2=this.getIntent().getExtras().getString("date_2");
        time_2=this.getIntent().getExtras().getString("time_2");
        bikemodel=this.getIntent().getExtras().getString("bikemodel");
        ridedetails=this.getIntent().getExtras().getString("ridedetails");
        total_cost=this.getIntent().getExtras().getString("total_cost");
        calculated_distance =this.getIntent().getExtras().getString("calculated_distance");
        calculated_time = this.getIntent().getExtras().getString("calculated_time");

        costperkm=this.getIntent().getExtras().getString("costperkm");
        obj = (Account)this.getIntent().getSerializableExtra("Account");
        Name.setText(obj.fname);


        textView8.setText("Estimated Distance :" + calculated_distance);
        textView9.setText("Estimated Cost Of Travel :" + total_cost);

        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
                 googleMap.setMyLocationEnabled(true);

                googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
          //      MarkerOptions markerOpt1 = new MarkerOptions();
            //    markerOpt1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
             //   markerOpt1.icon(R.drawable.common_plus_signin_btn_icon_dark);
                //markerOpt1.title("Vimy Ridge");
               // markerOpt1.InvokeIcon(BitmapDescriptorFactory.DefaultMarker(BitmapDescriptorFactory.HueCyan));
              //  googleMap.addMarker(markerOpt1);
                       // AddMarker(marker1);

            }
        }catch(Exception e){System.out.println(e.toString());}

            String serverKey = "AIzaSyAp-iEHtTtmIkG-AHtmT0KBDwE0pzi225A";
          double lat_1= Double.parseDouble(this.getIntent().getExtras().getString("lat_1"));
           double long_1 = Double.parseDouble(this.getIntent().getExtras().getString("long_1"));

        double lat_2=Double.parseDouble(this.getIntent().getExtras().getString("lat_2"));
        double long_2 = Double.parseDouble(this.getIntent().getExtras().getString("long_2"));


            final LatLng origin = new LatLng(lat_1, long_1);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin, 12.0f));
        final LatLng destination = new LatLng(lat_2,long_2);
        System.out.println("ORIGIN"+origin);
        System.out.println("DEST"+destination);
            GoogleDirection.withServerKey(serverKey)
                    .from(origin)
                    .to(destination)
                    .execute(new DirectionCallback() {
                        @Override
                        public void onDirectionSuccess(Direction direction) {
                            // Do something here
                            String status = direction.getStatus();
                            if (status.equals(RequestResult.OK)) {
                                // Do something
                            } else if (status.equals(RequestResult.NOT_FOUND)) {
                                // Do something
                            }
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            Info distanceInfo = leg.getDistance();
                            Info durationInfo = leg.getDuration();
                            String distance = distanceInfo.getText();
                            String duration = durationInfo.getText();

                            directionPositionList = leg.getDirectionPoint();
                            //PolylineOptions polylineOptions = DirectionConverter.createPolyline(getApplicationContext(), directionPositionList, 5, Color.RED);
                            //googleMap.addPolyline(polylineOptions);
                            PolygonOptions rectOptions = new PolygonOptions();

                            // GoogleMap map;

                            // ... get a map.
                            // Add a thin red line from London to New York.
                   /*         Polyline line = googleMap.addPolyline(new PolylineOptions()
                                    .add(new LatLng(11.9310, 79.7852), new LatLng(13.0827, 80.2707))
                                    .width(5)
                                    .color(Color.RED));

                            Polyline line2 = googleMap.addPolyline(new PolylineOptions()
                                    .add(new LatLng(13.0827, 80.2707), new LatLng(11.0183, 76.9725))
                                    .width(5)
                                    .color(Color.RED));
*/


                            for (int i = 0; i < directionPositionList.size()-1; i++) {
                                LatLng first = directionPositionList.get(i);
                                LatLng second = directionPositionList.get(i+1);
                                Polyline line1 = googleMap.addPolyline(new PolylineOptions()
                                        .add(first, second)
                                        .width(5)
                                        .color(Color.RED));
                              // System.out.println(first+","+second);
                            }
                            for (LatLng a : directionPositionList) {
                              //  System.out.println(a);
                                //     rectOptions.add(a);
                   /*         Marker marker = googleMap.addMarker(new MarkerOptions()
                                    .position(a)
                                    .title("San Francisco")
                                    .snippet("Population: 776733"));
                                    */
                            }
                            //  System.out.println("Adding polygon...");
                            // Polygon polygon = googleMap.addPolygon(rectOptions);


                            System.out.println("DISTANCE:" + distance);
                            System.out.println("DURATION" + duration);

                        }

                        @Override
                        public void onDirectionFailure(Throwable t) {
                            // Do something here
                        }
                    });


            //   SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            //          .findFragmentById(R.id.map);
            //   mapFragment.getMapAsync(this);





        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyTask1().execute();

            }
        });
        // new MyTask1().execute(start, stop);
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
        //getMenuInflater().inflate(R.menu.offer_aride__final, menu);
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
            Intent a = new Intent(OfferARide_FINAL.this,HomeScreenActivity.class);
            a.putExtra("Account",obj);
            startActivity(a);
        } else if (id == R.id.offerride) {
            Intent a = new Intent(OfferARide_FINAL.this,OfferARide.class);
            a.putExtra("Account",obj);
            startActivity(a);
        } else if (id == R.id.findride) {
            Intent a = new Intent(OfferARide_FINAL.this,FindARide1.class);
            a.putExtra("Account",obj);
            startActivity(a);
        } else if (id == R.id.Ratings) {

        }else if (id == R.id.wallet) {
            Intent a = new Intent(OfferARide_FINAL.this,Wallet.class);
            a.putExtra("Account",obj);
            startActivity(a);
        }
        else if (id == R.id.logout) {
            Intent a = new Intent(OfferARide_FINAL.this,LoginActivity.class);
            a.putExtra("dp","");

            startActivity(a);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class MyTask1 extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(OfferARide_FINAL.this, "Message", "Saving...");

            //  progressDialog.show(SignUpActivity.this, "Message", "Sending OTP..");
            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            // get the string from params, which is an array
            //  String myString = params[0];
            try {
                // Create a new CloudantClient instance for account endpoint example.cloudant.com

                CloudantClient client = ClientBuilder.account("7f1f486a-1104-47bd-add4-62663474ac15-bluemix")
                        .username("7f1f486a-1104-47bd-add4-62663474ac15-bluemix")
                        .password("93b4b2805dcaa6ecbe5277cd7baf4b9bd67d78750f78398a242cf1afabd0486b")
                        .build();

                Database db = client.database("pillion", false);
                DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                Date date = new Date();
                System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
                String trip_id= obj._id+""+dateFormat.format(date)+"";
                db.save(new Schedule(trip_id,obj._id,start,stop,date_1,date_2,time_1,time_2,calculated_distance,calculated_time,two_way,total_cost,bikemodel,ridedetails,obj.userid,directionPositionList));
// A Java type that can be serialized to JSON
                //  db.save(new Account(mobile, name, email, gender, dob, true, state, password));


// Create an Account and save it in the database


            }catch(Exception e){System.out.println(e.toString());
                Toast.makeText(getApplicationContext(), "Try After Sometime !!", Toast.LENGTH_LONG).show();
            }

            return "Done !!";
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

            System.out.println("You have inserted the document");
            Intent nn = new Intent(OfferARide_FINAL.this,HomeScreenActivity.class);
            nn.putExtra("Account",obj);
            Toast.makeText(getApplicationContext(),"Your Ride Has been Addded..",Toast.LENGTH_LONG).show();
            startActivity(nn);
            // Do things like hide the progress bar or change a TextView
        }
    }


}
