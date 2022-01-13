package com.example.postit.presentation.plant;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.postit.R;
import com.example.postit.databinding.ActivityMainBinding;
import com.example.postit.databinding.ActivityPlantBinding;
import com.example.postit.presentation.main.MainActivity;

public class PlantActivity extends AppCompatActivity {

    private ActivityPlantBinding binding;

    private PlantViewModel model;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        model = new ViewModelProvider(this).get(PlantViewModel.class);

        model.fetchPlantRecord();
        model.plantRecord.observe(this,pr ->{
            if(pr==null)return;

            Log.d("Humid : ", pr.getHumid()+ " ");
            int high_h = 4000; int low_h = 700; //700이 축축, 4000이 건조
            viewHumid(pr.getHumid(), high_h, low_h);

            Log.d("Temp : ", pr.getTemp()+ " ");
            int high_t = 25; int low_t = 15; //700이 축축, 4000이 건조
            viewTemp(pr.getTemp(), high_t, low_t);
        });


    }

    void viewHumid(int value, int high, int low){
        int v = -1;

        if(value < low) v = 4;
        else if(value < low+(high-low)/3) v = 3;
        else if(value < low+(high-low)/3*2) v = 2;
        else if(value < high) v = 1;
        else if(value >= high) v = 0;

        switch(v){
            case 0 :
                binding.humid0.setVisibility(View.VISIBLE);
                binding.humid1.setVisibility(View.GONE);
                binding.humid2.setVisibility(View.GONE);
                binding.humid3.setVisibility(View.GONE);
                binding.humid4.setVisibility(View.GONE);
                break;

            case 1 :
                binding.humid0.setVisibility(View.GONE);
                binding.humid1.setVisibility(View.VISIBLE);
                binding.humid2.setVisibility(View.GONE);
                binding.humid3.setVisibility(View.GONE);
                binding.humid4.setVisibility(View.GONE);
                break;
            case 2 :
                binding.humid0.setVisibility(View.GONE);
                binding.humid1.setVisibility(View.GONE);
                binding.humid2.setVisibility(View.VISIBLE);
                binding.humid3.setVisibility(View.GONE);
                binding.humid4.setVisibility(View.GONE);
                break;

            case 3 :
                binding.humid0.setVisibility(View.GONE);
                binding.humid1.setVisibility(View.GONE);
                binding.humid2.setVisibility(View.GONE);
                binding.humid3.setVisibility(View.VISIBLE);
                binding.humid4.setVisibility(View.GONE);
                break;

            case 4 :
                binding.humid0.setVisibility(View.GONE);
                binding.humid1.setVisibility(View.GONE);
                binding.humid2.setVisibility(View.GONE);
                binding.humid3.setVisibility(View.GONE);
                binding.humid4.setVisibility(View.VISIBLE);
                break;

            default:
                binding.humid0.setVisibility(View.GONE);
                binding.humid1.setVisibility(View.GONE);
                binding.humid2.setVisibility(View.GONE);
                binding.humid3.setVisibility(View.GONE);
                binding.humid4.setVisibility(View.GONE);
            break;
        }

    }

    void viewTemp(float value, int high, int low){
        int v = -1;

        if(value < low) v = 0;
        else if(value < low+(high-low)/3) v = 1;
        else if(value < low+(high-low)/3*2) v = 2;
        else if(value < high) v = 3;
        else if(value >= high) v = 4;

        switch(v){
            case 0 :
                binding.temp0.setVisibility(View.VISIBLE);
                binding.temp1.setVisibility(View.GONE);
                binding.temp2.setVisibility(View.GONE);
                binding.temp3.setVisibility(View.GONE);
                binding.temp4.setVisibility(View.GONE);
                break;

            case 1 :
                binding.temp0.setVisibility(View.GONE);
                binding.temp1.setVisibility(View.VISIBLE);
                binding.temp2.setVisibility(View.GONE);
                binding.temp3.setVisibility(View.GONE);
                binding.temp4.setVisibility(View.GONE);
                break;
            case 2 :
                binding.temp0.setVisibility(View.GONE);
                binding.temp1.setVisibility(View.GONE);
                binding.temp2.setVisibility(View.VISIBLE);
                binding.temp3.setVisibility(View.GONE);
                binding.temp4.setVisibility(View.GONE);
                break;

            case 3 :
                binding.temp0.setVisibility(View.GONE);
                binding.temp1.setVisibility(View.GONE);
                binding.temp2.setVisibility(View.GONE);
                binding.temp3.setVisibility(View.VISIBLE);
                binding.temp4.setVisibility(View.GONE);
                break;

            case 4 :
                binding.temp0.setVisibility(View.GONE);
                binding.temp1.setVisibility(View.GONE);
                binding.temp2.setVisibility(View.GONE);
                binding.temp3.setVisibility(View.GONE);
                binding.temp4.setVisibility(View.VISIBLE);
                break;

            default:
                binding.temp0.setVisibility(View.GONE);
                binding.temp1.setVisibility(View.GONE);
                binding.temp2.setVisibility(View.GONE);
                binding.temp3.setVisibility(View.GONE);
                binding.temp4.setVisibility(View.GONE);
                break;
        }

    }
}
