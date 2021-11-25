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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
//원래 있던 로그인화면(안 씀)
public class ProfileViewModel extends ViewModel{
    public MutableLiveData<Boolean> loggedIn = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> privateInfoSaved = new MutableLiveData<>(false);
    public MutableLiveData<String> buttonText = new MutableLiveData<>("로그인");
    public MutableLiveData<String> emailText = new MutableLiveData<>("로그인 되어 있지 않습니다.");


    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db= FirebaseFirestore.getInstance();

    public ProfileViewModel(){
        loggedIn.setValue(auth.getCurrentUser() != null);
        if (loggedIn.getValue()){
            buttonText.setValue("로그아웃");
            emailText.setValue(auth.getCurrentUser().getEmail());
            checkPrivateInfo();
        }

    }


    public void checkPrivateInfo(){
        loggedIn.setValue(auth.getCurrentUser() != null);
        db.collection("users").document(auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener((task)->{
                    Log.d("what!",loggedIn.getValue().toString());
                    Log.d("what!",privateInfoSaved.getValue().toString());
                    if(task.isSuccessful()){
                        if(task.getResult().exists()){
                            privateInfoSaved.setValue(true);
                        }else {
                            privateInfoSaved.setValue(false);
                        }
                    }else {
                        privateInfoSaved.setValue(false);
                    }

                    Log.d("what!",loggedIn.getValue().toString());
                    Log.d("what!",privateInfoSaved.getValue().toString());
                });
    }

    public void registerUserInfo(ParentUser user, String key) {
        db.collection("users").document(key)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("upload!!", "DocumentSnapshot successfully written!");
                        checkPrivateInfo();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("upload!!", "Error writing document", e);
                    }
                });
    }

    public void logout(){
        auth.signOut();
        loggedIn.setValue(false);
        buttonText.setValue("로그인");
        emailText.setValue("로그인되어 있지 않습니다.");
    }




}
