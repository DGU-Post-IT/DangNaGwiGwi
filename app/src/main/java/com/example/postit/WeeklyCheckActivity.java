package com.example.postit;

import android.Manifest;
import android.content.Intent;
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

import com.example.postit.EmotionIconUtil;
import com.example.postit.KoreanTime;
import com.example.postit.MainViewModel;
import com.example.postit.ProfileActivity;
import com.example.postit.QuestionActivity;
import com.example.postit.QuestionIdUtil;
import com.example.postit.databinding.ActivityWeeklycheckBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;

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

    private MainViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWeeklycheckBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkStoragePermission();
        model = new ViewModelProvider(this).get(MainViewModel.class);

        bindView();
        initView();

        int today_idx = (int) ((KoreanTime.koreaToday()) % 5);
        Log.d("TAG", today_idx + "");
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra("questionId", today_idx);
        binding.questionButton.setOnClickListener((v) -> {
            startActivity(intent);
        });

    }

    void initView() {
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
                binding.monday.emotionIconImageView.setImageResource(EmotionIconUtil.getEmotionIcon(hm.getOrDefault(DayOfWeek.MONDAY, 4)));
                binding.tuesday.emotionIconImageView.setImageResource(EmotionIconUtil.getEmotionIcon(hm.getOrDefault(DayOfWeek.TUESDAY, 4)));
                binding.wednesday.emotionIconImageView.setImageResource(EmotionIconUtil.getEmotionIcon(hm.getOrDefault(DayOfWeek.WEDNESDAY, 4)));
                binding.thursday.emotionIconImageView.setImageResource(EmotionIconUtil.getEmotionIcon(hm.getOrDefault(DayOfWeek.THURSDAY, 4)));
                binding.friday.emotionIconImageView.setImageResource(EmotionIconUtil.getEmotionIcon(hm.getOrDefault(DayOfWeek.FRIDAY, 4)));
                binding.saturday.emotionIconImageView.setImageResource(EmotionIconUtil.getEmotionIcon(hm.getOrDefault(DayOfWeek.SATURDAY, 4)));
                binding.sunday.emotionIconImageView.setImageResource(EmotionIconUtil.getEmotionIcon(hm.getOrDefault(DayOfWeek.SUNDAY, 4)));
            }
        });

        model.audioFileMap.observe(this, new Observer<HashMap<String, String>>() {
            @Override
            public void onChanged(HashMap<String, String> hm) {
                binding.monday.playButton.setTag(hm.getOrDefault(DayOfWeek.MONDAY.getDisplayName(TextStyle.SHORT, Locale.KOREA), null));
                binding.tuesday.playButton.setTag(hm.getOrDefault(DayOfWeek.TUESDAY.getDisplayName(TextStyle.SHORT, Locale.KOREA), null));
                binding.wednesday.playButton.setTag(hm.getOrDefault(DayOfWeek.WEDNESDAY.getDisplayName(TextStyle.SHORT, Locale.KOREA), null));
                binding.thursday.playButton.setTag(hm.getOrDefault(DayOfWeek.THURSDAY.getDisplayName(TextStyle.SHORT, Locale.KOREA), null));
                binding.friday.playButton.setTag(hm.getOrDefault(DayOfWeek.FRIDAY.getDisplayName(TextStyle.SHORT, Locale.KOREA), null));
                binding.saturday.playButton.setTag(hm.getOrDefault(DayOfWeek.SATURDAY.getDisplayName(TextStyle.SHORT, Locale.KOREA), null));
                binding.sunday.playButton.setTag(hm.getOrDefault(DayOfWeek.SUNDAY.getDisplayName(TextStyle.SHORT, Locale.KOREA), null));
            }
        });

        model.mondayQuestionId.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.monday.playButtonQuestion.setTag(integer);
            }
        });
        model.tuesdayQuestionId.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.tuesday.playButtonQuestion.setTag(integer);
            }
        });
        model.wednesdayQuestionId.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.wednesday.playButtonQuestion.setTag(integer);
            }
        });
        model.thursdayQuestionId.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.thursday.playButtonQuestion.setTag(integer);
            }
        });
        model.fridayQuestionId.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.friday.playButtonQuestion.setTag(integer);
            }
        });
        model.saturdayQuestionId.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.saturday.playButtonQuestion.setTag(integer);
            }
        });
        model.sundayQuestionId.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.sunday.playButtonQuestion.setTag(integer);
            }
        });

    }


    private void bindView() {
        binding.myPageButton.setOnClickListener((v) -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });
        binding.monday.playButtonQuestion.setOnClickListener((v) -> {
            playAudioByFile((int) v.getTag());
        });
        binding.tuesday.playButtonQuestion.setOnClickListener((v) -> {
            playAudioByFile((int) v.getTag());
        });
        binding.wednesday.playButtonQuestion.setOnClickListener((v) -> {
            playAudioByFile((int) v.getTag());
        });
        binding.thursday.playButtonQuestion.setOnClickListener((v) -> {
            playAudioByFile((int) v.getTag());
        });
        binding.friday.playButtonQuestion.setOnClickListener((v) -> {
            playAudioByFile((int) v.getTag());
        });
        binding.saturday.playButtonQuestion.setOnClickListener((v) -> {
            playAudioByFile((int) v.getTag());
        });
        binding.sunday.playButtonQuestion.setOnClickListener((v) -> {
            playAudioByFile((int) v.getTag());
        });

        binding.monday.playButton.setOnClickListener((v) -> {
            playAudioByUrl((String) v.getTag());
        });
        binding.tuesday.playButton.setOnClickListener((v) -> {
            playAudioByUrl((String) v.getTag());
        });
        binding.wednesday.playButton.setOnClickListener((v) -> {
            playAudioByUrl((String) v.getTag());
        });
        binding.thursday.playButton.setOnClickListener((v) -> {
            playAudioByUrl((String) v.getTag());
        });
        binding.friday.playButton.setOnClickListener((v) -> {
            playAudioByUrl((String) v.getTag());
        });
        binding.saturday.playButton.setOnClickListener((v) -> {
            playAudioByUrl((String) v.getTag());
        });
        binding.sunday.playButton.setOnClickListener((v) -> {
            playAudioByUrl((String) v.getTag());
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
