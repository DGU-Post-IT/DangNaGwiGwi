package com.example.postit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postit.databinding.ItemDayEmotionBinding;
import com.example.postit.model.VoiceEmotionRecord;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainPanelAdapter extends RecyclerView.Adapter<MainPanelAdapter.EmotionItemHolder> {
    Context context;
    ArrayList<VoiceEmotionRecord> data = new ArrayList<>();
    MainPanelAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public EmotionItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmotionItemHolder(ItemDayEmotionBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmotionItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class EmotionItemHolder extends RecyclerView.ViewHolder{
        ItemDayEmotionBinding binding;
        EmotionItemHolder(ItemDayEmotionBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
        void bind(VoiceEmotionRecord data){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(data.getDate());
            String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.KOREAN);
            binding.weekDayTextView.setText(dayOfWeek);
        }
    }
}
