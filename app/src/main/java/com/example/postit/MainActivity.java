package com.example.postit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postit.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    //남현 - 211029 오디오 변수 추가
    /**
     * xml 변수
     */
    ImageButton audioRecordImageBtn;
    TextView audioRecordText;

    /**
     * 오디오 파일 관련 변수
     */
    // 오디오 권한
    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private int PERMISSION_CODE = 21;

    // 오디오 파일 녹음 관련 변수
    private MediaRecorder mediaRecorder;
    private String audioFileName; // 오디오 녹음 생성 파일 이름
    private boolean isRecording = false;    // 현재 녹음 상태를 확인하기 위함.
    private Uri audioUri = null; // 오디오 파일 uri

    // 오디오 파일 재생 관련 변수
    private MediaPlayer mediaPlayer = null;
    private Boolean isPlaying = false;
    ImageView playIcon;

    /**
     * 리사이클러뷰
     */
    private AudioAdapter audioAdapter;
    private ArrayList<Uri> audioList;


    //남현 - 211031 통화기록 검색 변수 추가
    Button btn_call;
    TextView textView_call;
    EditText editText_callNumber;

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DBtest();
        record();
        callNumber();

        bindLoginButton();


    }

    private void bindLoginButton() {
        binding.loginButton.setOnClickListener((v)->{
            Intent profileIntent = new Intent(this,ProfileActivity.class);
            startActivity(profileIntent);
        });

    }

    //남현, 집민 - 211029 DBtest추가
    public void DBtest() {
        Button uploadButton = (Button) findViewById(R.id.upload_button);
        Button queryButton = (Button) findViewById(R.id.get_data_button);

        EditText keyEditText = (EditText) findViewById(R.id.query_key);
        TextView queryResultTextView = (TextView) findViewById(R.id.query_result_textview);

        EditText editText = findViewById(R.id.editText);

        // Access a Cloud Firestore instance from your Activity

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);
        uploadButton.setOnClickListener(v -> {
            String temp = editText.getText().toString();
            uploadData(db, user, temp);
        });


        queryButton.setOnClickListener(v -> {
            String key = keyEditText.getText().toString();
            DocumentReference docRef = db.collection("users").document(key);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("get!!!", "DocumentSnapshot data: " + document.getData());
                            queryResultTextView.setText(document.getData().toString());
                        } else {
                            Log.d("get!!!", "No such document");
                        }
                    } else {
                        Log.d("get!!!", "get failed with ", task.getException());
                    }
                }
            });
        });


    }

    //집민 - 211030 DBtest 데이터 업로드/다운로드 추가
    private void uploadData(FirebaseFirestore db, Map<String, Object> user, String temp) {
        db.collection("users").document(temp)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("upload!!", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("upload!!", "Error writing document", e);
                    }
                });
    }


    //남현 - 211029 녹음 클래스 추가
    // xml 변수 초기화
    // 리사이클러뷰 생성 및 클릭 이벤트
    private void record() {
        audioRecordImageBtn = findViewById(R.id.audioRecordImageBtn);
        audioRecordText = findViewById(R.id.audioRecordText);

        audioRecordImageBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecording) {
                    // 현재 녹음 중 O
                    // 녹음 상태에 따른 변수 아이콘 & 텍스트 변경
                    isRecording = false; // 녹음 상태 값
                    audioRecordImageBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_fiber_manual_record_24, null)); // 녹음 상태 아이콘 변경
                    audioRecordText.setText("녹음 시작"); // 녹음 상태 텍스트 변경
                    stopRecording();
                    // 녹화 이미지 버튼 변경 및 리코딩 상태 변수값 변경
                } else {
                    // 현재 녹음 중 X
                    /*절차
                     *       1. Audio 권한 체크
                     *       2. 처음으로 녹음 실행한건지 여부 확인
                     * */
                    if (checkAudioPermission()) {
                        // 녹음 상태에 따른 변수 아이콘 & 텍스트 변경
                        isRecording = true; // 녹음 상태 값
                        audioRecordImageBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_record_voice_over_24, null)); // 녹음 상태 아이콘 변경
                        audioRecordText.setText("녹음 중"); // 녹음 상태 텍스트 변경
                        startRecording();
                    }
                }
            }
        });

        // 리사이클러뷰
        RecyclerView audioRecyclerView = findViewById(R.id.recyclerview);

        audioList = new ArrayList<>();
        audioAdapter = new AudioAdapter(this, audioList);
        audioRecyclerView.setAdapter(audioAdapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        audioRecyclerView.setLayoutManager(mLayoutManager);

        // 커스텀 이벤트 리스너 4. 액티비티에서 커스텀 리스너 객체 생성 및 전달
        audioAdapter.setOnItemClickListener(new AudioAdapter.OnIconClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String uriName = String.valueOf(audioList.get(position));

                /*음성 녹화 파일에 대한 접근 변수 생성;
                     (ImageView)를 붙여줘서 View 객체를 형변환 시켜줌.
                     전역변수로 한 이유는
                    * */

                File file = new File(uriName);

                if (isPlaying) {
                    // 음성 녹화 파일이 여러개를 클릭했을 때 재생중인 파일의 Icon을 비활성화(비 재생중)으로 바꾸기 위함.
                    if (playIcon == (ImageView) view) {
                        // 같은 파일을 클릭했을 경우
                        stopAudio();
                    } else {
                        // 다른 음성 파일을 클릭했을 경우
                        // 기존의 재생중인 파일 중지
                        stopAudio();

                        // 새로 파일 재생하기
                        playIcon = (ImageView) view;
                        playAudio(file);
                    }
                } else {
                    playIcon = (ImageView) view;
                    playAudio(file);
                }
            }
        });
    }

    // 오디오 파일 권한 체크
    private boolean checkAudioPermission() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), recordPermission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }

    // 녹음 시작
    private void startRecording() {
        //파일의 외부 경로 확인
        String recordPath = getExternalFilesDir("/").getAbsolutePath();
        // 파일 이름 변수를 현재 날짜가 들어가도록 초기화. 그 이유는 중복된 이름으로 기존에 있던 파일이 덮어 쓰여지는 것을 방지하고자 함.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        audioFileName = recordPath + "/" + "RecordExample_" + timeStamp + "_" + "audio.mp4";

        //Media Recorder 생성 및 설정
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(audioFileName);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //녹음 시작
        mediaRecorder.start();
    }

    // 녹음 종료
    private void stopRecording() {
        // 녹음 종료 종료
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

        // 파일 경로(String) 값을 Uri로 변환해서 저장
        //      - Why? : 리사이클러뷰에 들어가는 ArrayList가 Uri를 가지기 때문
        //      - File Path를 알면 File을  인스턴스를 만들어 사용할 수 있기 때문
        audioUri = Uri.parse(audioFileName);


        // 데이터 ArrayList에 담기
        audioList.add(audioUri);


        // 데이터 갱신
        audioAdapter.notifyDataSetChanged();

    }

    // 녹음 파일 재생
    private void playAudio(File file) {
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        playIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_pause_circle_outline_24, null));
        isPlaying = true;

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopAudio();
            }
        });

    }

    // 녹음 파일 중지
    private void stopAudio() {
        playIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_play_circle_filled_24, null));
        isPlaying = false;
        mediaPlayer.stop();
    }


    //남현 - 211031 전화번호 검색 기능 추가
    public void callNumber() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, MODE_PRIVATE);

        btn_call = (Button) findViewById(R.id.btn_call);
        textView_call = (TextView) findViewById(R.id.textView_call);
        textView_call.setMovementMethod(new ScrollingMovementMethod());
        editText_callNumber = (EditText) findViewById(R.id.editText_callNumber);
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_call.setText(getCallHistory());
            }
        });
    }

    // 통화기록 DB에서 가져옴
    public String getCallHistory() {
        String[] callSet = new String[]{
                CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.NUMBER, CallLog.Calls.DURATION};
        Cursor c = getContentResolver().query(CallLog.Calls.CONTENT_URI, callSet,
                null, null, null);
        if (c.getCount() == 0) {
            return "통화기록 없음";
        }
        StringBuffer callBuff = new StringBuffer();
        callBuff.append("\n날짜 : 구분 : 전화번호 : 통화시간 \n\n");
        c.moveToFirst();

        String lookfor = editText_callNumber.getText().toString();

        do {
            if (c.getString(2).equals(lookfor)) {
                long callData = c.getLong(0);
                SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-MM-dd");
                String date_str = datePattern.format(new Date(callData));
                callBuff.append(date_str + ":");
                if (c.getInt(1) == CallLog.Calls.INCOMING_TYPE)
                    callBuff.append("착신 :");
                else
                    callBuff.append("발신 :");
                //callBuff.append(c.getString(2) + ":");

                callBuff.append(c.getString(2) + ":");
                callBuff.append(c.getString(3) + "초\n");
            }

        } while (c.moveToNext());

        c.close();
        return callBuff.toString();
    }



}