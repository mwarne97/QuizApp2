package com.example.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";

    private static final String CURRENT_INDEX = "Current Index";
    private static final String CURRENT_SCORE = "Current Score";
    private static final String IS_CHEATER = "Is Cheater";

    private static final int REQUEST_CODE_CHEAT = 0;

    private int mCurrentIndex;
    private int mScore;

    private boolean mIsCheater;

    TextView mQuestionText, mCorrectAnswerCounterTxt;
    TextView mQuestionNumberTxt;
    Button mTrueButton, mFalseButton;
    Button mNextButton, mPreviousButton;
    Button mCheatButton;


    Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_txt_australia, true, false, false),
            new Question(R.string.question_txt_oceans, true, false, false),
            new Question(R.string.question_txt_mideast, false, false, false),
            new Question(R.string.question_txt_africa, false, false, false),
            new Question(R.string.question_txt_americas, true, false, false),
            new Question(R.string.question_txt_asia, true, false, false)
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(CURRENT_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(IS_CHEATER, false);
            mScore = savedInstanceState.getInt(CURRENT_SCORE, 0);
        }

        int question = mQuestionBank[mCurrentIndex].getTextResId();

        mQuestionText = (TextView) findViewById(R.id.question_txt_view);
        mQuestionText.setText(question);

        mQuestionNumberTxt = (TextView) findViewById(R.id.question_number_txt_view);
        mQuestionNumberTxt.setText(getString(R.string.question_number_txt, mCurrentIndex + 1));

        mCorrectAnswerCounterTxt = (TextView) findViewById(R.id.correct_number_txt_view);
        mCorrectAnswerCounterTxt.setText(("Correct: " + mScore + "/" + mQuestionBank.length));

        //True Button
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }//End onClick
        });

        //False Button
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }//End onClick
        });

        //Next Button
        mNextButton = (Button) findViewById(R.id.next_button);
        if (mCurrentIndex==mQuestionBank.length-1) {
            mNextButton.setEnabled(false);
        }
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }//End onClick
        });

        //Previous Button
        mPreviousButton = (Button) findViewById(R.id.previous_button);
        if (mCurrentIndex == 0) {
            mPreviousButton.setEnabled(false);
        }
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                updateQuestion();
            }//End onClick
        });

        //Cheat Button
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intention = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intention, REQUEST_CODE_CHEAT);
            }//End onClick
        });

        if (mIsCheater) {
            disableCheatButton();
        }

    }// End onCreate

    //Save state of application here
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_INDEX, mCurrentIndex);
        outState.putInt(CURRENT_SCORE, mScore);
        outState.putBoolean(IS_CHEATER, mIsCheater);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }//End If
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data != null) {
                mIsCheater = CheatActivity.wasAnswerShown(data);
                if (mIsCheater) {
                    mQuestionBank[mCurrentIndex].setHasPressedCheat(true);
                    disableCheatButton();
                }
            }
        }//End If
    }//End onActivityResult

    //Updates Questions
    private void updateQuestion() {

        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionText.setText(question);

        //First Question
        if (mCurrentIndex == 0) {
            mPreviousButton.setEnabled(false);
            mNextButton.setEnabled(true);
        }

        //Last Question
        else if (mCurrentIndex == mQuestionBank.length - 1) {
            mNextButton.setEnabled(false);
            mPreviousButton.setEnabled(true);
        }

        //In Between First And Last Questions
        else {
            mPreviousButton.setEnabled(true);
            mNextButton.setEnabled(true);
        }

        if (mQuestionBank[mCurrentIndex].HasBeenAnswered()) {
            disableAnswerButtons();
            disableCheatButton();
        } else {
            mIsCheater = false;
            enableAnswerButtons();
            if (mQuestionBank[mCurrentIndex].HasPressedCheat()) {
                mIsCheater = true;
                disableCheatButton();
            } else {
                enableCheatButton();
            }
        }//End Else

        mQuestionNumberTxt.setText(getString(R.string.question_number_txt, mCurrentIndex + 1));

    }//End updateQuestion

    //Check Answers
    private void checkAnswer(boolean userPressedTrue) {

        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId;

        if (mIsCheater) {
            messageResId = R.string.judgement_toast;
        } else {

            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mScore++;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }//End Else

        mQuestionBank[mCurrentIndex].setHasBeenAnswered(true);
        disableAnswerButtons();
        disableCheatButton();
        mCorrectAnswerCounterTxt.setText(("Correct: " + mScore + "/" + mQuestionBank.length));
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

    }//End checkAnswer

    private void enableAnswerButtons() {
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
    }

    private void disableAnswerButtons() {
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
    }

    private void disableCheatButton() {
        mCheatButton.setEnabled(false);
    }

    private void enableCheatButton() {
        mCheatButton.setEnabled(true);
    }

}//End QuizActivity