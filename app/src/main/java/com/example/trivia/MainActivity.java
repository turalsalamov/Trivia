package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trivia.controller.AppControllers;
import com.example.trivia.data.AnswerListAsyncResponse;
import com.example.trivia.data.QuestionBank;
import com.example.trivia.model.Question;
import android.app.AlertDialog;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView scoreText;
    private static int score;
    private static int number;
    private ArrayList<Question> questionArrayList = new ArrayList<>();
    private TextView question;
    private TextView counter;
    private Button previous;
    private Button next;
    private CardView cardView;
    private  TextView timerText;
    public static int highScore;
    private static CountDownTimer countDownTimer;
    private boolean isPause = false;
    private Long timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        highScore = 0;
        number = 1;
        score = 0;
        counter = findViewById(R.id.counter);
        question = findViewById(R.id.question);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        cardView = findViewById(R.id.cardview);
        scoreText = findViewById(R.id.score);
        timerText = findViewById(R.id.time);
        questionArrayList = new ArrayList<>();

        //Showing questions and timer
        new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> arrayList) {

                questionArrayList = arrayList;
                updateQuestionAndNumberOfThem();
               countDownTimer =  new CountDownTimer(31000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timer = millisUntilFinished;
                        Long time = millisUntilFinished/1000;
                        timerText.setText(time.toString());
                    }

                    @Override
                    public void onFinish() {
                        alertDialogFunction();
                    }
                }.start();
                if (number == 1) {
                    previous.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        countDownTimer.cancel();
        isPause = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        afterPause();
    }

    // Clicking method of buttons
    public void clicking(View view){
        switch (view.getId()){
            case R.id.previous:{
                    number = number - 1;
                if (number == 1){
                    previous.setEnabled(false);
                    Toast.makeText(MainActivity.this,"It is first question and you can not move to previous question!",Toast.LENGTH_SHORT).show();
                }
                updateQuestionAndNumberOfThem();
                break;
            }
            case R.id.next:{
                if (number == 1){
                    previous.setEnabled(true);
                }
                if (number == questionArrayList.size()){
                    next.setEnabled(false);
                    Toast.makeText(MainActivity.this,"It is last question and you can not move to next question!",Toast.LENGTH_SHORT).show();
                }else{
                    number = number + 1;
                    if (number == questionArrayList.size()){
                        next.setEnabled(false);
                        Toast.makeText(MainActivity.this,"It is last question and you can not move to next question!",Toast.LENGTH_SHORT).show();
                    }
                    updateQuestionAndNumberOfThem();
                }
                break;
            }
            case R.id.false_button:{
                if (!questionArrayList.get(number - 1).isAnswer()){
                    Toast.makeText(MainActivity.this,"You are correct",Toast.LENGTH_SHORT).show();
                    updateQuestionAndNumberOfThem();
                    fadingAnimation();
                }else{
                    Toast.makeText(MainActivity.this,"You are wrong",Toast.LENGTH_SHORT).show();
                    updateQuestionAndNumberOfThem();
                    shakeAnimation();
                }
                break;
            } default:{
                if (questionArrayList.get(number-1).isAnswer()){
                    Toast.makeText(MainActivity.this,"You are correct",Toast.LENGTH_SHORT).show();
                    updateQuestionAndNumberOfThem();
                    fadingAnimation();
                }else{
                    Toast.makeText(MainActivity.this,"You are wrong",Toast.LENGTH_SHORT).show();
                    updateQuestionAndNumberOfThem();
                    shakeAnimation();
                }
                break;
            }
        }
    }

    //Updating of cardview
    public void updateQuestionAndNumberOfThem(){
        question.setText(questionArrayList.get(number-1).getQuestion());
        counter.setText(number + "/" + questionArrayList.size());
    }

    //Animations
    private void fadingAnimation(){
        Animation fading = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fading);
        cardView.setAnimation(fading);

        fading.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
                score();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(getResources().getColor(R.color.card,null));
                afterAnswer();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(getResources().getColor(R.color.card,null));
                afterAnswer();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    //Updating cardview after answering
    public void afterAnswer(){
        questionArrayList.remove(number-1);
        updateQuestionAndNumberOfThem();
    }

    //setting score text
    public void score(){
        score++;
        scoreText.setText("Score: " + score);
    }

    //Dialog
    public void alertDialogFunction(){
        AlertDialog.Builder builder =  new AlertDialog.Builder(MainActivity.this).setTitle("Again").setMessage("Do you want to play again?").
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppControllers.getInstance();
                        AppControllers.editing(score);
                        MainActivity.this.recreate();
                    }
                }).
                setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppControllers.getInstance();
                        AppControllers.editing(score);
                        finish();
                    }
                });
        builder.create();
        builder.show();
    }
    //setting timer after pausing
    private void afterPause(){
        if (isPause) {
            countDownTimer = new CountDownTimer(timer, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timer = millisUntilFinished;
                    Long time = millisUntilFinished / 1000;
                    timerText.setText(time.toString());
                    isPause = false;
                }

                @Override
                public void onFinish() {
                    alertDialogFunction();
                }
            }.start();
        }
    }

}