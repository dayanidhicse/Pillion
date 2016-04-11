package com.example.dayanidhi.pillion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;


public class MainActivity extends Activity{
FloatingActionButton login;
    EditText username,password;
    public String uname,pass;
    private ProgressDialog progressDialog;
    public  Account doc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.pass);
        login = (FloatingActionButton)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname=username.getText().toString();
                pass=password.getText().toString();
                new MyTask3().execute(uname,pass);
            }
        });

    }

    public void printFailure(){
        Toast.makeText(getApplicationContext(),"Login Failed..",Toast.LENGTH_LONG).show();
    }


    private class MyTask3 extends AsyncTask<String, Integer, String> {


        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "Message", "Logging In...");

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
                        doc = db.find(Account.class, given_uname);
                if( doc.password.equals(given_pass)){return "SUCCESS";}
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
                Intent n = new Intent(MainActivity.this,NavigationDrawer.class);
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

}
