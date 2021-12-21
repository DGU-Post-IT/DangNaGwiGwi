package com.example.postit.repository;

import android.util.Log;

import com.example.postit.model.AudioRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeEmitter;
import io.reactivex.rxjava3.core.MaybeOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AudioRecordRepository {

    private static final String TAG = "AudioRecordRepository";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public Maybe<ArrayList<AudioRecord>> getAudioRecordHistory(){
        return Maybe.create(new MaybeOnSubscribe<ArrayList<AudioRecord>>() {
            @Override
            public void subscribe(@NonNull MaybeEmitter<ArrayList<AudioRecord>> emitter) throws Throwable {
                db.collection("users").document(auth.getCurrentUser().getUid())
                        .collection("audioFile").orderBy("time", Query.Direction.DESCENDING)
                        .get()
                        .addOnSuccessListener((querySnapshot) -> {
                            if (querySnapshot.isEmpty()) {
                                emitter.onComplete();
                            }else {
                                ArrayList<AudioRecord> temp = new ArrayList<>();
                                for (QueryDocumentSnapshot doc : querySnapshot) {
                                    Log.d(TAG, "subscribe: "+Thread.currentThread().getName());
                                    AudioRecord ar = doc.toObject(AudioRecord.class);
                                    temp.add(ar);
                                }
                                emitter.onSuccess(temp);
                            }
                        });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
