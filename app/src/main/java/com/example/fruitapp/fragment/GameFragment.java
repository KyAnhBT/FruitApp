package com.example.fruitapp.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.fruitapp.Models.Answer;
import com.example.fruitapp.Models.Question;
import com.example.fruitapp.R;

import java.util.ArrayList;
import java.util.List;

public class GameFragment extends Fragment implements View.OnClickListener {
    private TextView tvQuestion,tvContentQuestion, tvAnswer1, tvAnswer2,tvAnswer3,tvAnswer4;
    private  List<Question> mListQuestion;
    private int currentQuestion = 0;
    private Question mQuestion;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        tvQuestion = view.findViewById(R.id.tv_question);
        tvContentQuestion = view.findViewById(R.id.tv_content_question);
        tvAnswer1 = view.findViewById(R.id.tv_answer1);
        tvAnswer2 = view.findViewById(R.id.tv_answer2);
        tvAnswer3 = view.findViewById(R.id.tv_answer3);
        tvAnswer4 = view.findViewById(R.id.tv_answer4);

        mListQuestion = getListQuestion();
        if(mListQuestion.isEmpty()){

        }else {
            setDataQuestion(mListQuestion.get(currentQuestion));
        }

        return view;
    }

    private void setDataQuestion(Question question) {
        if (question == null){
            return;
        }
        mQuestion = question;

        tvAnswer1.setBackgroundResource(R.drawable.bg_green_c_30);
        tvAnswer2.setBackgroundResource(R.drawable.bg_green_c_30);
        tvAnswer3.setBackgroundResource(R.drawable.bg_green_c_30);
        tvAnswer4.setBackgroundResource(R.drawable.bg_green_c_30);

        String titleQuestion = "Question " + question.getNumber();
        tvQuestion.setText(titleQuestion);

        tvContentQuestion.setText(question.getContent());

        tvAnswer1.setText(question.getListAnswer().get(0).getContent());
        tvAnswer2.setText(question.getListAnswer().get(1).getContent());
        tvAnswer3.setText(question.getListAnswer().get(2).getContent());
        tvAnswer4.setText(question.getListAnswer().get(3).getContent());

        tvAnswer1.setOnClickListener(this);
        tvAnswer2.setOnClickListener(this);
        tvAnswer3.setOnClickListener(this);
        tvAnswer4.setOnClickListener(this);
    }

    private List<Question> getListQuestion(){
        List<Question> list = new ArrayList<>();
        List<Answer> answerList1 = new ArrayList<>();
        answerList1.add(new Answer("Cotton fruit", false));
        answerList1.add(new Answer("Tamarind fruit", true));
        answerList1.add(new Answer("Black beans", false));
        answerList1.add(new Answer("Cabbage", false));

        List<Answer> answerList2 = new ArrayList<>();
        answerList2.add(new Answer("All options given", true));
        answerList2.add(new Answer("Rice", false));
        answerList2.add(new Answer("Corn", false));
        answerList2.add(new Answer("Lotus", false));

        List<Answer> answerList3 = new ArrayList<>();
        answerList3.add(new Answer("Lighter than water", false));
        answerList3.add(new Answer("Water proof", false));
        answerList3.add(new Answer("All options given", false));
        answerList3.add(new Answer("Contains air", true));

        List<Answer> answerList4 = new ArrayList<>();
        answerList4.add(new Answer("Red", false));
        answerList4.add(new Answer("Yellow", false));
        answerList4.add(new Answer("Green", true));
        answerList4.add(new Answer("Purple", false));

        List<Answer> answerList5 = new ArrayList<>();
        answerList5.add(new Answer("One", true));
        answerList5.add(new Answer("Two", false));
        answerList5.add(new Answer("Three", false));
        answerList5.add(new Answer("For", false));

        List<Answer> answerList6 = new ArrayList<>();
        answerList6.add(new Answer("Lion", false));
        answerList6.add(new Answer("Snake", false));
        answerList6.add(new Answer("Cat", false));
        answerList6.add(new Answer("Bird", true));

        List<Answer> answerList7 = new ArrayList<>();
        answerList7.add(new Answer("Orange", false));
        answerList7.add(new Answer("Strawberry", false));
        answerList7.add(new Answer("Guava", true));
        answerList7.add(new Answer("Papaya", false));

        List<Answer> answerList8 = new ArrayList<>();
        answerList8.add(new Answer("Watermelon", false));
        answerList8.add(new Answer("Tomato", true));
        answerList8.add(new Answer("Strawberry", false));
        answerList8.add(new Answer("Banana", false));

        List<Answer> answerList9 = new ArrayList<>();
        answerList9.add(new Answer("Coconut", false));
        answerList9.add(new Answer("Papaya", false));
        answerList9.add(new Answer("Banana", true));
        answerList9.add(new Answer("Pomelo", false));

        List<Answer> answerList10 = new ArrayList<>();
        answerList10.add(new Answer("Beer", false));
        answerList10.add(new Answer("Coca", false));
        answerList10.add(new Answer("Sugar", false));
        answerList10.add(new Answer("SO2", true));

        list.add(new Question("When ripe, the rind of which of the following is not capable of cracking on its own?",1,answerList1));
        list.add(new Question("Which of the following seeds is actually a fruit?",2,answerList2));
        list.add(new Question("Most fruits float on water because: ",3,answerList3));
        list.add(new Question("Which colored fruit can help strength teeth and bones?",4,answerList4));
        list.add(new Question("How many fruits do pineapple tree each year?",5,answerList5));
        list.add(new Question("Kiwi fruit is named after what animal?",6,answerList6));
        list.add(new Question("Which of the following fruit contains the most vitamin C?",7,answerList7));
        list.add(new Question("Which is the most famous fruit in the world?",8,answerList8));
        list.add(new Question("Which of the following fruit contains radioactive material?",9,answerList9));
        list.add(new Question("Which substances do raisins usually use to create color?",10,answerList10));
        return  list;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_answer1:
                tvAnswer1.setBackgroundResource(R.drawable.bg_orange_c_30);
                checkAnswer(tvAnswer1, mQuestion, mQuestion.getListAnswer().get(0));
                break;
            case R.id.tv_answer2:
                tvAnswer2.setBackgroundResource(R.drawable.bg_orange_c_30);
                checkAnswer(tvAnswer2, mQuestion, mQuestion.getListAnswer().get(1));
                break;
            case R.id.tv_answer3:
                tvAnswer3.setBackgroundResource(R.drawable.bg_orange_c_30);
                checkAnswer(tvAnswer3, mQuestion, mQuestion.getListAnswer().get(2));
                break;
            case R.id.tv_answer4:
                tvAnswer4.setBackgroundResource(R.drawable.bg_orange_c_30);
                checkAnswer(tvAnswer4, mQuestion, mQuestion.getListAnswer().get(3));
                break;
        }
    }

    private void checkAnswer(TextView textView, Question question, Answer answer){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(answer.isCorrect()){
                    textView.setBackgroundResource(R.drawable.bg_green);
                    nextQuestion();
                }else {
                    textView.setBackgroundResource(R.drawable.bg_red_c_30);
                    showAnserCorrect(question);
                    gameOver();
                }
            }
        },1000);
    }

    private void gameOver() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showDialog("Game Over");
            }
        },1000);
    }

    private void showAnserCorrect(Question question) {
        if(question == null || question.getListAnswer() == null || question.getListAnswer().isEmpty()){
            return;
        }
        if (question.getListAnswer().get(0).isCorrect()){
            tvAnswer1.setBackgroundResource(R.drawable.bg_green);
        }else if(question.getListAnswer().get(1).isCorrect()){
            tvAnswer2.setBackgroundResource(R.drawable.bg_green);
        }else if(question.getListAnswer().get(2).isCorrect()){
            tvAnswer3.setBackgroundResource(R.drawable.bg_green);
        }else if(question.getListAnswer().get(3).isCorrect()){
            tvAnswer4.setBackgroundResource(R.drawable.bg_green);
        }
    }

    private void nextQuestion() {
        if (currentQuestion == mListQuestion.size() - 1){
            showDialog("You Win");
        }else {
            currentQuestion++;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setDataQuestion(mListQuestion.get(currentQuestion));
                }
            },1000);

        }
    }
    private  void showDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                currentQuestion = 0;
                setDataQuestion(mListQuestion.get(currentQuestion));
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
