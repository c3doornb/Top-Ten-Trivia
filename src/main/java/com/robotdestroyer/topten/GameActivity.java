package com.robotdestroyer.topten;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;

public class GameActivity extends AppCompatActivity {

    private ImageButton btnGuess;
    private Button btnCat;
    private Button btnNext;
    private Button btnFreebie;

    private TextView tvQuestion;
    private TextView[] tvAnswers = new TextView[10];

    private EditText etGuess;
    private boolean[] guessed = new boolean[10];
    private Category category;
    private Question question;
    private String chosenCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Gets the chosen category from the main activity
        Bundle extras = getIntent().getExtras();
        chosenCategory = extras.getString("Cat");

        manageJsonFile();
        init();
        getQuestion();
    }

    //Finds the correct json file with all of the questions according to the chosen category
    // and makes a new category object from the file
    private void manageJsonFile() {
        String json = null;
        try {
            int catId = getResources().getIdentifier(chosenCategory, "raw", getApplicationContext().getPackageName());
            InputStream inputStream = getResources().openRawResource(catId);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (json.length() > 0) {
            Gson gson = new Gson();
            category = gson.fromJson(String.valueOf(json), Category.class);
        } else {
            Log.e("JSON Error", "json not parsed");
        }
    }

    //Initializes all layout objects
    private void init() {
        btnGuess = findViewById(R.id.btnGuess);
        btnCat = findViewById(R.id.btnCategories);
        btnNext = findViewById(R.id.btnNextQuestion);
        btnFreebie = findViewById(R.id.btnFreebie);

        tvQuestion = findViewById(R.id.tvQuestion);
        for(int i = 1; i < 11; i++) {
            String id = "tvAnswer" + i;
            int answerID = getResources().getIdentifier(id, "id", getApplicationContext().getPackageName());
            tvAnswers[i - 1] = findViewById(answerID);
        }


        etGuess = findViewById(R.id.editText);

        //Submit Guess with enter button on keyboard
        etGuess.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    submitGuess();
                    return true;
                }
                return false;
            }
        });

        //Button clicks
        //Submit Guess
        btnGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitGuess();
            }
        });
        //Go back to menu
        btnCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(GameActivity.this,
                        MainActivity.class);
                startActivity(myIntent);
            }
        });
        //Get another question
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQuestion();
            }
        });
        //Gives lowest answer
        btnFreebie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFreebie();
            }
        });
    }

    //Gets another question
    private void getQuestion() {
        resetAnswers();
        guessed = new boolean[10];

        int ran = (int) (Math.random() * category.questions.length);
        Log.d("Questions", category.questions.length + "");
        Log.d("Questions", ran + "");
        question = category.questions[ran];

        tvQuestion.setText(question.question);
    }

    //Submit Guess
    private void submitGuess() {
        String guess = etGuess.getText().toString();
        Log.d("Submitted guess", guess.toLowerCase());
        for (int i = 0; i < 10; i++) {
            Log.d("Answers", question.answers[i].toLowerCase());
            if (guess.equalsIgnoreCase(question.answers[i])) {
                Log.d("Game", "Correct!");
                correctGuess(i);
                break;
            }
        }
        etGuess.setText("");
        etGuess.clearFocus();
        etGuess.requestFocusFromTouch();
    }

    //If guess is correct
    private void correctGuess(int index) {
        int displayIndex = index + 1;
        tvAnswers[index].setText(displayIndex + ") " + question.answers[index]);
        guessed[index] = true;
    }

    //Gives lowest answer
    private void getFreebie() {
        for (int i = 9; i >= 0; i--) {
            if (!guessed[i]) {
                guessed[i] = true;
                int displayIndex = i + 1;
                tvAnswers[i].setText(displayIndex + ") " + question.answers[i]);
                etGuess.requestFocus();
                return;
            }
        }
    }

    //Clears all answers
    private void resetAnswers() {
        for (int i = 1; i < 11; i++) {
            tvAnswers[i - 1].setText(i + ") ");
        }
    }
}
