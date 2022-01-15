package com.example.postit.util;

import com.example.postit.R;

public class QuestionIdUtil {
    public static int getAudioId(int questionId){
        switch(questionId){
            case 0:
                return R.raw.postit_0;
            case 1:
                return R.raw.postit_1;
            case 2:
                return R.raw.postit_2;
            case 3:
                return R.raw.postit_3;
            case 4:
                return R.raw.postit_4;
            case 5:
                return R.raw.postit_5;
            case 6:
                return R.raw.postit_6;
            case 7:
                return R.raw.postit_7;
            case 8:
                return R.raw.postit_8;
            case 9:
                return R.raw.postit_9;

            default:
                return R.raw.postit_10;
        }
    }
}
