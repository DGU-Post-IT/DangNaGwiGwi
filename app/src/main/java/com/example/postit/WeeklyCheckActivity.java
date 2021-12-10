package com.example.postit;

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

import com.example.postit.databinding.ActivityWeeklycheckBinding;
import com.example.postit.model.VoiceEmotionRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;

public class WeeklyCheckActivity extends AppCompatActivity {

    final int AUDIO_PERMISSION_CODE = 100;
    final int STORAGE_PERMISSION_CODE = 200;

    MediaPlayer mediaPlayer = null;


    //뷰바인딩
    private ActivityWeeklycheckBinding binding;

    //파이어베이스 파이어스토어DB
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //파이어베이스 로그인 인증 객체
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private HistoryViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWeeklycheckBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkStoragePermission();
        model = new ViewModelProvider(this).get(HistoryViewModel.class);

        initRecyclerView();
        bindView();
    }

    void initRecyclerView() {
        RecordHistoryAdapter adapter = new RecordHistoryAdapter(this);
        binding.recordHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.recordHistoryRecyclerView.setAdapter(adapter);

        model.recordData.observe(this, new Observer<ArrayList<VoiceEmotionRecord>>() {
            @Override
            public void onChanged(ArrayList<VoiceEmotionRecord> voiceEmotionRecords) {
                adapter.data = voiceEmotionRecords;
                adapter.notifyDataSetChanged();
                Log.d("recordData changed","changed!!!");
            }
        });
        model.fetchRecordHistory();


    }


    private void bindView() {

    }


    // 오디오 파일 권한 체크
    private boolean checkAudioPermission() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_PERMISSION_CODE);
            return false;
        }
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

    void playAudioByFile(int questionId) {
        if(questionId==-1){
            Toast.makeText(this, "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
        }

        mediaPlayer = MediaPlayer.create(this, QuestionIdUtil.getAudioId(questionId));

//        try {
//            mediaPlayer.setDataSource(url);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        mediaPlayer.setOnPreparedListener((player) -> {
            player.start();
        });
        mediaPlayer.setOnCompletionListener((player) -> {
            player.release();
            player = null;
        });
    }
}
