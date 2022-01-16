package com.example.postit.presentation.main;

import android.app.Application;
import android.icu.util.Calendar;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.time.DayOfWeek;
import java.time.LocalDate;

import com.example.postit.model.AudioRecord;
import com.example.postit.model.PlantRecord;
import com.example.postit.repository.AudioRecordRepository;
import com.example.postit.repository.EmotionRepository;
import com.example.postit.repository.PlantRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import io.reactivex.rxjava3.functions.Consumer;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "MainViewModel";

    final DayOfWeek firstDayOfWeek = WeekFields.of(Locale.KOREA).getFirstDayOfWeek();
    final DayOfWeek lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.getValue() + 6) % DayOfWeek.values().length) + 1);

    MutableLiveData<Integer[]> emotionCount = new MutableLiveData<>(new Integer[]{0,0,0,0});
    MutableLiveData<PlantRecord> plantRecord = new MutableLiveData<>(null);
    MutableLiveData<AudioRecord> audioRecord = new MutableLiveData<>(null);

    FirebaseAuth auth = FirebaseAuth.getInstance();
    
    EmotionRepository emotionRepository = new EmotionRepository();
    AudioRecordRepository audioRecordRepository = new AudioRecordRepository();
    PlantRepository plantRepository = new PlantRepository();

    LocalDate date;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.date = LocalDate.now();
    }

    void fetchWeeklyData() {
        if (auth.getCurrentUser() == null) return;

        Log.d("mainviewmodel", "fetch data" + "");
        emotionRepository.getWeeklyEmotionFromDb().subscribe(new Consumer<QuerySnapshot>() {
            @Override
            public void accept(QuerySnapshot queryDocumentSnapshots) throws Throwable {
                Log.d(TAG, "accept: from main view model"+Thread.currentThread().getName());
                emotionRepository.getWeeklyEmotion(queryDocumentSnapshots).subscribe(new Consumer<Integer[]>() {
                    @Override
                    public void accept(Integer[] newEmotionArray) throws Throwable {
                        Log.d(TAG, "accept: set Value of live data");
                        emotionCount.setValue(newEmotionArray);
                    }
                });
            }
        });
    }

    void fetchPlantRecord(){
        if (auth.getCurrentUser() == null) return;
        plantRepository.getRecentPlantRecord().subscribe(plantRecord1 -> {
            plantRecord.setValue(plantRecord1);
        });

    }

    void fetchOneAudioRecord(){
        if (auth.getCurrentUser() == null) return;
        audioRecordRepository.getOneAudioRecordHistory().subscribe((ar)->{
            if(ar==null) return;
            Log.d(TAG, "fetchOneAudioRecord: ar incoming");
            Calendar cal = Calendar.getInstance();
            Date nowDate = cal.getTime();
            Log.d(TAG, "fetchOneAudioRecord: "+ar.getTime().getMonth());
            Log.d(TAG, "fetchOneAudioRecord: "+nowDate.getMonth());
            if(ar.getTime().getMonth()==nowDate.getMonth()&&ar.getTime().getYear()==nowDate.getYear()&&ar.getTime().getDate()==nowDate.getDate()){
                Log.d(TAG, "fetchOneAudioRecord: ar is today");
                audioRecord.setValue(ar);
            }
        });

    }

}
