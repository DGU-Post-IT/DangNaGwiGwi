package com.example.postit.presentation.history;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.postit.model.AudioRecord;
import com.example.postit.repository.AudioRecordRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
public class HistoryViewModel extends AndroidViewModel {

    FirebaseAuth auth = FirebaseAuth.getInstance();

    MutableLiveData<ArrayList<AudioRecord>> recordData = new MutableLiveData<>(new ArrayList<>());

    AudioRecordRepository repository = new AudioRecordRepository();

    public HistoryViewModel(@NonNull Application application) {
        super(application);
    }

    void fetchRecordHistory() {
        if (auth.getCurrentUser() == null) return;

        repository.getAudioRecordHistory().subscribe((array)->{
            recordData.setValue(array);
        });
    }
}
