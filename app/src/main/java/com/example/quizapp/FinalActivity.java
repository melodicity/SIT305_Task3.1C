package com.example.quizapp;

import static com.example.quizapp.Constants.TOTAL_QUESTIONS;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FinalActivity extends AppCompatActivity {

    // Declare UI elements
    TextView tvCongrats, tvScoreValue;
    Button btnNewQuiz, btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_final);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialise UI elements
        tvCongrats = findViewById(R.id.tvCongrats);
        tvScoreValue = findViewById(R.id.tvScoreValue);
        btnNewQuiz = findViewById(R.id.btnNewQuiz);
        btnFinish = findViewById(R.id.btnFinish);

        btnNewQuiz.setBackgroundColor(ContextCompat.getColor(this, R.color.purple));
        btnFinish.setBackgroundColor(ContextCompat.getColor(this, R.color.purple));

        // Initialise values passed from the previous activity
        String username = getIntent().getStringExtra("username");
        int score = getIntent().getIntExtra("score", 0);

        // Set elements to display the above values
        tvCongrats.setText(getString(R.string.congratulations, username));
        tvScoreValue.setText(getString(R.string.score_value, score, TOTAL_QUESTIONS));

        // Set onClickListener for the New Quiz Button
        // Creates a new intent for the MainActivity, passes the username and starts the activity
        btnNewQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(FinalActivity.this, MainActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
            finish();
        });

        // Set onClickListener for the Finish Button
        // Calls finish() on this Activity, closing the app
        btnFinish.setOnClickListener(v -> finish());
    }
}