package com.example.dayanidhi.pillion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class Register extends AppCompatActivity implements OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    private ProgressDialog progressDialog;
    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    String userid;
FloatingActionButton reg;
    EditText name1,email1;
    TextView dob1,state1;
    RadioButton gender1,gender2;
    String dp;
    RoundedImageView riw;
 public   String name,gender,dob,email,state,mobile,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name1=(EditText)findViewById(R.id.name);
        dob1=(TextView)findViewById(R.id.dob);
        gender1=(RadioButton)findViewById(R.id.male);
        gender2=(RadioButton)findViewById(R.id.female);
        riw=(RoundedImageView)findViewById(R.id.img);
        userid= getIntent().getExtras().getString("userid");
        String name2=getIntent().getExtras().getString("name");
        String gender3=getIntent().getExtras().getString("gender");
        String dob2=getIntent().getExtras().getString("dob");
        String email2=getIntent().getExtras().getString("email");
        name1.setText(name2);
        dob1.setText(dob2);
        if(gender3.equals("male")){gender1.setChecked(true);}
        else{gender2.setChecked(true);}
     //  String  aa.putExtra("userid",getIntent().getExtras().getString("userid"));



        Picasso.with(this).load("https://graph.facebook.com/" + userid+ "/picture?type=large").into(riw);

        final Calendar calendar = Calendar.getInstance();
      //  dp=getIntent().getExtras().getString("dp");
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    //    final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);

        dob1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                datePickerDialog.setYearRange(1950, 2016);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });


        if (savedInstanceState != null) {
            DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
            if (dpd != null) {
                dpd.setOnDateSetListener(this);
            }

            TimePickerDialog tpd = (TimePickerDialog) getSupportFragmentManager().findFragmentByTag(TIMEPICKER_TAG);
            if (tpd != null) {
                tpd.setOnTimeSetListener(this);
            }
        }


       gender1.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               gender2.setChecked(false);
           }
       });
        gender2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gender1.setChecked(false);
            }
        });
        email1=(EditText)findViewById(R.id.email);
        email1.setText(email2);
        state1=(TextView)findViewById(R.id.state);
        reg=(FloatingActionButton)findViewById(R.id.reg);
        reg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                name = name1.getText().toString();
                if (gender1.isChecked()) {
                    gender = "Male";
                } else if (gender2.isChecked()) {
                    gender = "Female";
                }
                dob = dob1.getText().toString();
                email = email1.getText().toString();
                state = state1.getText().toString();
                mobile = getIntent().getExtras().getString("mobile");
                password = getIntent().getExtras().getString("password");
                System.out.println(name + gender + dob + email + state + mobile + password);
               System.out.println("Starting my task");
               new MyTask1().execute();
            }
        });
        state1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Register.this,state1);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
                popup.setGravity(1);
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        state1.setText(item.getTitle());
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });


    }



    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        dob1.setText(day + "-" + (month + 1) + "-" + year);
      //  Toast.makeText(Register.this, "new date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        Toast.makeText(Register.this, "new time:" + hourOfDay + "-" + minute, Toast.LENGTH_LONG).show();
    }

    private class MyTask1 extends AsyncTask<String, Integer, String> {

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Register.this, "Message", "Registering...");

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

// A Java type that can be serialized to JSON
 db.save(new Account(mobile, name, email, gender, dob, true, state, password,0,userid));


// Create an Account and save it in the database


            }catch(Exception e){System.out.println(e.toString());
                Toast.makeText(getApplicationContext(),"Try After Sometime !!",Toast.LENGTH_LONG).show();
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
            Intent nn = new Intent(Register.this,LoginActivity.class);
            nn.putExtra("mobile",mobile);
            startActivity(nn);
            // Do things like hide the progress bar or change a TextView
        }
    }

}
