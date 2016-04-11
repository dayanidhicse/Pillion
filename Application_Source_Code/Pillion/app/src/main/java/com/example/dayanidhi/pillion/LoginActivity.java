package com.example.dayanidhi.pillion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.example.dayanidhi.pillion.animations.FlowAnimation;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
EditText et1,et2;
    String uname,pass;
    Button signin,signup;
    private ProgressDialog progressDialog;
    public Account doc;
    TextView titlee;
    String dp,userid;
    private AccessTokenTracker accessTokenTracker;
    public AccessToken accessToken;
    private CallbackManager callbackManager;

    private ProfileTracker profileTracker;
    protected static final String TAG = "main-activity";
    LoginButton loginButton;
    protected static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    protected static final String LOCATION_ADDRESS_KEY = "location-address";

    /**
     * Provides the entry point to Google Play services.
     */
//facebook Login

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            accessToken = loginResult.getAccessToken();
            //System.out.println("DDD"+accessToken);
            final Profile profile = Profile.getCurrentProfile();
          //  System.out.println("LOGIN SUCESS");
            //   System.out.println("LOGIN SUCESS");
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            try {
                                System.out.println(object);
                                System.out.println("REQ TOKEN :"+accessToken);
                                //  System.out.println(object.get("id"));
                                userid = object.getString("id");
                                uname = et1.getText().toString();
                                pass = et2.getText().toString();
                               // loginButton
                             //   loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday,user_friends"));
                                // loginButton.setFragment(this);

                               // loginButton.registerCallback(callbackManager, callback);

                                //
                                new MyTask3().execute(uname, pass);

                            } catch (Exception e) {
                                System.out.println(e.toString());
                            }
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link, birthday,gender,location,email");
            request.setParameters(parameters);
            request.executeAsync();


            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/me/feed",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
            /* handle the result */

                        }
                    }
            ).executeAsync();

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    //facebook login ends


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
//facebook

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
           //     Toast.makeText(getApplicationContext(),oldToken+"",Toast.LENGTH_LONG).show();
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                // displayMessage(newProfile);

             // Toast.makeText(getApplicationContext(),newProfile.getId(),Toast.LENGTH_LONG).show();

            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
//facebook

        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // printKeyHash(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//facebook

        loginButton = (LoginButton)findViewById(R.id.login_button);
        //  textView = (TextView)findViewById(R.id.textView);

        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday,user_friends"));
        // loginButton.setFragment(this);

        loginButton.registerCallback(callbackManager, callback);

        //facebook
        et1 =(EditText)findViewById(R.id.et1);
        et2=(EditText)findViewById(R.id.et2);
        signin = (Button)findViewById(R.id.signin);
        signup =(Button)findViewById(R.id.signup);
        try{
        dp =getIntent().getExtras().getString("dp");}
        catch (Exception e){}
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                i.putExtra("dp", dp);
                startActivity(i);
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname = et1.getText().toString();
                pass = et2.getText().toString();
                new MyTask3().execute(uname, pass);
            }
        });



    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       //  getMenuInflater().inflate(R.menu.login, menu);
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

        if (id == R.id.Login) {
            Toast.makeText(getApplicationContext(),"Please enter login info..",Toast.LENGTH_LONG).show();
        }
        else if (id == R.id.Sigup) {
            Intent i= new Intent(LoginActivity.this,SignUpActivity.class);
            startActivity(i);
        } else if (id == R.id.HowitWorks) {


//Set "hasLoggedIn" to true

            Intent i= new Intent(LoginActivity.this,FlowAnimation.class);
            startActivity(i);
            LoginActivity.this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void printFailure(){
        Toast.makeText(getApplicationContext(),"Login Failed..",Toast.LENGTH_LONG).show();
    }

    private class MyTask3 extends AsyncTask<String, Integer, String> {


        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loginButton.performClick();
            progressDialog = ProgressDialog.show(LoginActivity.this, "Message", "Logging In...");

        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            // get the string from params, which is an array
            String given_uname = params[0];
            String given_pass =params[1];

            try {

                CloudantClient client = ClientBuilder.account("7f1f486a-1104-47bd-add4-62663474ac15-bluemix")
                        .username("7f1f486a-1104-47bd-add4-62663474ac15-bluemix")
                        .password("93b4b2805dcaa6ecbe5277cd7baf4b9bd67d78750f78398a242cf1afabd0486b")
                        .build();

                Database db = client.database("pillion", false);
                Database db2 = client.database("personality", false);
                if(given_uname.equals("")){
                    System.out.println("LOGIN WITH FB EXECUTING..");
                    List<Account> al=  db.findByIndex("{\"selector\":{\"userid\":\"" + userid + "\"}}", Account.class);
                    if(al.isEmpty()){
                        System.out.println("Not Found Data");
                        return "FAILED";}
                    else{doc=al.get(0);
                    return "SUCCESS";
                    }

                }
                else {
                    doc = db.find(Account.class, given_uname);
                    if (doc.password.equals(given_pass)) {
                        //Personality psr = new Personality(doc._id,profile);
                        //db2.save(psr);


                        return "SUCCESS";


                    }
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
            if(result.equals("SUCCESS")){
                System.out.println("SUCCESS");
                //User has successfully logged in, save this information
// We need an Editor object to make preference changes.

                Intent n = new Intent(LoginActivity.this,HomeScreenActivity.class);
                n.putExtra("mobile",uname);
                n.putExtra("Account",doc);
                startActivity(n);
            }
            else{
                printFailure();
            }



            // System.out.println(result);
            progressDialog.dismiss();
            // Do things like hide the progress bar or change a TextView
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
     //   System.out.println("--------===>"+requestCode+"====>"+resultCode);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();
     //   System.out.println("-=-=-=->"+accessToken);
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        //displayMessage(profile);
        // Toast.makeText(getApplicationContext(),profile.getId(),Toast.LENGTH_LONG).show();

    }
/*
    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }
*/

}
