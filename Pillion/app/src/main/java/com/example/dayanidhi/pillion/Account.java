package com.example.dayanidhi.pillion;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by arvind on 10/01/16.
 */
public class Account implements Serializable {
    public String _id;
    public String _rev =null;
    public String fname;
    public String email;
    public String gender;
    public String dob;
    public boolean verified;
    public String state;
    public String password;
    public float balance;
    public String userid;
    public ArrayList<String> associated_users;
    public String group_id;

    public Account(String userid){
        this._id=userid;

    }
    public Account(String mobile, String fname,  String email, String gender, String dob, boolean verified, String state,String password,float balance,String userid){
        this._id=mobile;
        this.balance=balance;
        this.fname=fname;
        this.email=email;
        this.gender=gender;
        this.dob=dob;
        this.verified=verified;
        this.state=state;
        this.password=password;
        this.userid=userid;

    }

    public float getBalance(){
        return balance;

    }

    public void updateBalance(int new_amt){
        this.balance=this.balance+new_amt;
    }

    public String toString() {
        return "{ id: " + _id + ",\nrev: " + _rev + "\nfname:"+fname+"\nemail:"+email+"\ngender:"+gender+
    "\ndob:"+dob+"\nverfied:"+verified+"\nstate:"+state+"\npassword:"+password+"}";
    }


    public void addAssociatedUsers(String acc_id){
        this.associated_users.add(acc_id);
    }
}