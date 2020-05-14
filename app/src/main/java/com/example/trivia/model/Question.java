package com.example.trivia.model;

public class Question {

    private String question;
    private boolean answer;

    public Question() {
    }

    public Question(String question, boolean answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public boolean isAnswer() {
        return answer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", answer=" + answer +
                '}';
    }
}
