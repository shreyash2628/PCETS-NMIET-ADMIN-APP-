package com.example.collegeadminapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.collegeadminapp.MenuOptions.UploadEbook;
import com.example.collegeadminapp.MenuOptions.UploadNotice;
import com.example.collegeadminapp.MenuOptions.quizCard;

public class DashBoardActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView uploadNoticeCard,uploadEbookCard,uploadQuizQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);


        uploadNoticeCard = findViewById(R.id.uploadNotice);
        uploadEbookCard = findViewById(R.id.uploadEbookCard) ;
        uploadQuizQuestions = findViewById(R.id.upload_quiz_ques_Card);

        uploadQuizQuestions.setOnClickListener(this);
        uploadNoticeCard.setOnClickListener(this);
        uploadEbookCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {

            case R.id.uploadNotice:
                Intent intent = new Intent(DashBoardActivity.this, UploadNotice.class);
                startActivity(intent);
                break;

            case R.id.uploadEbookCard:
                Intent intent1 = new Intent(DashBoardActivity.this, UploadEbook.class);
                startActivity(intent1);
                break;


            case R.id.upload_quiz_ques_Card:

                Intent intent2 = new Intent(DashBoardActivity.this, quizCard.class);
                startActivity(intent2);
                break;


        }

    }
}