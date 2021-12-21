package com.example.postit.presentation.history;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.postit.databinding.ActivityHistoryBinding;
import com.example.postit.util.QuestionIdUtil;
import com.example.postit.model.AudioRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class HistoryActivity extends AppCompatActivity {

    final int STORAGE_PERMISSION_CODE = 200;

    private static final String TAG = "HistoryAdapter";

    CompositeDisposable cd = new CompositeDisposable();

    MediaPlayer mediaPlayer = null;


    //뷰바인딩
    private ActivityHistoryBinding binding;

    private HistoryViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkStoragePermission();
        model = new ViewModelProvider(this).get(HistoryViewModel.class);

        initRecyclerView();
    }

    void initRecyclerView() {
        RecordHistoryAdapter adapter = new RecordHistoryAdapter(this);
        adapter.setOnButtonClickListener((v,url)->{
            playAudioByUrl(url);
            Log.d(TAG, "initRecyclerView: ");
        });
        binding.recordHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.recordHistoryRecyclerView.setAdapter(adapter);

        model.recordData.observe(this, new Observer<ArrayList<AudioRecord>>() {
            @Override
            public void onChanged(ArrayList<AudioRecord> voiceEmotionRecords) {
                adapter.data = voiceEmotionRecords;
                adapter.notifyDataSetChanged();
                Log.d("recordData changed","changed!!!");
            }
        });
        model.fetchRecordHistory();


    }


    private boolean checkStoragePermission() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            return false;
        }
    }

    void playAudioByUrl(String url) {
        Toast.makeText(this, "버퍼링 ...", Toast.LENGTH_SHORT).show();
        if (url == null) return;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener((player) -> {
            player.start();
        });
        mediaPlayer.setOnCompletionListener((player) -> {
            player.release();
            player = null;
        });
        mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
    }

}
