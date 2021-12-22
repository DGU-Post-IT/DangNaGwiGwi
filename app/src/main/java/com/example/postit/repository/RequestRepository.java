package com.example.postit.repository;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableEmitter;
import io.reactivex.rxjava3.core.CompletableOnSubscribe;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeEmitter;
import io.reactivex.rxjava3.core.MaybeOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RequestRepository {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public Completable confirmRequest(String email){
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter emitter) throws Throwable {
                //일괄작업으로 시행
                WriteBatch batch = db.batch();
                //request 배열에서 삭제하고 follower 배열에 추가
                DocumentReference ref = db.collection("users").document(auth.getCurrentUser().getUid());
                batch.update(ref,"requested", FieldValue.arrayRemove(email));
                batch.update(ref,"follower",FieldValue.arrayUnion(email));
                batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        emitter.onComplete();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        emitter.onError(e);
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<DocumentSnapshot> fetchRequest(){
        return Maybe.create(new MaybeOnSubscribe<DocumentSnapshot>() {
            @Override
            public void subscribe(@NonNull MaybeEmitter<DocumentSnapshot> emitter) throws Throwable {
                db.collection("users").document(auth.getCurrentUser().getUid())
                        .get()
                        .addOnSuccessListener((docs) -> {
                            if (docs.exists()){
                                emitter.onSuccess(docs);
                            }else{
                                emitter.onComplete();
                            }
                        });
            }
        });
    }


}
