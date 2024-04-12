package com.example.quizapp;

import static com.example.quizapp.Constants.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    // Declare UI elements
    TextView tvWelcome, tvQuizProgress, tvQuestion;
    ProgressBar pbQuizProgress;
    Button btnAnswer1, btnAnswer2, btnAnswer3, btnSubmit;

    // References to the correct and chosen buttons, will change for each question
    Button btnClicked, btnCorrect;

    // Initialise question variables
    List<String[]> qnList;
    int qnIndex = 0;
    int qnScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialise UI elements
        tvWelcome = findViewById(R.id.tvWelcome);
        tvQuizProgress = findViewById(R.id.tvQuizProgress);
        tvQuestion = findViewById(R.id.tvQuestion);
        pbQuizProgress = findViewById(R.id.pbQuizProgress);
        btnAnswer1 = findViewById(R.id.btnAnswer1);
        btnAnswer2 = findViewById(R.id.btnAnswer2);
        btnAnswer3 = findViewById(R.id.btnAnswer3);
        btnSubmit = findViewById(R.id.btnSubmit);

        resetButtonColors();

        // Initialise text views
        String username = getIntent().getStringExtra("username");
        tvWelcome.setText(getString(R.string.welcome, username));

        // Shuffle a list of questions; the first 5 will be used
        qnList = pickQuestions();

        // Update elements to display the current question
        // If all questions have been answered, changes to FinalActivity
        updateQuestion(username);

        // Set onClickListener for the answer buttons
        btnAnswer1.setOnClickListener(onClickListener);
        btnAnswer2.setOnClickListener(onClickListener);
        btnAnswer3.setOnClickListener(onClickListener);

        // Set onClickListener for the submit button
        btnSubmit.setOnClickListener(v -> {
            // If the user pressed "Submit", check their answer
            if (btnSubmit.getText().equals("Submit")) {
                checkAnswer();
            } else {
                // The user pressed "Next", change the question shown
                qnIndex++;
                updateQuestion(username);
            }
        });
    }

    // Shuffles a list of predefined questions, the first five will be used this round
    private List<String[]> pickQuestions() {
        // Get a list of all question IDs from strings.xml
        List<Integer> qnIds = Arrays.asList(
                R.array.q1, R.array.q2, R.array.q3, R.array.q4, R.array.q5,
                R.array.q6, R.array.q7, R.array.q8, R.array.q9, R.array.q10
        );

        // Add all questions to a list
        List<String[]> questions = new ArrayList<>();
        for (int i = 0; i < qnIds.size(); i++) {
            questions.add(getResources().getStringArray(qnIds.get(i)));
        }

        // Shuffle the questions
        Collections.shuffle(questions);
        return questions;
    }
    
    // Check the user's answer was correct
    // Sets the button colors accordingly
    private void checkAnswer() {
        // Check if the clicked button refers to the same element as btnCorrect
        if (btnClicked == btnCorrect) qnScore++;
        else btnClicked.setBackgroundColor(ContextCompat.getColor(this, R.color.red));

        // Set the correct answer button green, overrides red color if answer was correct
        btnCorrect.setBackgroundColor(ContextCompat.getColor(this, R.color.green));

        // Disable the answer buttons
        btnAnswer1.setEnabled(false);
        btnAnswer2.setEnabled(false);
        btnAnswer3.setEnabled(false);

        // Change the submit button to "Next"
        btnSubmit.setText(getString(R.string.next));
    }

    // Updates the question and answer buttons' text
    private void updateQuestion(String username) {
        if (qnIndex < TOTAL_QUESTIONS) {
            // Set the text view and buttons to show the current question
            String[] qn = qnList.get(qnIndex);
            tvQuestion.setText(qn[0]);

            // Shuffle the answers, so btnAnswer1 is not always the solution
            List<String> ans = Arrays.asList(qn[1], qn[2], qn[3]);
            Collections.shuffle(ans);

            // Set the text for each button
            btnAnswer1.setText(ans.get(0));
            btnAnswer2.setText(ans.get(1));
            btnAnswer3.setText(ans.get(2));
            btnSubmit.setText(getString(R.string.submit));

            // Set btnCorrect to point to the correct answer button
            if (ans.get(0).equals(qn[1])) {
                btnCorrect = btnAnswer1;
            } else if (ans.get(1).equals(qn[1])) {
                btnCorrect = btnAnswer2;
            } else if (ans.get(2).equals(qn[1])) {
                btnCorrect = btnAnswer3;
            }

            // Update quiz progress bar
            pbQuizProgress.setProgress((qnIndex+1) * (100/TOTAL_QUESTIONS));
            tvQuizProgress.setText(getString(R.string.score_value, qnIndex+1, TOTAL_QUESTIONS));

            // Enable the answer buttons, disable the submit button
            btnAnswer1.setEnabled(true);
            btnAnswer2.setEnabled(true);
            btnAnswer3.setEnabled(true);
            btnSubmit.setEnabled(false);

            resetButtonColors();
        } else {
            // Create and start an intent for the FinalActivity
            Intent intent = new Intent(QuizActivity.this, FinalActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("score", qnScore);
            startActivity(intent);
            finish(); // closes the QuizActivity so the user cannot navigate back
        }
    }

    // Reset all buttons to their default color
    private void resetButtonColors() {
        btnAnswer1.setBackgroundColor(ContextCompat.getColor(this, R.color.purple));
        btnAnswer2.setBackgroundColor(ContextCompat.getColor(this, R.color.purple));
        btnAnswer3.setBackgroundColor(ContextCompat.getColor(this, R.color.purple));
        btnSubmit.setBackgroundColor(ContextCompat.getColor(this, R.color.purple));
    }

    // OnClickListener for the answer buttons
    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (btnClicked != null) {
                // Set the old clicked button to purple again
                btnClicked.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.purple));
            }

            // Set the selected button to refer to the clicked button
            btnClicked = (Button) v;
            btnClicked.setBackgroundColor(ContextCompat.getColor(QuizActivity.this, R.color.blue));

            // Enable the submit button
            btnSubmit.setEnabled(true);
        }
    };
}