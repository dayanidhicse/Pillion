package com.example.dayanidhi.pillion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.language_translation.v2.LanguageTranslation;
import com.ibm.watson.developer_cloud.language_translation.v2.model.TranslationResult;

import java.util.List;

//import com.ibm.watson.developer_cloud.language_translation.v2.LanguageTranslation;
//import com.ibm.watson.developer_cloud.language_translation.v2.model.TranslationResult;

public class StopOverMap extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SeekBar.OnSeekBarChangeListener {
   public Account obj;
    Button proceed;
    EditText textview6,edittext5;
    SeekBar sb;
    TextView et4;
    TextView editText,editText2;
    private ProgressDialog progressDialog;
String fren;
    Button french;
    double latitude1,longitude1,latitude2,longitude2;
  public String start="",stop="",date_1,time_1,date_2,time_2,calculated_distance,calculated_time;
    public boolean two_way;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_over_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        proceed=(Button)findViewById(R.id.button3);
        sb=(SeekBar)findViewById(R.id.seekBar);
        sb.setOnSeekBarChangeListener(this); // set seekbar listener.
        et4=(TextView)findViewById(R.id.editText4);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        textview6=(EditText)findViewById(R.id.textView6);
        edittext5=(EditText)findViewById(R.id.editText5);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_home_screen, navigationView, false);
        navigationView.addHeaderView(headerView);
        TextView Name = (TextView) headerView.findViewById(R.id.usern);

        french=(Button)findViewById(R.id.french);

        start=this.getIntent().getExtras().getString("start");
        stop=this.getIntent().getExtras().getString("stop");
        final double[] starta= getLocationFromAddress(start);
        final double[] stopa =getLocationFromAddress(stop);
        System.out.println("PRINTING LOCATION\n\n");
        for(int i=0;i<2;i++){
            System.out.println("LOC 1:"+starta[i]);
            System.out.println("LOC 2:"+stopa[i]);
        }
        two_way=this.getIntent().getExtras().getBoolean("two_way");
        date_1=this.getIntent().getExtras().getString("date_1");
        time_1=this.getIntent().getExtras().getString("time_1");
        date_2=this.getIntent().getExtras().getString("date_2");
        time_2=this.getIntent().getExtras().getString ("time_2");
        calculated_distance =this.getIntent().getExtras().getString("calculated_distance");
        calculated_time=this.getIntent().getExtras().getString("calculated_time");
        editText=(TextView)findViewById(R.id.editText);
        editText2=(TextView)findViewById(R.id.editText2);
        editText.setText(start);
        editText2.setText(stop);
        obj = (Account)this.getIntent().getSerializableExtra("Account");
        Name.setText(obj.fname);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StopOverMap.this, OfferARide_FINAL.class);


                i.putExtra("Account", obj);
                i.putExtra("start", start);
                i.putExtra("stop", stop);
                i.putExtra("two_way", two_way);
                i.putExtra("date_1", date_1);
                i.putExtra("time_1", time_1);
                i.putExtra("date_2", date_2);
                i.putExtra("time_2", time_2);
                i.putExtra("lat_1", "" + starta[0]);
                i.putExtra("long_1", "" + starta[1]);
                i.putExtra("lat_2", "" + stopa[0]);
                i.putExtra("long_2", "" + stopa[1]);
                i.putExtra("bikemodel", textview6.getText().toString());
                i.putExtra("ridedetails", edittext5.getText().toString());
                i.putExtra("calculated_distance", calculated_distance);
                i.putExtra("calculated_time", calculated_time);
                String costperkm = et4.getText().toString();
                costperkm = costperkm.substring(costperkm.indexOf(":") + 1);
                i.putExtra("costperkm", costperkm);
                String dis = calculated_distance;
                dis = dis.substring(0, dis.indexOf("k") - 1);
                float total_cost = Float.parseFloat(dis) * Float.parseFloat(costperkm);
                i.putExtra("total_cost", "Rs. " + total_cost);
                startActivity(i);
            }
        });

        french.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=edittext5.getText().toString();
                new TranslateToFrench().execute(text);
            }
        });

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        // TODO Auto-generated method stub

        // change progress text label with current seekbar value
       et4.setText("Rs :"+progress);
        // change action text label to changing
     //  et4.setText("changing");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    //   et4.setText("starting to track touch");

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
        seekBar.setSecondaryProgress(seekBar.getProgress());
      //  et4.setText("ended tracking touch");
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
        getMenuInflater().inflate(R.menu.stop_over_map, menu);
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
            Intent a = new Intent(StopOverMap.this,HomeScreenActivity.class);
            a.putExtra("Account",obj);
            startActivity(a);
        } else if (id == R.id.offerride) {
            Intent a = new Intent(StopOverMap.this,OfferARide.class);
            a.putExtra("Account",obj);
            startActivity(a);
        } else if (id == R.id.findride) {
            Intent a = new Intent(StopOverMap.this,FindARide1.class);
            a.putExtra("Account",obj);
            startActivity(a);
        } else if (id == R.id.Ratings) {

        }else if (id == R.id.wallet) {
            Intent a = new Intent(StopOverMap.this,Wallet.class);
            a.putExtra("Account",obj);
            startActivity(a);
        }
        else if (id == R.id.logout) {
            Intent a = new Intent(StopOverMap.this,LoginActivity.class);
            a.putExtra("dp","");

            startActivity(a);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public double[] getLocationFromAddress(String strAddress){

        double[] lat_long=new double[2];
        Geocoder coder = new Geocoder(this);
        List<Address> address;
     //   GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            lat_long[0]=location.getLatitude();
            lat_long[1]=location.getLongitude();
            //return p1;
        }catch (Exception e){}
        return lat_long;
    }



    private class TranslateToFrench extends AsyncTask<String, Integer, String> {


        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(StopOverMap.this, "Message", "Translating...");

        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            // get the string from params, which is an array
            String text = params[0];

            try {
                LanguageTranslation service = new LanguageTranslation();
                service.setUsernameAndPassword("83755299-7f50-4073-ada6-2fdc8abb8acc", "oFpQffwsPhCv");

                TranslationResult translationResult = service.translate(text, "en", "fr");
             fren= translationResult.getTranslations().get(0).getTranslation();
                System.out.println(translationResult);


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
            edittext5.setText(fren);
            progressDialog.dismiss();
            // Do things like hide the progress bar or change a TextView
        }
    }
}
