package com.example.postit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postit.databinding.ActivityMainBinding;
import com.example.postit.model.AudioRecord;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    final int AUDIO_PERMISSION_CODE = 100;
    final int STORAGE_PERMISSION_CODE = 200;


    //뷰바인딩
    private ActivityMainBinding binding;

    //파이어베이스 파이어스토어DB
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //파이어베이스 로그인 인증 객체
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private MainViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkStoragePermission();
        model = new ViewModelProvider(this).get(MainViewModel.class);

        bindView();
        initView();

    }

    void initView(){
        binding.monday.weekDayTextView.setText("월");
        binding.tuesday.weekDayTextView.setText("화");
        binding.wednesday.weekDayTextView.setText("수");
        binding.thursday.weekDayTextView.setText("목");
        binding.friday.weekDayTextView.setText("금");
        binding.saturday.weekDayTextView.setText("토");
        binding.sunday.weekDayTextView.setText("일");

        model.emotionMap.observe(this, new Observer<HashMap<DayOfWeek, Integer>>() {
            @Override
            public void onChanged(HashMap<DayOfWeek, Integer> hm) {
                binding.monday.emotionIconImageView.setImageResource(EmotionIconUtil.getEmotionIcon(hm.getOrDefault(DayOfWeek.MONDAY,4)));
                binding.tuesday.emotionIconImageView.setImageResource(EmotionIconUtil.getEmotionIcon(hm.getOrDefault(DayOfWeek.TUESDAY,4)));
                binding.wednesday.emotionIconImageView.setImageResource(EmotionIconUtil.getEmotionIcon(hm.getOrDefault(DayOfWeek.WEDNESDAY,4)));
                binding.thursday.emotionIconImageView.setImageResource(EmotionIconUtil.getEmotionIcon(hm.getOrDefault(DayOfWeek.THURSDAY,4)));
                binding.friday.emotionIconImageView.setImageResource(EmotionIconUtil.getEmotionIcon(hm.getOrDefault(DayOfWeek.FRIDAY,4)));
                binding.saturday.emotionIconImageView.setImageResource(EmotionIconUtil.getEmotionIcon(hm.getOrDefault(DayOfWeek.SATURDAY,4)));
                binding.sunday.emotionIconImageView.setImageResource(EmotionIconUtil.getEmotionIcon(hm.getOrDefault(DayOfWeek.SUNDAY,4)));
            }
        });

        model.audioFileMap.observe(this, new Observer<HashMap<String, String>>() {
            @Override
            public void onChanged(HashMap<String, String> hm) {
                binding.monday.playButton.setTag(hm.getOrDefault(DayOfWeek.MONDAY.getDisplayName(TextStyle.SHORT, Locale.KOREA),null));
                binding.tuesday.playButton.setTag(hm.getOrDefault(DayOfWeek.TUESDAY.getDisplayName(TextStyle.SHORT, Locale.KOREA),null));
                binding.wednesday.playButton.setTag(hm.getOrDefault(DayOfWeek.WEDNESDAY.getDisplayName(TextStyle.SHORT, Locale.KOREA),null));
                binding.thursday.playButton.setTag(hm.getOrDefault(DayOfWeek.THURSDAY.getDisplayName(TextStyle.SHORT, Locale.KOREA),null));
                binding.friday.playButton.setTag(hm.getOrDefault(DayOfWeek.FRIDAY.getDisplayName(TextStyle.SHORT, Locale.KOREA),null));
                binding.saturday.playButton.setTag(hm.getOrDefault(DayOfWeek.SATURDAY.getDisplayName(TextStyle.SHORT, Locale.KOREA),null));
                binding.sunday.playButton.setTag(hm.getOrDefault(DayOfWeek.SUNDAY.getDisplayName(TextStyle.SHORT, Locale.KOREA),null));
            }
        });

    }


    private void bindView() {
        binding.myPageButton.setOnClickListener((v)->{
            Intent intent = new Intent(this,ProfileActivity.class);
            startActivity(intent);
        });

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


}