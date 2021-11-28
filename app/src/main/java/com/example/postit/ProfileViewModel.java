package com.example.postit;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.postit.model.ParentUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
//원래 있던 로그인화면(안 씀)
public class ProfileViewModel extends ViewModel{
    public MutableLiveData<Boolean> privateInfoSaved = new MutableLiveData<>(false);


    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db= FirebaseFirestore.getInstance();

    public ProfileViewModel(){

    }


    public void checkPrivateInfo(FirebaseUser user){
        db.collection("users").document(auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener((task)->{
                    if(task.isSuccessful()){
                        if(task.getResult().exists()){
                            privateInfoSaved.setValue(true);
                        }else {
                            privateInfoSaved.setValue(false);
                        }
                    }else {
                        privateInfoSaved.setValue(false);
                    }

                    Log.d("what!",privateInfoSaved.getValue().toString());
                });
    }

    public void registerUserInfo(ParentUser user, String key) {
        db.collection("users").document(key)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("upload!!", "Error writing document", e);
                    }
                });
    }
}
