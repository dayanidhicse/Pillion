package com.example.dayanidhi.pillion;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by arvind on 18/01/16.
 */
public class Schedule implements Serializable {
    public String trip_id;
    public String mobile;
    public String start,stop,date_1,date_2,time_1,time_2,calculated_distance,calculated_time,stopovers,ride_details,bikemodel,preffered_gender,co_traveller_id;
    boolean detour,fixed;
    public String getCo_traveller_id;
    public String costofride;
    public String _id;
    public String _rev = null;
    public String userid;
    public boolean review_over;
    public ArrayList<LatLng> directionPositionList= new ArrayList<>();

    public Schedule(String trip_id,String mobile, String start,String stop,String date_1,String date_2,String time_1,String time_2,String calculated_distance,String calculated_time,boolean detour,String total_cost,String bikemodel,String ride_details,String userid,ArrayList<LatLng> directionPositionList){
        this.trip_id=trip_id;
        this.mobile=mobile;
        this.start=start;
        this.stop=stop;
        this.date_1=date_1;
        this.costofride=total_cost;
        this.date_2=date_2;
        this.time_1=time_1;
        this.time_2=time_2;
        this.calculated_distance=calculated_distance;
        this.calculated_time=calculated_time;
        this.detour=detour;
        this._id=trip_id;
        this.fixed=false;
        this.co_traveller_id="";
        this.stopovers="No Stopovers";
        this.ride_details="I am expecting a good person to ride with me..";
        this.bikemodel="Pulsar 180CC - Black";
        this.preffered_gender="Male";
        this.bikemodel=bikemodel;
        this.ride_details=ride_details;
        this.userid=userid;
        this.review_over=false;
        this.directionPositionList=directionPositionList;
    }

    public void setCoTraveller(String co_traveller_id){
        this.co_traveller_id=co_traveller_id;
        this.fixed=true;
    }

}