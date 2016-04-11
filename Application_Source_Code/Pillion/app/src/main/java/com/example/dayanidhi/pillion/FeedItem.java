package com.example.dayanidhi.pillion;

/**
 * Created by arvind on 17/01/16.
 */

public class FeedItem {
    private String title;
    private String thumbnail;
    public String trip_id;
    public String dateoftravel,timeoftravel,costofride,sourcetodestination,nameandage;

    public String getTitle() {
        return title;
    }
    public String getTrip_id(){return  trip_id;}
    public void setTrip_id(String trip_id){this.trip_id=trip_id;}
    public void setTitle(String title) {
        this.title = title;
    }
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}