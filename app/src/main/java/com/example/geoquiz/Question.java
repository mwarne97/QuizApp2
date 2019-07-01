package com.example.geoquiz;

public class Question {

    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mHasBeenAnswered;
    private boolean mHasPressedCheat;

    public Question(int textResId, boolean answerTrue, boolean hasBeenAnswered, boolean hasPressedCheat){
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
        mHasBeenAnswered = hasBeenAnswered;
        mHasPressedCheat = hasPressedCheat;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public void setHasBeenAnswered(boolean hasBeenAnswered){
        mHasBeenAnswered = hasBeenAnswered;
    }

    public boolean HasBeenAnswered(){
        return mHasBeenAnswered;
    }

    public void setHasPressedCheat(boolean hasPressedCheat){
        mHasPressedCheat = hasPressedCheat;
    }

    public boolean HasPressedCheat(){
        return mHasPressedCheat;
    }
}
