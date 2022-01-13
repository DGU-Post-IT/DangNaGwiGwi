package com.example.postit.presentation.main;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.time.DayOfWeek;
import java.time.LocalDate;

import com.example.postit.model.PlantRecord;
import com.example.postit.repository.EmotionRepository;
import com.example.postit.repository.PlantRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.Locale;

import io.reactivex.rxjava3.functions.Consumer;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "MainViewModel";

    final DayOfWeek firstDayOfWeek = WeekFields.of(Locale.KOREA).getFirstDayOfWeek();
    final DayOfWeek lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.getValue() + 6) % DayOfWeek.values().length) + 1);

    MutableLiveData<Integer[]> emotionCount = new MutableLiveData<>(new Integer[]{0,0,0,0});
    MutableLiveData<PlantRecord> plantRecord = new MutableLiveData<>(null);

    FirebaseAuth auth = FirebaseAuth.getInstance();
    
    EmotionRepository emotionRepository = new EmotionRepository();
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
        
//        db.collection("users").document(auth.getCurrentUser().getUid())
//                .collection("emotionRecord")
//                .whereLessThanOrEqualTo("time", Date.from(date.with(TemporalAdjusters.nextOrSame(lastDayOfWeek)).atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant()))
//                .whereGreaterThanOrEqualTo("time", Date.from(date.with(TemporalAdjusters.previousOrSame(firstDayOfWeek)).atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant()))
//                .orderBy("time", Query.Direction.DESCENDING)
//                .get()
//                .addOnCompleteListener((task) -> {
//                    HashMap<DayOfWeek,Integer> emotionMap2 = new HashMap<>();
//                    Integer[] arr = new Integer[4];
//                    Arrays.fill(arr, 0);
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot doc :
//                                task.getResult()) {
//                            EmotionRecord er = doc.toObject(EmotionRecord.class);
//                            LocalDate localDate = er.getTime().toInstant() // Date -> Instant
//                                    .atZone(ZoneId.systemDefault()) // Instant -> ZonedDateTime
//                                    .toLocalDate();
//                            emotionMap2.put(localDate.getDayOfWeek(),er.getEmotion());
//                            arr[er.getEmotion()]++;
//
//                        }
//                        this.emotionMap.setValue(emotionMap2);
//                        Log.d("data","setting value");
//                        this.emotionCount.setValue(arr);
//                    }
//                });
//        db.collection("users").document(auth.getCurrentUser().getUid())
//                .collection("audioFile")
//                .whereLessThanOrEqualTo("time", Date.from(date.with(TemporalAdjusters.nextOrSame(lastDayOfWeek)).atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant()))
//                .whereGreaterThanOrEqualTo("time", Date.from(date.with(TemporalAdjusters.previousOrSame(firstDayOfWeek)).atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant()))
//                .orderBy("time", Query.Direction.DESCENDING)
//                .get()
//                .addOnCompleteListener((task) -> {
//
//                    HashMap<String,String> audioMap = new HashMap<>();
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot doc :
//                                task.getResult()) {
//                            AudioRecord af = doc.toObject(AudioRecord.class);
//                            LocalDate localDate = af.getTime().toInstant() // Date -> Instant
//                                    .atZone(ZoneId.systemDefault()) // Instant -> ZonedDateTime
//                                    .toLocalDate();
//
//                            Log.d("mainviewmodel",localDate.toString());
//                            DayOfWeek dow = localDate.getDayOfWeek();
//                            if(dow==DayOfWeek.MONDAY){
//                                mondayQuestionId.setValue(af.getQuestionId());
//                            }else if(dow==DayOfWeek.TUESDAY){
//                                tuesdayQuestionId.setValue(af.getQuestionId());
//                            }else if(dow==DayOfWeek.WEDNESDAY){
//                                wednesdayQuestionId.setValue(af.getQuestionId());
//                            }else if(dow==DayOfWeek.THURSDAY){
//                                thursdayQuestionId.setValue(af.getQuestionId());
//                            }else if(dow==DayOfWeek.FRIDAY){
//                                fridayQuestionId.setValue(af.getQuestionId());
//                            }else if(dow==DayOfWeek.SATURDAY){
//                                saturdayQuestionId.setValue(af.getQuestionId());
//                            }else if(dow==DayOfWeek.SUNDAY){
//                                sundayQuestionId.setValue(af.getQuestionId());
//                            }
//                            audioMap.put(localDate.getDayOfWeek().getDisplayName(TextStyle.SHORT,Locale.KOREA),af.getAudioLocation());
//                        }
//                        audioFileMap.setValue(audioMap);
//                    }
//                });
    }

    void fetchPlantRecord(){
        if (auth.getCurrentUser() == null) return;
        plantRepository.getRecentPlantRecord().subscribe(plantRecord1 -> {
            plantRecord.setValue(plantRecord1);
        });

    }

}
