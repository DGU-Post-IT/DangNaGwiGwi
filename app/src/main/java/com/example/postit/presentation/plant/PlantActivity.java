package com.example.postit.presentation.plant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.postit.R;
import com.example.postit.databinding.ActivityMainBinding;
import com.example.postit.databinding.ActivityPlantBinding;
import com.example.postit.presentation.main.MainActivity;

public class PlantActivity extends AppCompatActivity {

    private ActivityPlantBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int value = 5000; //value input
        int high = 4000; int low = 700; //700이 축축, 4000이 건조
        viewHumid(value, high, low);

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
}
