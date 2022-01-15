package com.example.postit.presentation.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
import com.github.proportionsbarlibrary.ProportionsBar;

import java.util.ArrayList;
import java.util.List;


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
        model.fetchPlantRecord();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

        model.emotionCount.observe(this, new Observer<Integer[]>() {
            int[] colors = {getResources().getColor(R.color.happy_pink),
                    getResources().getColor(R.color.soso_blue),
                    getResources().getColor(R.color.angry_yellow),
                    getResources().getColor(R.color.sad_mint)};
            @Override
            public void onChanged(Integer[] integers) {
                binding.proportionView.removeAllViewsInLayout();
                ProportionsBar pb = new ProportionsBar(getApplicationContext());
                pb.showGaps(false);
                pb.showRoundedCorners(true);
                pb.radiusRoundedCorners(8);
                int cnt = 0;
                int color = 0;
                for (int i =0;i<integers.length;i++) {
                    if(integers[i]!=0){
                    pb.addColors(colors[i]);
                    pb.addValues(integers[i]);
                    color = colors[i];
                    cnt++;
                    }
                }
                if(cnt==1){
                    pb.addColors(color);
                    pb.addValues(1);
                }else if(cnt ==0){//데이터가 없는 경우

                }
                binding.proportionView.addView(pb);
            }
        });

        model.plantRecord.observe(this,plantRecord -> {
            if(plantRecord==null) return;
            Toast.makeText(this,plantRecord.getHumid()+" "+plantRecord.getTemp()+" "+plantRecord.getMyTimestamp(),Toast.LENGTH_SHORT).show();
        });
    }
}