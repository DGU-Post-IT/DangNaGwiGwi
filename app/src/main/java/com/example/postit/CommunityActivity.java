package com.example.postit;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.postit.databinding.ActivityCommunityBinding;

public class CommunityActivity extends AppCompatActivity {

    ActivityCommunityBinding binding;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_community);
        setContentView(binding.getRoot());
        getWindow().setWindowAnimations(0);



        binding.layoutGohome.setOnClickListener((v)->{
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        });
//
//        binding.layoutGocalendar.setOnClickListener((v)->{
//            Intent intent = new Intent(this,CalendarActivity.class);
//            startActivity(intent);
//        });

        binding.layoutGogame.setOnClickListener((v)->{
            Intent intent = new Intent(this,GameActivity.class);
            startActivity(intent);
        });

    }


}
