package com.example.postit.presentation.weeklyemotion;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.postit.model.AudioRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
public class HistoryViewModel extends AndroidViewModel {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    MutableLiveData<ArrayList<AudioRecord>> recordData = new MutableLiveData<>(new ArrayList<>());

    public HistoryViewModel(@NonNull Application application) {
        super(application);
    }

    void fetchRecordHistory() {
        if (auth.getCurrentUser() == null) return;
        ArrayList<AudioRecord> temp = recordData.getValue();
        db.collection("users").document(auth.getCurrentUser().getUid())
                .collection("audioFile").orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            AudioRecord ar = doc.toObject(AudioRecord.class);
                            temp.add(ar);
                        }
                        recordData.setValue(temp);
                    }
                });

    }
}
