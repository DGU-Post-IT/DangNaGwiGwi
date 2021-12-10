package com.example.postit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.postit.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {

    //파이어베이스 파이어스토어DB
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //파이어베이스 로그인 인증 객체
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    //뷰바인딩
    private ActivityMainBinding binding;

    private MainViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = new ViewModelProvider(this).get(MainViewModel.class);
        binding.layoutWeeklycheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WeeklyCheckActivity.class);
                startActivity(intent);

            }
        });

        binding.layoutNagging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NaggingActivity.class);
                startActivity(intent);
            }
        });

        binding.layoutGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });



        initProportionView();
        binding.myPageButton.setOnClickListener((v) -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });

        model.fetchWeeklyData();

    }

    void initProportionView(){
        binding.proportionView.showGaps(false);
        binding.proportionView.showRoundedCorners(true);
        binding.proportionView.radiusRoundedCorners(10);
        binding.proportionView.addColors(getResources().getColor(R.color.happy_pink),
                getResources().getColor(R.color.soso_blue),
                getResources().getColor(R.color.angry_yellow),
                getResources().getColor(R.color.sad_mint));
        model.emotionCount.observe(this, new Observer<Integer[]>() {
            @Override
            public void onChanged(Integer[] integers) {
                binding.proportionView.setDataList(integers);
            }
        });
    }
}