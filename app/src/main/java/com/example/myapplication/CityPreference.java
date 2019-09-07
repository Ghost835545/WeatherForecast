package com.example.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

public class CityPreference {
    SharedPreferences prefs;
    public CityPreference(Activity activity){
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);

    }

    // If the user has not chosen a City yet, return
    // Sydney as the default City
    String getCity(){
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString("City","Moscow");
        ed.commit();
        return prefs.getString("City", "");

    }

    void setCity(String city){
        prefs.edit().putString("City", city).commit();
    }

}

