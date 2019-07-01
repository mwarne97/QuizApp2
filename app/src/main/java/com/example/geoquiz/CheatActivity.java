package com.example.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.example.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.example.geoquiz.answer_shown";
    private static final String EXTRA_ANSWER_SHOW = "com.example.geoquiz.answer_show";

    private boolean mAnswerIsTrue;

    private boolean mAnswerSeen;

    Button mShowAnswerButton;
    TextView mAnswerTextView;

    //Tells the activity what the answer is
    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intention = new Intent(packageContext, CheatActivity.class);
        intention.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intention;
    }//End newIntent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null) {
            mAnswerSeen = savedInstanceState.getBoolean(EXTRA_ANSWER_SHOWN, false);
        }//End If

        //This grabs what the answer is for the particular question
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        mShowAnswerButton = findViewById(R.id.show_answer_button);

        //If the answer has already been seen
        if (!mAnswerSeen) {
            mShowAnswerButton.setEnabled(true);
        }//End If
        else {
            mShowAnswerButton.setEnabled(false);
            if (mAnswerIsTrue) {
                mAnswerTextView.setText(R.string.true_button);
            } else {
                mAnswerTextView.setText(R.string.false_button);
            }//End Else
        }//End Else

        //Logic For Button
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                setAnswerShownResult(true);
                mAnswerSeen = true;
                mShowAnswerButton.setEnabled(false);
            }//End onClick
        });
    }//End onCreate

    //Used for up button in toolbar
    //Used the same way as the back button from the system
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }//End Switch
        return super.onOptionsItemSelected(item);
    }//End onOptionsItemSelected

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_ANSWER_SHOWN, mAnswerSeen);
    }//End onSaveInstanceState

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOW, isAnswerShown);
        setResult(RESULT_OK, data);
    }//End setAnswerShownResult

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOW, false);
    }//End wasAnswerShown

}//End CheatActivity