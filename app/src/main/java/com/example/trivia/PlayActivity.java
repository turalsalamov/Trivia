package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.trivia.controller.AppControllers;

public class PlayActivity extends AppCompatActivity {
    private TextView highestScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        highestScore = findViewById(R.id.highest_score);
        gettingHighScore();
        Button play = findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayActivity.this,MainActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        gettingHighScore();
    }

    private void gettingHighScore(){
        AppControllers.getInstance();
        AppControllers.editing(0);
        int score = AppControllers.settingScore();
        highestScore.setText("Highest score: " + score);
    }


}


