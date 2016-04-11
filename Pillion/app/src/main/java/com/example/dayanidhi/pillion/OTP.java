package com.example.dayanidhi.pillion;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class OTP extends AppCompatActivity {
    public static EditText et;

    FloatingActionButton re;
    public static ProgressDialog progressDialog;
   // String dp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        progressDialog = new ProgressDialog(OTP.this);
        progressDialog.setTitle("Message");
        progressDialog.setMessage("Please wait while we read the OTP...");
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        progressDialog.show();
        re =(FloatingActionButton)findViewById(R.id.reg);
        et =(EditText)findViewById(R.id.otp);
       // dp=getIntent().getExtras().getString("dp");
      //  et.setText(getIntent().getExtras().getString("rand","000000"));
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mob=getIntent().getExtras().getString("mobile");
                String otp =getIntent().getExtras().getString("otp");
                System.out.println("OTP GOT:"+otp);
                String password = getIntent().getExtras().getString("password");
                if(otp.equals(et.getText().toString())){
                    Toast.makeText(getApplicationContext(),"OTP MATCHES..",Toast.LENGTH_LONG).show();

                    Intent aa = new Intent(OTP.this,Register.class);
                    aa.putExtra("mobile",mob);
                    aa.putExtra("password",getIntent().getExtras().getString("password"));
                    //aa.putExtra("dp",dp);
                    aa.putExtra("name",getIntent().getExtras().getString("name"));
                    aa.putExtra("gender",getIntent().getExtras().getString("gender"));
                    aa.putExtra("dob",getIntent().getExtras().getString("dob"));
                    aa.putExtra("email",getIntent().getExtras().getString("email"));
                    aa.putExtra("userid",getIntent().getExtras().getString("userid"));
                    startActivity(aa);
                }
                else{
                    Toast.makeText(getApplicationContext(),"OTP MIS-MATCH..",Toast.LENGTH_LONG).show();

                }
              //  Intent aa = new Intent(OTP.this,Register.class);
               // aa.putExtra("name","arvind");
                //startActivity(aa);
            }
        });

    }


















}
