package com.example.whatsapp;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class Apps extends Application {
    public void onCreate() {

        super.onCreate();
         Parse.enableLocalDatastore(this);
       // Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);


        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())

                .applicationId("myappID")
                .clientKey("MBkqXLrJfQn2")
                .server("http://18.191.73.246/parse") //You have to check every time
                 .enableLocalDataStore()
                .build()
        );




    }

}
