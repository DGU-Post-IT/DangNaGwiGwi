package com.example.postit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.postit.model.AudioRecord;
import com.example.postit.model.EmotionRecord;
import com.example.postit.model.VoiceEmotionRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

public class HistoryViewModel extends AndroidViewModel {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    MutableLiveData<ArrayList<VoiceEmotionRecord>> recordData = new MutableLiveData<>(new ArrayList<>());

    public HistoryViewModel(@NonNull Application application) {
        super(application);
    }

    void fetchRecordHistory() {
        if (auth.getCurrentUser() == null) return;
        ArrayList<VoiceEmotionRecord> temp = recordData.getValue();
        db.collection("users").document(auth.getCurrentUser().getUid())
                .collection("audioFile").orderBy("time", Query.Direction.DESCENDING).limit(15)
                .get()
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            AudioRecord ar = doc.toObject(AudioRecord.class);
                            VoiceEmotionRecord ver = new VoiceEmotionRecord();
                            ver.setDate(ar.getTime());
                            ver.setAudioUrl(ar.getAudioLocation());
                            ver.setEmotion(1);
                            temp.add(ver);
                        }
                        recordData.setValue(temp);
                    }
                });

    }
}
