package com.example.postit.presentation.plant;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.postit.model.PlantRecord;
import com.example.postit.repository.EmotionRepository;
import com.example.postit.repository.PlantRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDate;

public class PlantViewModel extends AndroidViewModel {

    MutableLiveData<PlantRecord> plantRecord = new MutableLiveData<>(null);

    FirebaseAuth auth = FirebaseAuth.getInstance();

    PlantRepository plantRepository = new PlantRepository();

    public PlantViewModel(@NonNull Application application) {
        super(application);
    }
    void fetchPlantRecord(){
        if (auth.getCurrentUser() == null) return;
        plantRepository.getRecentPlantRecord().subscribe(plantRecord1 -> {
            plantRecord.setValue(plantRecord1);
        });

    }
}
