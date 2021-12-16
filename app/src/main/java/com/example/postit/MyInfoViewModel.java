package com.example.postit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.postit.model.AudioRecord;
import com.example.postit.model.ParentUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MyInfoViewModel extends AndroidViewModel {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    MutableLiveData<ParentUser> user = new MutableLiveData<>();

    public MyInfoViewModel(@NonNull Application application) {
        super(application);
    }


    void fetchUserInfo() {
        if (auth.getCurrentUser() == null) return;

        db.collection("users").document(auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener((task) -> {
                    if(task.isSuccessful()){
                        user.setValue(task.getResult().toObject(ParentUser.class));
                    }
                });
    }

}
