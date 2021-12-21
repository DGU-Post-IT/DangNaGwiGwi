package com.example.postit.repository;

import android.database.Observable;
import android.util.Log;

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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeEmitter;
import io.reactivex.rxjava3.core.MaybeOnSubscribe;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EmotionRepository {
    private static final String TAG = "EmotionRepository";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    final DayOfWeek firstDayOfWeek = WeekFields.of(Locale.KOREA).getFirstDayOfWeek();
    final DayOfWeek lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.getValue() + 6) % DayOfWeek.values().length) + 1);

    public Maybe<Integer[]> getWeeklyEmotion(QuerySnapshot querySnapshot) {

        return Maybe.create(new MaybeOnSubscribe<Integer[]>() {
            @Override
            public void subscribe(@NonNull MaybeEmitter<Integer[]> emitter) throws Throwable {
//                HashMap<DayOfWeek, Integer> emotionMap2 = new HashMap<>();
                Integer[] arr = new Integer[4];
                    Arrays.fill(arr, 0);
                for (QueryDocumentSnapshot doc :
                        querySnapshot) {
                    EmotionRecord er = doc.toObject(EmotionRecord.class);
                    Log.d(TAG, "subscribe: "+er.getTime()+" "+er.getEmotion());
                    LocalDate localDate = er.getTime().toInstant() // Date -> Instant
                            .atZone(ZoneId.systemDefault()) // Instant -> ZonedDateTime
                            .toLocalDate();
//                    emotionMap2.put(localDate.getDayOfWeek(), er.getEmotion());
                    arr[er.getEmotion()]++;
                }
                emitter.onSuccess(arr);
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());


    }

    public Maybe<QuerySnapshot> getWeeklyEmotionFromDb() {
        LocalDate date = LocalDate.now();

        return Maybe.create(new MaybeOnSubscribe<QuerySnapshot>() {
            @Override
            public void subscribe(@NonNull MaybeEmitter<QuerySnapshot> emitter) throws Throwable {
                Log.d(TAG, "subscribe: " + Thread.currentThread().getName());
                Task task = db.collection("users").document(auth.getCurrentUser().getUid())
                        .collection("emotionRecord")
                        .whereLessThanOrEqualTo("time", Date.from(date.with(TemporalAdjusters.nextOrSame(lastDayOfWeek)).atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant()))
                        .whereGreaterThanOrEqualTo("time", Date.from(date.with(TemporalAdjusters.previousOrSame(firstDayOfWeek)).atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant()))
                        .orderBy("time", Query.Direction.DESCENDING)
                        .get().addOnSuccessListener((documentSnapshots -> {
                            if (documentSnapshots.isEmpty()) {
                                emitter.onComplete();
                            } else {
                                emitter.onSuccess(documentSnapshots);
                            }
                        }));
            }
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.computation());
    }

}
