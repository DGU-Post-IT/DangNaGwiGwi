package com.example.postit;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;

import com.example.postit.model.AudioRecord;
import com.example.postit.model.EmotionRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.ZoneId;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainViewModel extends AndroidViewModel {

    final DayOfWeek firstDayOfWeek = WeekFields.of(Locale.KOREA).getFirstDayOfWeek();
    final DayOfWeek lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.getValue() + 6) % DayOfWeek.values().length) + 1);

    MutableLiveData<HashMap<String,String>> audioFileMap = new MutableLiveData<HashMap<String, String>>(new HashMap<>());
    MutableLiveData<Integer[]> emotionCount = new MutableLiveData<>(new Integer[]{0,0,0,0});
    MutableLiveData<HashMap<DayOfWeek,Integer>> emotionMap = new MutableLiveData<HashMap<DayOfWeek, Integer>>(new HashMap<>());
    MutableLiveData<Integer> mondayQuestionId = new MutableLiveData<>(-1);
    MutableLiveData<Integer> tuesdayQuestionId = new MutableLiveData<>(-1);
    MutableLiveData<Integer> wednesdayQuestionId = new MutableLiveData<>(-1);
    MutableLiveData<Integer> thursdayQuestionId = new MutableLiveData<>(-1);
    MutableLiveData<Integer> fridayQuestionId = new MutableLiveData<>(-1);
    MutableLiveData<Integer> saturdayQuestionId = new MutableLiveData<>(-1);
    MutableLiveData<Integer> sundayQuestionId = new MutableLiveData<>(-1);

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    LocalDate date;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.date = LocalDate.now();
        fetchWeeklyData();
    }

    void fetchWeeklyData() {
        if (auth.getCurrentUser() == null) return;

        Log.d("mainviewmodel", "fetch data" + "");
        db.collection("users").document(auth.getCurrentUser().getUid())
                .collection("emotionRecord")
                .whereLessThanOrEqualTo("time", Date.from(date.with(TemporalAdjusters.nextOrSame(lastDayOfWeek)).atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant()))
                .whereGreaterThanOrEqualTo("time", Date.from(date.with(TemporalAdjusters.previousOrSame(firstDayOfWeek)).atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant()))
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener((task) -> {
                    HashMap<DayOfWeek,Integer> emotionMap2 = new HashMap<>();
                    Integer[] arr = new Integer[4];
                    Arrays.fill(arr, 0);
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc :
                                task.getResult()) {
                            EmotionRecord er = doc.toObject(EmotionRecord.class);
                            LocalDate localDate = er.getTime().toInstant() // Date -> Instant
                                    .atZone(ZoneId.systemDefault()) // Instant -> ZonedDateTime
                                    .toLocalDate();
                            emotionMap2.put(localDate.getDayOfWeek(),er.getEmotion());
                            arr[er.getEmotion()]++;

                        }
                        this.emotionMap.setValue(emotionMap2);
                        Log.d("data","setting value");
                        this.emotionCount.setValue(arr);
                    }
                });
        db.collection("users").document(auth.getCurrentUser().getUid())
                .collection("audioFile")
                .whereLessThanOrEqualTo("time", Date.from(date.with(TemporalAdjusters.nextOrSame(lastDayOfWeek)).atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant()))
                .whereGreaterThanOrEqualTo("time", Date.from(date.with(TemporalAdjusters.previousOrSame(firstDayOfWeek)).atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant()))
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener((task) -> {

                    HashMap<String,String> audioMap = new HashMap<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc :
                                task.getResult()) {
                            AudioRecord af = doc.toObject(AudioRecord.class);
                            LocalDate localDate = af.getTime().toInstant() // Date -> Instant
                                    .atZone(ZoneId.systemDefault()) // Instant -> ZonedDateTime
                                    .toLocalDate();

                            Log.d("mainviewmodel",localDate.toString());
                            DayOfWeek dow = localDate.getDayOfWeek();
                            if(dow==DayOfWeek.MONDAY){
                                mondayQuestionId.setValue(af.getQuestionId());
                            }else if(dow==DayOfWeek.TUESDAY){
                                tuesdayQuestionId.setValue(af.getQuestionId());
                            }else if(dow==DayOfWeek.WEDNESDAY){
                                wednesdayQuestionId.setValue(af.getQuestionId());
                            }else if(dow==DayOfWeek.THURSDAY){
                                thursdayQuestionId.setValue(af.getQuestionId());
                            }else if(dow==DayOfWeek.FRIDAY){
                                fridayQuestionId.setValue(af.getQuestionId());
                            }else if(dow==DayOfWeek.SATURDAY){
                                saturdayQuestionId.setValue(af.getQuestionId());
                            }else if(dow==DayOfWeek.SUNDAY){
                                sundayQuestionId.setValue(af.getQuestionId());
                            }
                            audioMap.put(localDate.getDayOfWeek().getDisplayName(TextStyle.SHORT,Locale.KOREA),af.getAudioLocation());
                        }
                        audioFileMap.setValue(audioMap);
                    }
                });




    }

}
