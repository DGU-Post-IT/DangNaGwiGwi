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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainViewModel extends AndroidViewModel {

    final DayOfWeek firstDayOfWeek = WeekFields.of(Locale.KOREA).getFirstDayOfWeek();
    final DayOfWeek lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.getValue() + 6) % DayOfWeek.values().length) + 1);

    MutableLiveData<HashMap<String,String>> audioFileMap = new MutableLiveData<HashMap<String, String>>(new HashMap<>());
    MutableLiveData<HashMap<DayOfWeek,Integer>> emotionMap = new MutableLiveData<HashMap<DayOfWeek, Integer>>(new HashMap<>());

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
        HashMap<String,String> audioMap = new HashMap<>();
        HashMap<DayOfWeek,Integer> emotionMap2 = new HashMap<>();

        Log.d("mainviewmodel", "fetch data" + "");
        db.collection("users").document(auth.getCurrentUser().getUid())
                .collection("emotionRecord")
                .whereLessThanOrEqualTo("time", Date.from(date.with(TemporalAdjusters.nextOrSame(lastDayOfWeek)).atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant()))
                .whereGreaterThanOrEqualTo("time", Date.from(date.with(TemporalAdjusters.previousOrSame(firstDayOfWeek)).atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant()))
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc :
                                task.getResult()) {
                            EmotionRecord er = doc.toObject(EmotionRecord.class);
                            LocalDate localDate = er.getTime().toInstant() // Date -> Instant
                                    .atZone(ZoneId.systemDefault()) // Instant -> ZonedDateTime
                                    .toLocalDate();
                            emotionMap2.put(localDate.getDayOfWeek(),er.getEmotion());

                        }
                        this.emotionMap.setValue(emotionMap2);
                    }
                });
        db.collection("users").document(auth.getCurrentUser().getUid())
                .collection("audioFile")
                .whereLessThanOrEqualTo("time", Date.from(date.with(TemporalAdjusters.nextOrSame(lastDayOfWeek)).atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant()))
                .whereGreaterThanOrEqualTo("time", Date.from(date.with(TemporalAdjusters.previousOrSame(firstDayOfWeek)).atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant()))
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc :
                                task.getResult()) {
                            AudioRecord af = doc.toObject(AudioRecord.class);
                            LocalDate localDate = af.getTime().toInstant() // Date -> Instant
                                    .atZone(ZoneId.systemDefault()) // Instant -> ZonedDateTime
                                    .toLocalDate();
                            audioMap.put(localDate.getDayOfWeek().getDisplayName(TextStyle.SHORT,Locale.KOREA),af.getAudioLocation());

                            Log.d("mainviewmodel", localDate.getDayOfWeek().getDisplayName(TextStyle.SHORT,Locale.KOREA));
                        }
                        audioFileMap.setValue(audioMap);
                    }
                });




    }

}
