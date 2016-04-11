package com.example.dayanidhi.pillion;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

public class OfferARide extends AppCompatActivity
        implements OnItemClickListener, DatePickerDialog.OnDateSetListener,NavigationView.OnNavigationItemSelectedListener,TimePickerDialog.OnTimeSetListener {
    public static final String DATEPICKER_TAG = "datepicker";
    public int current_day,current_month,current_year;
    public static final String TIMEPICKER_TAG = "timepicker";
    double latitude, longitute;
    public Account obj;
    public String calculated_time,calculated_distance;

    Button button2;
    JSONObject obj1,obj2;
    AutoCompleteTextView et1, et2;

    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    public String start,stop,date_1,time_1;
    public boolean two_way;

    private static final String API_KEY = "AIzaSyBrnb90riZtqmMhMsH5dbTAS6XDaMr9BKY";

    TextView date1,date2,time1,time2;
    CheckBox checkbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_aride);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Calendar calendar = Calendar.getInstance();
        et1 = (AutoCompleteTextView) findViewById(R.id.place1);
        et2 = (AutoCompleteTextView) findViewById(R.id.place2);
        start=this.getIntent().getExtras().getString("start");
        stop=this.getIntent().getExtras().getString("stop");
        two_way=this.getIntent().getExtras().getBoolean("two_way");
        date_1=this.getIntent().getExtras().getString("date_1");
        time_1=this.getIntent().getExtras().getString("time_1");


        obj = (Account)this.getIntent().getSerializableExtra("Account");
        final DatePickerDialog datePickerDialog1 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        final DatePickerDialog datePickerDialog2 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);
        date1 = (TextView) findViewById(R.id.date1);
        date2 = (TextView) findViewById(R.id.date2);
        time1 = (TextView) findViewById(R.id.time1);
        time2 = (TextView) findViewById(R.id.time2);
        checkbox=(CheckBox)findViewById(R.id.checkBox);
     //   DatePickerDialog.OnDateSetListener from_dateListener, to_dateListener;
        button2=(Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OfferARide.this,StopOverMap.class);
                String start = et1.getText().toString();
                String stop = et2.getText().toString();
                boolean two_way = checkbox.isChecked();
                String date_1 = date1.getText().toString();
                String time_1 = time1.getText().toString();
                String date_2 = date2.getText().toString();
                String time_2 = time2.getText().toString();
                if(start!="" && stop!="" && date_1!=""&& date_2!="" && time_1 !="" && time_2!= "" )
                {i.putExtra("Account",obj);
                i.putExtra("start",start);
                i.putExtra("stop",stop);
                i.putExtra("two_way",two_way);
                i.putExtra("date_1",date_1);
                i.putExtra("time_1",time_1);
                i.putExtra("date_2",date_2);
                i.putExtra("time_2",time_2);
                i.putExtra("calculated_distance",calculated_distance);
                i.putExtra("calculated_time",calculated_time);

                startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please fill all details..",Toast.LENGTH_LONG).show();
                }

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
       NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_home_screen, navigationView, false);
        navigationView.addHeaderView(headerView);
       TextView Name = (TextView) headerView.findViewById(R.id.usern);
        Name.setText(obj.fname);
        TextView Mail = (TextView) headerView.findViewById(R.id.mailid);
        //  Name.setText("Myname");
       ImageView profile=(ImageView) headerView.findViewById(R.id.dp);
        Mail.setText(obj.email);


        String url="http://graph.facebook.com/"+obj.userid+"/picture?type=large";
        //  new LoadImage().execute(url);

        Picasso.with(getApplicationContext()).load(url).into(profile);

        AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.place1);
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        autoCompView.setOnItemClickListener(this);

        AutoCompleteTextView autoCompView2 = (AutoCompleteTextView) findViewById(R.id.place2);
        autoCompView2.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        autoCompView2.setOnItemClickListener(this);


        date1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               start = et1.getText().toString();
               stop = et2.getText().toString();
                new MyTask1().execute(start,stop);

                datePickerDialog1.setYearRange(2016, 2018);
                datePickerDialog1.setYearRange(2016, 2018);
                datePickerDialog1.setYearRange(2016, 2018);
                datePickerDialog1.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show(getSupportFragmentManager(),TIMEPICKER_TAG);
            }
        });
        date2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                datePickerDialog2.setYearRange(2016, 2018);
                datePickerDialog2.setYearRange(2016, 2018);
                datePickerDialog2.setYearRange(2016, 2018);
                datePickerDialog2.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show(getSupportFragmentManager(),TIMEPICKER_TAG);
            }
        });
    }
    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

    }


    public static ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:in");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
              //  System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }



    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index).toString();
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }


    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i1) {
        time1.setText(i+"-"+i1+" Hrs");
        String dur = calculated_time;
        if(dur.contains("day")){
            Toast.makeText(getApplicationContext(),"Please select a travel within you state..",Toast.LENGTH_SHORT).show();
        }
        else if(dur.contains("hours")){
            dur = dur.substring(0,dur.indexOf(" "));
            int total_hrs = i+Integer.parseInt(dur);
            String m = calculated_time;
            int first = m.indexOf(" ");
            int second=m.indexOf(" ", first + 1);
            m=m.substring(second+1);
            m=m.substring(0,m.indexOf(" "));
            System.out.println("Mins total:"+m);
            System.out.println("Time to be kept:"+total_hrs);
            int total_mins =i1+Integer.parseInt(m);
            if(total_mins>60){
                int h =total_mins/60;
                total_hrs+=h;
                total_mins%=60;
            }
            if(total_hrs>24){
                date2.setText((current_day+1) + "-" + (current_month + 1) + "-" + current_year);
                total_hrs-=24;
                time2.setText(total_hrs+"-"+total_mins+" Hrs");

            }
            else {
                time2.setText(total_hrs + "-" + total_mins + " Hrs");
            }

        }
        else{
            dur = dur.substring(0,dur.indexOf(" "));

           // String m = calculated_time;
           // int first = m.indexOf(" ");
            //int second=m.indexOf(" ", first + 1);
            //m=m.substring(second+1);
            //m=m.substring(0,m.indexOf(" "));
          //  System.out.println("Mins total:"+m);
            //System.out.println("Time to be kept:"+total_hrs);
            int total_hrs=i;
            int total_mins =i1+Integer.parseInt(dur);
            if(total_mins>60){
                int h =total_mins/60;
                total_hrs+=h;
                total_mins%=60;
            }
            if(total_hrs>24){
                date2.setText((current_day+1) + "-" + (current_month + 1) + "-" + current_year);
                total_hrs-=24;
                time2.setText(total_hrs+"-"+total_mins+" Hrs");

            }
            else {
                time2.setText(total_hrs + "-" + total_mins + " Hrs");
            }


        }

    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        date1.setText(day + "-" + (month + 1) + "-" + year);
        current_day=day;
        current_month=month;
        current_year=year;
        date2.setText(day + "-" + (month + 1) + "-" + year);
        //  Toast.makeText(Register.this, "new date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();
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
       // getMenuInflater().inflate(R.menu.offer_aride, menu);
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
            Intent a = new Intent(OfferARide.this,HomeScreenActivity.class);
            a.putExtra("Account",obj);
            startActivity(a);
        } else if (id == R.id.offerride) {
            Intent a = new Intent(OfferARide.this,OfferARide.class);
            a.putExtra("Account",obj);
            startActivity(a);
        } else if (id == R.id.findride) {
            Intent a = new Intent(OfferARide.this,FindARide1.class);
            a.putExtra("Account",obj);
            startActivity(a);
        } else if (id == R.id.Ratings) {

        }else if (id == R.id.wallet) {
            Intent a = new Intent(OfferARide.this,Wallet.class);
            a.putExtra("Account",obj);
            startActivity(a);
        }
        else if (id == R.id.logout) {
            Intent a = new Intent(OfferARide.this,LoginActivity.class);
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
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            // get the string from params, which is an array
            String distance;
            String time;
            String loc1 = params[0];
            String loc2 =params[1];
            StringBuilder stringBuilder = new StringBuilder();
            try {

                loc1 = loc1.replaceAll(" ", "%20");
                loc2 =loc2.replaceAll(" ","%20");
                HttpPost httppost = new HttpPost("http://maps.googleapis.com/maps/api/distancematrix/json?origins="+loc1+"&destinations="+loc2+"&mode=driving");
                HttpClient client = new DefaultHttpClient();
                HttpResponse response;
                stringBuilder = new StringBuilder();


                response = client.execute(httppost);
                HttpEntity entity = response.getEntity();
                InputStream stream = entity.getContent();
                int b;
                while ((b = stream.read()) != -1) {
                    stringBuilder.append((char) b);
                }
            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }

            JSONObject jsonObject1 = new JSONObject();
            try {
                jsonObject1 = new JSONObject(stringBuilder.toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

           try {
               JSONArray array = (JSONArray)jsonObject1.getJSONArray("rows");
               JSONObject elements= (JSONObject)array.getJSONObject(0);
               JSONArray ele =(JSONArray)elements.getJSONArray("elements");
               JSONObject d =(JSONObject)ele.getJSONObject(0);
               JSONObject dis =(JSONObject)d.get("distance");
               JSONObject dur =(JSONObject)d.get("duration");
               calculated_time=dur.get("text").toString();
               calculated_distance = dis.get("text").toString();
               System.out.println(dur.get("text"));
               System.out.println(dis.get("text"));

           }catch (Exception e){System.out.println(e.toString());}

            return distance="";

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

           // et3.setText(et3.getText()+" "+ result);
        }





    }


}
