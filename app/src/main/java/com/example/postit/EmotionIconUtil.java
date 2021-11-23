package com.example.postit;

public class EmotionIconUtil {
    static int getEmotionIcon(int emotion) {
        switch (emotion) {
            case 0:
                return R.drawable.ic_weather_good_shadow;
            case 1:
                return R.drawable.ic_weather_soso_shadow;
            case 2:
                return R.drawable.ic_weather_angry_shadow;
            case 3:
                return R.drawable.ic_weather_sad_shadow;
            default:
                return R.drawable.ic_baseline_not_interested_24;

        }


    }
}
