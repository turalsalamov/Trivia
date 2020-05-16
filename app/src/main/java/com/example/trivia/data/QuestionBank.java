package com.example.trivia.data;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import com.example.trivia.controller.AppControllers;
import com.example.trivia.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class QuestionBank {
    private static ArrayList<Question> questions = new ArrayList<>();

    public void getQuestions(final AnswerListAsyncResponse callback){
        String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,
                 null, new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            questions = new ArrayList<>();
            for (int i = 0; i < response.length(); i++){
                try {
                    JSONArray jsonArray = response.getJSONArray(i);
                    String question = jsonArray.get(0).toString();
                    boolean answer = jsonArray.getBoolean(1);
                    questions.add(new Question(question, answer));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }if (callback != null) {
                callback.processFinished(questions);
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error.networkResponse == null){
                if (error.getClass().equals(TimeoutError.class)){
                    System.out.println("There is network problem");
                }
            }
        }
    });
        AppControllers.getInstance().addingRequest(jsonArrayRequest);
    }

}
