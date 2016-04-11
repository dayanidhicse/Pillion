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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Wallet extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public Account obj;
    TextView balance,date;
    EditText recharge;
    Button add;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd   HH:mm:ss");
        Date date1 = new Date();

        date = (TextView)findViewById(R.id.date);
        date.setText("" + dateFormat.format(date1));
        new FetchBalance().execute();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_wallet, navigationView, false);
        navigationView.addHeaderView(headerView);
        TextView Name = (TextView) headerView.findViewById(R.id.usern);

        obj = (Account)this.getIntent().getSerializableExtra("Account");
        Name.setText(obj.fname);
        TextView Mail = (TextView) headerView.findViewById(R.id.mailid);
        //  Name.setText("Myname");
        ImageView profile=(ImageView) headerView.findViewById(R.id.dp);
        Mail.setText(obj.email);


        String url="http://graph.facebook.com/"+obj.userid+"/picture?type=large";
        //  new LoadImage().execute(url);

        Picasso.with(getApplicationContext()).load(url).into(profile);

        balance=(TextView)findViewById(R.id.balance);
        recharge=(EditText)findViewById(R.id.recharge);
        add=(Button)findViewById(R.id.add);
        balance.setText("Rs."+obj.balance);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(recharge.getText().toString().equals("ARVIND1000")){
                        obj.balance+=1000;
                        new AddMoney().execute();
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
        getMenuInflater().inflate(R.menu.wallet, menu);
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
            Intent a = new Intent(Wallet.this,HomeScreenActivity.class);
            a.putExtra("Account",obj);
            startActivity(a);
        } else if (id == R.id.offerride) {
            Intent a = new Intent(Wallet.this,OfferARide.class);
            a.putExtra("Account",obj);
            startActivity(a);
        } else if (id == R.id.findride) {
            Intent a = new Intent(Wallet.this,FindARide1.class);
            a.putExtra("Account",obj);
            startActivity(a);
        } else if (id == R.id.Ratings) {

        }else if (id == R.id.wallet) {
            Intent a = new Intent(Wallet.this,Wallet.class);
            a.putExtra("Account",obj);
            startActivity(a);
        }
        else if (id == R.id.logout) {
            Intent a = new Intent(Wallet.this,LoginActivity.class);
            a.putExtra("dp","");

            startActivity(a);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public class FetchBalance extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(Wallet.this);
            progressDialog.setTitle("Message");
            progressDialog.setMessage("Fetching Balance...");
            progressDialog.show();

        }

        @Override
        protected Integer doInBackground(String... params) {

            try {

                CloudantClient client = ClientBuilder.account("7f1f486a-1104-47bd-add4-62663474ac15-bluemix")
                        .username("7f1f486a-1104-47bd-add4-62663474ac15-bluemix")
                        .password("93b4b2805dcaa6ecbe5277cd7baf4b9bd67d78750f78398a242cf1afabd0486b")
                        .build();

                Database db = client.database("pillion", false);
               obj= db.find(Account.class,obj._id);

            }catch(Exception e){System.out.println(e.toString());}
            return 1; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            NumberFormat formatter = NumberFormat.getNumberInstance();
            formatter.setMinimumFractionDigits(2);

            formatter.setMaximumFractionDigits(2);
            String output = formatter.format(obj.balance);

            balance.setText("Rs." + output);
           // Toast.makeText(getApplicationContext(),"Recharge Successfull..",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();


        }
    }

    public class AddMoney extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(Wallet.this);
            progressDialog.setTitle("Message");
            progressDialog.setMessage("Adding Money...");
            progressDialog.show();

        }

        @Override
        protected Integer doInBackground(String... params) {

            try {

                CloudantClient client = ClientBuilder.account("7f1f486a-1104-47bd-add4-62663474ac15-bluemix")
                        .username("7f1f486a-1104-47bd-add4-62663474ac15-bluemix")
                        .password("93b4b2805dcaa6ecbe5277cd7baf4b9bd67d78750f78398a242cf1afabd0486b")
                        .build();

                Database db = client.database("pillion", false);
                db.update(obj);

            }catch(Exception e){System.out.println(e.toString());}
            return 1; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            balance.setText("Rs."+obj.balance);
            Toast.makeText(getApplicationContext(),"Recharge Successfull..",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();


        }
    }

}
