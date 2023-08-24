package com.example.fruitapp.Models;

import java.util.List;

public class Question {

    private String content;
    private  int number;
    private List<Answer> listAnswer;

    public Question(String content, int number, List<Answer> listAnswer) {
        this.content = content;
        this.number = number;
        this.listAnswer = listAnswer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Answer> getListAnswer() {
        return listAnswer;
    }

    public void setListAnswer(List<Answer> listAnswer) {
        this.listAnswer = listAnswer;
    }
}
