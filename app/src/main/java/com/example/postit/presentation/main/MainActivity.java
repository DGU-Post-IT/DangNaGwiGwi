package com.example.postit.presentation.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.postit.presentation.plant.PlantActivity;
import com.example.postit.presentation.nagging.NaggingActivity;
import com.example.postit.presentation.question.QuestionActivity;
import com.example.postit.R;
import com.example.postit.databinding.ActivityMainBinding;
import com.example.postit.presentation.profile.ProfileActivity;
import com.example.postit.presentation.history.HistoryActivity;


public class MainActivity extends AppCompatActivity {

    //뷰바인딩
    private ActivityMainBinding binding;

    private MainViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = new ViewModelProvider(this).get(MainViewModel.class);

        bindButton();
        initProportionView();

        model.fetchWeeklyData();
    }

    private void bindButton() {
        binding.layoutWeeklycheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
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

        binding.layoutPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PlantActivity.class);
                startActivity(intent);
            }
        });
        binding.myPageButton.setOnClickListener((v) -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });

        binding.btnQuestionActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                startActivity(intent);
            }
        });
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