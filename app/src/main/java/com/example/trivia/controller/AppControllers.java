package com.example.trivia.controller;

import android.app.Application;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppControllers extends Application {
    private static AppControllers instance;
    private RequestQueue requestQueue;
    private static SharedPreferences sharedPreferences;
    public static synchronized AppControllers getInstance(){
        if (instance == null){
            instance = new AppControllers();
        }
        return instance;
    }


    public static void editing(int high_score){
        sharedPreferences  = getInstance().getSharedPreferences("HighScore", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int score = sharedPreferences.getInt("high",0);
        if (score < high_score){
            score = high_score;
            editor.putInt("high",score).apply();
        }
    }

    public static int settingScore(){
        if (sharedPreferences != null) {
            return sharedPreferences.getInt("high", 0);
        }
        return 0;
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
