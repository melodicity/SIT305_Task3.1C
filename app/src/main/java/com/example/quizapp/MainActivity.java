package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Declare UI elements
    EditText etName;
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialise UI elements
        btnStart = findViewById(R.id.btnStart);
        btnStart.setBackgroundColor(ContextCompat.getColor(this, R.color.purple));
        etName = findViewById(R.id.etName);
        if (getIntent().hasExtra("username")) {
            // Retrieve the username from the intent (if the user has already given their name)
            etName.setText(getIntent().getStringExtra("username"));
        }

        // Set onClickListener for the Start Button
        btnStart.setOnClickListener(v -> {
            // Get the username from etName
            String username = etName.getText().toString().trim();

            // Check if a username has been entered
            if (username.isEmpty()) {
                // Send a Toast telling the user to enter their name
                Toast.makeText(MainActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
            } else {
                // Create an intent for the QuizActivity, passes the username and starts the activity
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            }
        });
    }
}