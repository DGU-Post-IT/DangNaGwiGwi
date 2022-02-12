package com.example.postit.util;

import com.example.postit.R;

public class EmotionIconUtil {
    public static int getEmotionIcon(int emotion) {
        switch (emotion) {
            case 0:
                return R.drawable.ic_weather_good_shadow;
            case 1:
                return R.drawable.ic_weather_sad_shadow;
            case 2:
                return R.drawable.ic_weather_angry_shadow;
            case 3:
                return R.drawable.ic_weather_anxiety_shadow;
            case 4:
                return R.drawable.ic_weather_sad_shadow;
            case 5:
                return R.drawable.ic_weather_sad_shadow;
            default:
                return R.drawable.ic_baseline_not_interested_24;

        }


    }
}
