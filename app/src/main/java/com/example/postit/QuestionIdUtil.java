package com.example.postit;

public class QuestionIdUtil {
    public static int getAudioId(int questionId){
        switch(questionId){
            case 0:
                return R.raw.postit_1;
            case 1:
                return R.raw.postit_2;
            case 2:
                return R.raw.postit_3;
            case 3:
                return R.raw.postit_4;
            case 4:
                return R.raw.postit_5;

            default:
                return R.raw.good_morning;
        }
    }
}
