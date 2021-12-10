package com.example.postit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postit.databinding.ItemRecordHistoryBinding;
import com.example.postit.model.AudioRecord;
import com.example.postit.model.VoiceEmotionRecord;

import java.time.LocalDate;
import java.util.ArrayList;

public class RecordHistoryAdapter extends RecyclerView.Adapter<RecordHistoryAdapter.RecordHistoryHolder> {

    Context context;
    ArrayList<AudioRecord> data = new ArrayList<>();

    RecordHistoryAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public RecordHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecordHistoryHolder(ItemRecordHistoryBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecordHistoryHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RecordHistoryHolder extends RecyclerView.ViewHolder{

        ItemRecordHistoryBinding binding;

        RecordHistoryHolder(ItemRecordHistoryBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
        void bind(AudioRecord data){
            LocalDate ld = DateUtil.convertDateToLocalDate(data.getTime());
            binding.dateTextView.setText(String.format("%02d.%02d",ld.getMonthValue(),ld.getDayOfMonth()));

            int icon;
            if(data.getEmotion()==0){
                icon = R.drawable.ic_weather_good_shadow;
            }else if(data.getEmotion()==1){
                icon = R.drawable.ic_weather_soso_shadow;
            }else if(data.getEmotion()==2){
                icon = R.drawable.ic_weather_angry_shadow;
            }else if(data.getEmotion()==3){
                icon = R.drawable.ic_weather_sad_shadow;
            }else{
                icon = R.drawable.ic_baseline_not_interested_24;
            }
            binding.emotionIconImageView.setImageResource(icon);

            if(data.getQuestionId()==0){
                binding.questionTextView.setText("나에게 가장 잘해줬던 사람은 누구인가? \n 그 사람에게 어떤 선물을 주고 싶나요?");
            }else if(data.getQuestionId()==1){
                binding.questionTextView.setText("다시 돌아가고 싶은 순간은 언제였나요?");
            }else if(data.getQuestionId()==2){
                binding.questionTextView.setText("가장 열심히 살았던 순간은 언제였나요?");
            }else if(data.getQuestionId()==3){
                binding.questionTextView.setText("가장 뿌듯했던 순간은 언제였나요?");
            }else if(data.getQuestionId()==4){
                binding.questionTextView.setText("만약 내일 해외여행을 갈 수 있다면 \n어느나라에 가고 싶나요?");
            }else{
                binding.questionTextView.setText("나에게 가장 잘해줬던 사람은 누구인가? \n 그 사람에게 어떤 선물을 주고 싶나요?");
            }
            binding.answerTextView.setText("우리집 둘째가 되게 잘해. 착하지");


        }

    }
}
