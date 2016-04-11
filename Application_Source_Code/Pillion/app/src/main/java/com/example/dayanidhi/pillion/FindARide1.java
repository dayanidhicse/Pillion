package com.example.dayanidhi.pillion;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.ibm.watson.developer_cloud.personality_insights.v2.PersonalityInsights;
import com.ibm.watson.developer_cloud.personality_insights.v2.model.Trait;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class FindARide1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemClickListener {

    //




        protected static final String TAG1 = "main-activity";

        protected static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
        protected static final String LOCATION_ADDRESS_KEY = "location-address";

        /**
         * Provides the entry point to Google Play services.
         */
        protected GoogleApiClient mGoogleApiClient;

        /**
         * Represents a geographical location.
         */
        protected Location mLastLocation;

        /**
         * Tracks whether the user has requested an address. Becomes true when the user requests an
         * address and false when the address (or an error message) is delivered.
         * The user requests an address by pressing the Fetch Address button. This may happen
         * before GoogleApiClient connects. This activity uses this boolean to keep track of the
         * user's intent. If the value is true, the activity tries to fetch the address as soon as
         * GoogleApiClient connects.
         */
        protected boolean mAddressRequested;

        /**
         * The formatted location address.
         */
        protected String mAddressOutput;

        /**
         * Receiver registered with this activity to get the response from FetchAddressIntentService.
         */
       // private AddressResultReceiver mResultReceiver;

        /**
         * Displays the location address.
         */
        protected TextView mLocationAddressTextView;

        /**
         * Visible while the address is being fetched.
         */
        ProgressBar mProgressBar;

        /**
         * Kicks off the request to fetch an address when pressed.
         */
        Button mFetchAddressButton;
    //
AutoCompleteTextView editText,editText2;
    String fromPlace,toPlace;
    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    public Account obj;
    ArrayList<ArrayList<LatLng>> groupret;
    ArrayList<Integer> comparingList = new ArrayList<>();
    int  cout1=0;
    private static final String API_KEY = "AIzaSyBrnb90riZtqmMhMsH5dbTAS6XDaMr9BKY";

    private static final String TAG = "RecyclerViewExample";
    private List<FeedItem> feedsList;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter adapter;
    private ProgressBar progressBar;
    ArrayList<ArrayList<LatLng>> listOfRides;
    List<Schedule> sss ;
    ArrayList<LatLng> myroute;
    List<Schedule> al;
    Leg leg;
    ImageView location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_aride1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        obj = (Account)this.getIntent().getSerializableExtra("Account");

       location =(ImageView) findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start thi code to exicure for location
                System.out.println("--------->1");
              //  fetchAddressButtonHandler(v);

                //end od location
            }
        });
        listOfRides =   new  ArrayList<ArrayList<LatLng>>();

        myroute = new ArrayList<LatLng>();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_find_aride1, navigationView, false);
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

        editText = (AutoCompleteTextView)findViewById(R.id.editText);
        editText2 =(AutoCompleteTextView)findViewById(R.id.editText2);
        editText.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
       // editText.setOnItemClickListener(this);
        editText2.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
     //   editText2.setOnItemClickListener(this);
        editText2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toPlace=editText2.getText().toString();
                fromPlace=editText.getText().toString();
                String serverKey = "AIzaSyAp-iEHtTtmIkG-AHtmT0KBDwE0pzi225A";

                GoogleDirection.withServerKey(serverKey)
                        .from(getLocationFromAddress(fromPlace))
                        .to(getLocationFromAddress(toPlace))
                        .execute(new DirectionCallback() {
                            @Override
                            public void onDirectionSuccess(Direction direction) {
                                // Do something here
                                String status = direction.getStatus();
                                if (status.equals(RequestResult.OK)) {
                                    // Do something
                                    System.out.println("MGOT DIRECTION");

                                } else if (status.equals(RequestResult.NOT_FOUND)) {
                                    // Do something
                                    System.out.println("MDIRECTION NOT FOUND");

                                }
                                Route route = direction.getRouteList().get(0);

                                Leg leg = route.getLegList().get(0);

                                //  ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                                myroute = leg.getDirectionPoint();
                                System.out.println("Route assigned..");
                                System.out.println("DIRECTION LIST SIZE" + leg.getDirectionPoint().size());

                                // listOfRides.add(leg.getDirectionPoint());
                            }

                            @Override
                            public void onDirectionFailure(Throwable t) {
                                // Do something here
                            }
                        });

                new AsyncHttpTask().execute();

            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(20));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);




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
      //  getMenuInflater().inflate(R.menu.find_aride1, menu);
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
            Intent a = new Intent(FindARide1.this,HomeScreenActivity.class);
            a.putExtra("Account",obj);
            startActivity(a);
        } else if (id == R.id.offerride) {
            Intent a = new Intent(FindARide1.this,OfferARide.class);
            a.putExtra("Account",obj);
            startActivity(a);
        } else if (id == R.id.findride) {
            Intent a = new Intent(FindARide1.this,FindARide1.class);
            a.putExtra("Account",obj);
            startActivity(a);
        } else if (id == R.id.Ratings) {

        }else if (id == R.id.wallet) {
            Intent a = new Intent(FindARide1.this,Wallet.class);
            a.putExtra("Account",obj);
            startActivity(a);
        }
         else if (id == R.id.logout) {
            Intent a = new Intent(FindARide1.this,LoginActivity.class);
            a.putExtra("dp","");

            startActivity(a);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
       // Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
       FeedItem sd = feedsList.get(position);
        System.out.println("FEEDITEM LOCATION:"+position);
        System.out.println(sd.getTrip_id());
       // Toast.makeText(this,feedsList.get(position).getTrip_id().toString(),Toast.LENGTH_LONG).show();
        System.out.println(feedsList.get(position).trip_id);
        System.out.println(feedsList.get(position).getTrip_id());
      //  Toast.makeText(this, "Fetching...", Toast.LENGTH_SHORT).show();

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
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
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
                al=  db.findByIndex("{\"selector\":{\"fixed\":false}}", Schedule.class);

                /*personality
                PersonalityInsights service = new PersonalityInsights();
                service.setUsernameAndPassword("3b005534-2ff0-45bb-a6ea-eca35ae771e8", "Vk75myzsRvcw");



                //Read text from file
                StringBuilder text = new StringBuilder();

                try {
                    File traceFile = new File(getExternalFilesDir(null), "TraceFile.txt");
                    if (!traceFile.exists())
                        traceFile.createNewFile();

                    BufferedReader br = new BufferedReader(new FileReader(traceFile));
                    String line;

                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }
                    br.close();
                }
                catch (IOException e) {
                    //You'll need to add proper error handling here
                }
                com.ibm.watson.developer_cloud.personality_insights.v2.model.Profile profile = service.getProfile(text.toString());
                //System.out.println(profile);

                //System.out.println(profile.getTree().getChildren());

                Trait t1=  profile.getTree().getChildren().get(0).getChildren().get(0).getChildren().get(0);
                Trait t2=  profile.getTree().getChildren().get(0).getChildren().get(0).getChildren().get(0);
                Trait t3=  profile.getTree().getChildren().get(0).getChildren().get(0).getChildren().get(0);
                Trait t4=  profile.getTree().getChildren().get(0).getChildren().get(0).getChildren().get(0);
                Trait t5=  profile.getTree().getChildren().get(0).getChildren().get(0).getChildren().get(0);
                List l1 = t1.getChildren();
                System.out.println(l1.get(0)); //Adventurousness
                System.out.println(l1.get(1));//Artistic Interests
                System.out.println("\n\n");
                System.out.println(l1.get(2)); //Emotionality    //   System.out.println("\n\n");

                System.out.println(l1.get(3));   //Imagination         System.out.println("\n\n");

                System.out.println(l1.get(4));    //Intellect        System.out.println("\n\n");

                System.out.println(l1.get(5));

                //personality*/
                for(Schedule a:al){
                    System.out.println(a.trip_id);
                    listOfRides.add(a.directionPositionList);
                   // String serverKey = "AIzaSyAp-iEHtTtmIkG-AHtmT0KBDwE0pzi225A";

                        }


            }catch(Exception e){System.out.println(e.toString());}
            return 1; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            System.out.println("SIZE OF MYROUTE : " + myroute.size());
            System.out.println("SIZE OF LISTOFRIDES : " + listOfRides.size());
            System.out.println("SIZE OF SCHEDULE CLASS : " + al.size());

            sss = findNearbyRoute(myroute,listOfRides,al);

            for(Schedule a : sss) {
                FeedItem item = new FeedItem();
                item.setTrip_id(a.trip_id);
                System.out.println("Setting trip ID:" + a.trip_id);
                item.setTitle("Date:" + a.date_1 + "\n" + a.start + " >> " + a.stop);
                System.out.println("THUMBAI" + a.userid);
                item.setThumbnail(a.userid);
                feedsList.add(item);
                //feedsList.add(item);
            }
            // Download complete. Let us update UI
            progressBar.setVisibility(View.GONE);

            if (result == 1) {

                adapter = new MyRecyclerAdapter(FindARide1.this, feedsList,obj);
                mRecyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(FindARide1.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

public float getPercentage(ArrayList<LatLng> myroute, ArrayList<LatLng> otherroute){
    float per=0.0f;
    int count=0;
    for(LatLng obj : myroute){
        if(otherroute.contains(obj)){
            count++;
        }
    }
    per = (count/myroute.size())*100;
    return per;
}


    private List<Schedule> findNearbyRoute(ArrayList<LatLng> list11, ArrayList<ArrayList<LatLng>> group1,List <Schedule> sh){

        ArrayList<Float> distances = new ArrayList<>();
        for(ArrayList<LatLng> PATHA : group1){
            distances.add(getPercentage(list11,PATHA));
        }

        for(int i=0;i<group1.size();i++){
            for(int j=i+1;j<group1.size();j++){
                if(distances.get(i)<distances.get(j)){
                    float distance = 0.0f;
                    distances.set(i,distances.get(i)+distances.get(j));
                    distances.set(j,distances.get(i)-distances.get(j));
                    distances.set(i,distances.get(i)-distances.get(j));


                    Schedule temp = sh.get(i);
                    sh.set(i,sh.get(j));
                    sh.set(j,temp);

                    ArrayList<LatLng> obj = group1.get(i);
                    group1.set(i,group1.get(j));
                    group1.set(j,obj);

                }
            }
        }
        for(int i=0;i<sh.size();i++){
            if(distances.get(i)<20.00f){
                sh.remove(i);
            }
        }

        return sh;
    }



    LatLng latLng;

    public LatLng getLocationFromAddress(String strAddress){
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        //   GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);

            latLng=new LatLng(location.getLatitude(),location.getLongitude()) ;

            //return p1;
        }catch (Exception e){}
        return latLng;
    }


}
