
package com.example.collegeadminapp.MenuOptions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.collegeadminapp.ModalClasses.QuizModalClass;
import com.example.collegeadminapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class quizCard extends AppCompatActivity {

    private EditText AddQueET,option1,option2,option3,option4,queNoET,ans;
    private Button addButton;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_card);

    //hooks
        queNoET = findViewById(R.id.queNo);
        AddQueET = findViewById(R.id.add_que);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        ans = findViewById(R.id.ans);
       addButton = findViewById(R.id.add_question_button);

       firebaseDatabase = FirebaseDatabase.getInstance();
       databaseReference = firebaseDatabase.getReference("QuizQuestions");


       addButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String que,queNo,o1,o2,o3,o4,ansStr;

               queNo=queNoET.getText().toString();
               que=AddQueET.getText().toString();
               o1=option1.getText().toString();
               o2=option2.getText().toString();
               o3=option3.getText().toString();
               o4=option4.getText().toString();
               ansStr=ans.getText().toString();


               if(que.isEmpty() || o1.isEmpty() || o2.isEmpty() || o3.isEmpty() || o4.isEmpty() || queNo.isEmpty() || ansStr.isEmpty())
               {
                   Toast.makeText(quizCard.this, "Value cant be Empty", Toast.LENGTH_SHORT).show();
               }

               else{
                   QuizModalClass quizModalClass = new QuizModalClass(que,o1,o2,o3,o4,ansStr);

                   databaseReference.child(queNo).setValue(quizModalClass);
                   Toast.makeText(quizCard.this, "Successfully added!!!", Toast.LENGTH_SHORT).show();


                   queNoET.setText("");
                   AddQueET.setText("");
                   option1.setText("");
                   option2.setText("");
                   option3.setText("");
                   option4.setText("");
                   ans.setText("");
               }



           }
       });


    }
}