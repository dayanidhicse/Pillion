package com.example.dayanidhi.pillion;

import com.ibm.watson.developer_cloud.personality_insights.v2.model.Profile;

import java.io.Serializable;

/**
 * Created by arvind on 13/02/16.
 */
public class Personality  implements Serializable {

    public Profile profile;
    public  String _id;

    public Personality(String _id,Profile profile){
        this.profile=profile;
        this._id=_id;
    }
}
