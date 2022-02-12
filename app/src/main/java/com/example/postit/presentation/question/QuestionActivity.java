package com.example.postit.presentation.question;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.postit.util.QuestionIdUtil;
import com.example.postit.R;
import com.example.postit.databinding.ActivityQuestionBinding;
import com.example.postit.model.AudioRecord;
import com.example.postit.model.EmotionRecord;
import com.example.postit.util.KoreanTime;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QuestionActivity extends AppCompatActivity {

    final int RECORD_AUDIO_PERMISSION_CODE = 100;
    MediaPlayer player = null;
    MediaRecorder recorder = null;
    ActivityQuestionBinding binding;
    CountDownTimer ct = null;
    File cacheFile;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    int questionId;
    DocumentReference audioRef;
    AudioRecord ar;

    boolean isRecording = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //questionId = getIntent().getIntExtra("questionId",-1);
        questionId = (int) ((KoreanTime.koreaToday()) % 10);
        bindEmotionDialog();

    }

    private void bindEmotionDialog() {
        binding.happyButton.setOnClickListener((v) -> {
            saveEmotionRecordOnDataBase(0);
        });
        binding.sadButton.setOnClickListener((v) -> {
            saveEmotionRecordOnDataBase(1);
        });
        binding.angryButton.setOnClickListener((v) -> {
            saveEmotionRecordOnDataBase(2);
        });
        binding.anxietyButton.setOnClickListener((v) -> {
            saveEmotionRecordOnDataBase(3);
        });
        binding.woundButton.setOnClickListener((v) -> {
            saveEmotionRecordOnDataBase(4);
        });
        binding.embarrassButton.setOnClickListener((v) -> {
            saveEmotionRecordOnDataBase(5);
        });


    }

    void saveEmotionRecordOnDataBase(int emotion){
        EmotionRecord emotionRecord = new EmotionRecord(emotion);

        WriteBatch batch = db.batch();
        DocumentReference emotionRef = db.collection("users").document(auth.getCurrentUser().getUid())
                .collection("emotionRecord").document();

        batch.set(emotionRef,emotionRecord);

        ar.setEmotion(emotion);
        batch.set(audioRef,ar);

        MediaPlayer player2 = MediaPlayer.create(this, R.raw.good_night);

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                player2.start();

                player2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        finish();
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(questionId==-1){
            finish();
        }

        //시작되면 질문하고 자동으로 녹음 시행, 그리고 업로드까지
        startMediaPlayer(questionId);
    }

    public void startMediaPlayer(int questionId) {

        player = MediaPlayer.create(this, QuestionIdUtil.getAudioId(questionId));
        switch (questionId){
            case 0:
                binding.tvQuestion.setText("나에게 가장 잘해줬던 사람은 누구인가요? \n 그 사람에게 어떤 선물을 주고 싶나요?");
                break;
            case 1:
                binding.tvQuestion.setText("다시 돌아가고 싶은 순간은 언제였나요?");
                break;
            case 2:
                binding.tvQuestion.setText("가장 열심히 살았던 순간은 언제였나요?");
                break;
            case 3:
                binding.tvQuestion.setText("가장 뿌듯했던 순간은 언제였나요?");
                break;
            case 4:
                binding.tvQuestion.setText("만약 내일 해외여행을 갈 수 있다면 어느나라에 가고 싶나요?");
                break;
            case 5:
                binding.tvQuestion.setText("오늘 가장 재밌었던 일이 뭔가요?");
                break;
            case 6:
                binding.tvQuestion.setText("가장 좋아하는 음식이 뭐예요?\n 누구랑 같이 먹고싶나요?");
                break;
            case 7:
                binding.tvQuestion.setText("어렸을 때 꿈이 뭐였어요?");
                break;
            case 8:
                binding.tvQuestion.setText("보고싶은 친구들이 있나요? \n친구들이랑 주로 뭐하고 노셨어요?");
                break;
            case 9:
                binding.tvQuestion.setText("좋아하는 색깔이 뭐예요?");
                break;
        }

        player.setOnCompletionListener((pl) -> {

            Log.d("tag", "play complete");
            pl.release();
            Log.d("tag", "play complete");
            //재생 완룍되면 녹음 시작
            startVoiceRecording();
            Log.d("tag", "start recording");

        });
        player.start();
        Log.d("tag", "startmedia play");

    }


    void startVoiceRecording() {
        //권한 확인부터
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {//권한 있으면 녹음

            Log.d("tag", "permission granted.. start recording..");
            startRecorder();


        } else {//권한 없으면 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_PERMISSION_CODE);
        }

    }

    private void startRecorder() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String audioFileName = "RecordExample_" + timeStamp + "_" + "audio.3gp";

        cacheFile = new File(getApplicationContext().getCacheDir(), audioFileName);
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setAudioEncodingBitRate(16);
        recorder.setAudioSamplingRate(44100);
        recorder.setOutputFile(cacheFile.getAbsolutePath());

        try {
            recorder.prepare();
            recorder.start();
            isRecording = true;

            //버튼 visibility 활성화
            binding.stopButton.setVisibility(View.VISIBLE);

//            ct = new CountDownTimer(30 * 1000, 1000) {
//                @Override
//                public void onTick(long l) {
//                    updateRemainTimeView(l);
//                }
//
//                @Override
//                public void onFinish() {
//                    stopRecorder();
//                    uploadFileWithUri(Uri.fromFile(cacheFile));
//                }
//            };
//
//            ct.start();
            
            //버튼 클릭하면 녹음 멈추게
            binding.stopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopRecorder();
                    uploadFileWithUri(Uri.fromFile(cacheFile));
                }
            });

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecorder() {
        try {
            recorder.stop();
            recorder.release();
            recorder = null;
        } catch (RuntimeException ex) {

        }
        isRecording = false;
        Toast.makeText(this, "녹음 종료", Toast.LENGTH_SHORT).show();
    }

    //Uri로 파일 업로드
    private void uploadFileWithUri(Uri audioUri) {
        StorageReference ref = storage.getReference();
        StorageReference fileRef = ref.child("audio/" + auth.getCurrentUser().getUid()+"/"+audioUri.getLastPathSegment());
        UploadTask uploadTask = fileRef.putFile(audioUri);
        Toast.makeText(this, "업로드 시작 중..", Toast.LENGTH_SHORT);
        Log.d("upload!!", "starting upload...");

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Log.d("upload!!", "fail!!1");
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return fileRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.d("upload!!", downloadUri.toString());
                    Toast.makeText(getApplicationContext(), "업로드 성공", Toast.LENGTH_SHORT);
                    writeAudioRecordDatabase(downloadUri.toString());
                } else {
                    // Handle failures
                    // ...
                    Log.d("upload!!", "fail!!");
                }
            }
        });
    }

    private void writeAudioRecordDatabase(String downloadUri) {
        String myID = auth.getCurrentUser().getUid();
        DocumentReference ref = db.collection("users").document(myID)
                .collection("audioFile").document();
        AudioRecord audioRecord = new AudioRecord(auth.getCurrentUser().getUid(), downloadUri, 0,questionId,"hello");
        ref.set(audioRecord);
        audioRef = ref;
        ar = audioRecord;

        showEmotionDialog();
    }

    private void showEmotionDialog() {
        binding.tvInfo1.setVisibility(View.GONE);
        binding.tvQuestion.setVisibility(View.GONE);
        binding.stopButton.setVisibility(View.GONE);
        binding.cvQuestion.setVisibility(View.GONE);

        MediaPlayer player_feeling_check = MediaPlayer.create(this, R.raw.feeling_check);
        player_feeling_check.start();
        binding.tvInfo2.setVisibility(View.VISIBLE);
        binding.cvEmotioncheck.setVisibility(View.VISIBLE);
    }

    public void updateRemainTimeView(long l) {
        binding.tvQuestion.setText(String.valueOf(l)+"밀리초 후 녹음 종료 및 업로드 시작");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RECORD_AUDIO_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startVoiceRecording();
        } else {
            Toast.makeText(this, "녹음 권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            player.start();
        } catch (Exception e) {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }
}
