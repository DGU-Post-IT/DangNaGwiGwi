package com.example.postit.repository;

import android.util.Log;

import com.example.postit.model.AudioRecord;
import com.example.postit.model.PlantRecord;
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

public class PlantRepository {

    private static final String TAG = "PlantRepository";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public Maybe<PlantRecord> getRecentPlantRecord(){
        return Maybe.create(new MaybeOnSubscribe<PlantRecord>() {
            @Override
            public void subscribe(@NonNull MaybeEmitter<PlantRecord> emitter) throws Throwable {
                db.collection("users").document(auth.getCurrentUser().getUid())
                        .collection("plant").orderBy("time", Query.Direction.DESCENDING)
                        .get()
                        .addOnSuccessListener((querySnapshot) -> {
                            if (querySnapshot.isEmpty()) {
                                emitter.onComplete();
                            }else {
                                PlantRecord pr =null;
                                for (QueryDocumentSnapshot doc : querySnapshot) {
                                    Log.d(TAG, "subscribe: "+Thread.currentThread().getName());
                                    pr = doc.toObject(PlantRecord.class);
                                }
                                emitter.onSuccess(pr);
                            }
                        });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
