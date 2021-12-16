package com.example.postit.repository;

import com.example.postit.model.EmotionRecord;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;

public class EmotionRepository {

    FirebaseFirestore db  = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    final DayOfWeek firstDayOfWeek = WeekFields.of(Locale.KOREA).getFirstDayOfWeek();
    final DayOfWeek lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.getValue() + 6) % DayOfWeek.values().length) + 1);


    public HashMap<DayOfWeek,Integer> getEmotionRecord7Days(){
        LocalDate date = LocalDate.now();

        HashMap<DayOfWeek,Integer> emotionMap2 = new HashMap<>();

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

                            emotionMap2.put(localDate.getDayOfWeek(), er.getEmotion());
                        }

                    }
                });

        return emotionMap2;
    }

//    public Observable<Task<QuerySnapshot>> getWeeklyEmotion(){
//        LocalDate date = LocalDate.now();
//
//
//        db.collection("users").document(auth.getCurrentUser().getUid())
//                .collection("emotionRecord")
//                .whereLessThanOrEqualTo("time", Date.from(date.with(TemporalAdjusters.nextOrSame(lastDayOfWeek)).atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant()))
//                .whereGreaterThanOrEqualTo("time", Date.from(date.with(TemporalAdjusters.previousOrSame(firstDayOfWeek)).atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant()))
//                .orderBy("time", Query.Direction.DESCENDING)
//                .get();
//
//
//    }

}
