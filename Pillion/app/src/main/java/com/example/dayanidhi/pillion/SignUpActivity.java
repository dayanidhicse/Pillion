package com.example.dayanidhi.pillion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.dayanidhi.pillion.animations.FlowAnimation;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.ibm.watson.developer_cloud.visual_recognition.v1.model.Label;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;


public class SignUpActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ProgressDialog progressDialog;
    Button reg;
    EditText mobile,pass;
    String otp,mob,password;
    String dp,userid,name,gender,dob,email,state;
    private AccessTokenTracker accessTokenTracker;
    public AccessToken accessToken;
    private CallbackManager callbackManager;
    int chc=0;
    private ProfileTracker profileTracker;
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            accessToken = loginResult.getAccessToken();
            final Profile profile = Profile.getCurrentProfile();
            System.out.println("LOGIN SUCESS");
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
                                //  System.out.println(object.get("id"));
                                userid = object.getString("id");
                                // System.out.println(object.get("name"));
                                gender = object.getString("gender");

                                dob = object.getString("birthday");
                                name = object.getString("name");
                                email = object.getString("email");
                                chc=1;
                                //System.out.println(profile.get)
                            } catch (Exception e) {
                                System.out.println(e.toString());
                            }
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link, birthday,gender,location,email");

            request.setParameters(parameters);
            request.executeAsync();

           // System.out.println(profile.getProfilePictureUri(150,150));


          //  displayMessage(profile);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
               // displayMessage(newProfile);

                //    Toast.makeText(getApplicationContext(),newProfile.getId(),Toast.LENGTH_LONG).show();

            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();



        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar21);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
      //  textView = (TextView)findViewById(R.id.textView);

        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday,user_friends"));
        // loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);


        // progressDialog = new ProgressDialog(this);
        reg = (Button)findViewById(R.id.next);
        mobile =(EditText)findViewById(R.id.mobile);
        pass = (EditText)findViewById(R.id.pass);
        //conpass = (EditText)findViewById(R.id.conpass);
       // dp=getIntent().getExtras().getString("dp");
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mobile.getText().toString().length()<12){
                    Toast.makeText(getApplicationContext(),"Please Add 91 before the mobile number",Toast.LENGTH_SHORT).show();
                }
                else if(mobile.getText().toString().length()>12){
                    Toast.makeText(getApplicationContext(),"Please check the mobile number",Toast.LENGTH_SHORT).show();

                }
                else if(pass.getText().toString().length()<6){
                    Toast.makeText(getApplicationContext(),"Password should be of minimum 6 characters..",Toast.LENGTH_SHORT).show();

                }


                else{
                    //if password matches, send SMS
                   mob=mobile.getText().toString();
                    password=pass.getText().toString();
                    new MyTask().execute(mobile.getText().toString());

                }
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
        getMenuInflater().inflate(R.menu.login, menu);
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
            Intent i= new Intent(SignUpActivity.this,LoginActivity.class);
            startActivity(i);
        } else if (id == R.id.Sigup) {
            Intent i= new Intent(SignUpActivity.this,SignUpActivity.class);
            startActivity(i);
        } else if (id == R.id.HowitWorks) {
            Intent i= new Intent(SignUpActivity.this,FlowAnimation.class);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void receivedSms(String text){
            mobile.setText(text);

    }


    private class MyTask extends AsyncTask<String, Integer, String> {

        public  String random(int size) {

            StringBuilder generatedToken = new StringBuilder();
            try {
                SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
                // Generate 20 integers 0..20
                for (int i = 0; i < size; i++) {
                    generatedToken.append(number.nextInt(9));
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            return generatedToken.toString();
        }
        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SignUpActivity.this, "Message", "Sending OTP...");

          //  progressDialog.show(SignUpActivity.this, "Message", "Sending OTP..");
            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            // get the string from params, which is an array
            String myString = params[0];

            try {
                // Construct data
                String user = "username=" + "arvind.patricks@gmail.com";
                String hash = "&hash=" + "68e48827b3baa6569d89fb57d34b598ac95c040f";
             //   Random random = new Random();
              //  int randomInteger = random.nextInt(999999);
            //    System.out.println("OTP SENT IS "+randomInteger);
            //    Log.w("Random number",""+randomInteger);
            //    String mm= ":"+randomInteger;
                otp=random(6);
                String message = "&message=" + "Welcome To Pillion Android App - Your OTP is :"+otp;
                System.out.println(message);
                String sender = "&sender=" + "TXTLCL";
                String numbers = "&numbers=" + myString;
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


            return "OTP Sent !!";
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
           // Toast.makeText(getApplicationContext(), "Edklskldsklsd",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        if(chc==1)
        { Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
            Intent next = new Intent(SignUpActivity.this,OTP.class);
            System.out.println(otp+mob+password+userid+name+dob+gender+email);
            next.putExtra("otp",otp);
            next.putExtra("mobile",mob);
            next.putExtra("password",password);
            //next.putExtra("dp",dp);
            next.putExtra("userid",userid);
            next.putExtra("name",name);
            next.putExtra("dob",dob);
            next.putExtra("gender",gender);
            next.putExtra("email",email);
            startActivity(next);}
        else
        {
            Toast.makeText(getApplicationContext(),"Login with facebook...",Toast.LENGTH_SHORT).show();
           // Intent next = new Intent(SignUpActivity.this,SignUpActivity.class);
        }

                // Do things like hide the progress bar or change a TextView


        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }




    @Override
    public void onStop() {
        super.onStop();
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




}


