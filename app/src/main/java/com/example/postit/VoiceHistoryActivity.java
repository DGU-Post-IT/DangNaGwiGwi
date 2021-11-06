package com.example.postit;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postit.databinding.ActivityVoiceHistoryBinding;
import com.example.postit.model.AudioRecord;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;

public class VoiceHistoryActivity extends AppCompatActivity {
    private ActivityVoiceHistoryBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    VoiceHistoryAdapter adapter = null;

    MediaPlayer mediaPlayer = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVoiceHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRecyclerView();

        getListOfVoiceHistory();






    }

    private void initRecyclerView() {
        adapter = new VoiceHistoryAdapter(this,new ArrayList<>());
        adapter.setOnItemClickListener((v,pos,location)->{
            Toast.makeText(this,"버퍼링 ...",Toast.LENGTH_SHORT).show();

            String url = location;

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            try {
                mediaPlayer.setDataSource(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.setOnPreparedListener((player)->{
                player.start();
            });
            mediaPlayer.setOnCompletionListener((player)->{
                player.release();
                player = null;
            });
            mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
        });
        binding.voiceHistoryRecyclerView.setAdapter(adapter);
        binding.voiceHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
    }

    private void getListOfVoiceHistory(){
        db.collection("audioFile")
                .whereEqualTo("speakerID", auth.getCurrentUser().getUid())
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("TAG", document.getId() + " => " + document.getData());
                        AudioRecord ar = document.toObject(AudioRecord.class);
                        adapter.addItem(ar.getAudioLocation());
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });


    }




}
