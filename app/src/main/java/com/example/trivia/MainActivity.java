package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trivia.data.AnswerListAsyncResponse;
import com.example.trivia.data.QuestionBank;
import com.example.trivia.model.Question;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    private static int number;
    private ArrayList<Question> questionArrayList = new ArrayList<>();
    private TextView question;
    private TextView counter;
    private Button previous;
    private Button next;
    private CardView cardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        number = 1;

        counter = findViewById(R.id.counter);
        question = findViewById(R.id.question);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        cardView = findViewById(R.id.cardview);

        questionArrayList.clear();
        new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> arrayList) {
                questionArrayList = arrayList;
                updateQuestionAndNumberOfThem();
                if (number == 1){
                    previous.setEnabled(false);
                }
            }
        });

    }

    public void clicking(View view){
        switch (view.getId()){
            case R.id.previous:{
                //code for previous button
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
                //code for next
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
                //some code for true button
            }
        }
    }

    
    public void updateQuestionAndNumberOfThem(){
        question.setText(questionArrayList.get(number-1).getQuestion());
        counter.setText(number + "/" + questionArrayList.size());
    }


    private void fadingAnimation(){
        Animation fading = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fading);
        cardView.setAnimation(fading);

        fading.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(getResources().getColor(R.color.card,null));
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
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

}