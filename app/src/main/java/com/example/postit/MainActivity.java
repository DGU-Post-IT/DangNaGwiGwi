package com.example.postit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.example.postit.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    //뷰바인딩
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.layoutWeeklycheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WeeklyCheckActivity.class);
                startActivity(intent);

            }
        });
        binding.myPageButton.setOnClickListener((v) -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });

    }


}

