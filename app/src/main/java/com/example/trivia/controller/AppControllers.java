package com.example.trivia.controller;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppControllers extends Application {

    private static AppControllers instance;
    private RequestQueue requestQueue;


    public static synchronized AppControllers getInstance(){
        if (instance == null){
            instance = new AppControllers();
        }
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> AppControllers addingRequest(Request<T> request){
        getRequestQueue().add(request);
        return null;
    }

    public void cancelPending(Object tag){
        if (requestQueue != null){
            requestQueue.cancelAll(tag);
        }
    }

}
