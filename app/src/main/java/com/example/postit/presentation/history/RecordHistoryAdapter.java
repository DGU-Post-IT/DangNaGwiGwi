package com.example.postit.presentation.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postit.util.DateUtil;
import com.example.postit.model.Emotion;
import com.example.postit.R;
import com.example.postit.databinding.ItemRecordHistoryBinding;
import com.example.postit.model.AudioRecord;

import java.time.LocalDate;
import java.util.ArrayList;

public class RecordHistoryAdapter extends RecyclerView.Adapter<RecordHistoryAdapter.RecordHistoryHolder> {

    Context context;
    ArrayList<AudioRecord> data = new ArrayList<>();

    RecordHistoryAdapter(Context context) {
        this.context = context;
    }

    private OnButtonClickListener listener = null;

    public interface OnButtonClickListener {
        void onItemClick(View v, String url);
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecordHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecordHistoryHolder(ItemRecordHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecordHistoryHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RecordHistoryHolder extends RecyclerView.ViewHolder {

        ItemRecordHistoryBinding binding;

        RecordHistoryHolder(ItemRecordHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.answerTextView.setOnClickListener((v) -> {
                if (listener != null) {
                    listener.onItemClick(v, data.get(getAdapterPosition()).getAudioLocation());
                }
            });
        }

        void bind(AudioRecord data) {
            LocalDate ld = DateUtil.convertDateToLocalDate(data.getTime());
            binding.dateTextView.setText(String.format("%02d.%02d", ld.getMonthValue(), ld.getDayOfMonth()));

            int icon;
            if (data.getEmotion() == Emotion.HAPPY.ordinal()) {
                icon = R.drawable.ic_weather_good_shadow;
            } else if (data.getEmotion() == Emotion.SAD.ordinal()) {
                icon = R.drawable.ic_weather_sad_shadow;
            } else if (data.getEmotion() == Emotion.ANGRY.ordinal()) {
                icon = R.drawable.ic_weather_angry_shadow;
            } else if (data.getEmotion() == Emotion.SOSO.ordinal()) {
                icon = R.drawable.ic_weather_soso_shadow;
            } else {
                icon = R.drawable.ic_baseline_not_interested_24;
            }
            binding.emotionIconImageView.setImageResource(icon);

            if (data.getQuestionId() == 0) {
                binding.questionTextView.setText("나에게 가장 잘해줬던 사람은 누구인가요? \n 그 사람에게 어떤 선물을 주고 싶나요?");
            } else if (data.getQuestionId() == 1) {
                binding.questionTextView.setText("다시 돌아가고 싶은 순간은 언제였나요?");
            } else if (data.getQuestionId() == 2) {
                binding.questionTextView.setText("가장 열심히 살았던 순간은 언제였나요?");
            } else if (data.getQuestionId() == 3) {
                binding.questionTextView.setText("가장 뿌듯했던 순간은 언제였나요?");
            } else if (data.getQuestionId() == 4) {
                binding.questionTextView.setText("만약 내일 해외여행을 갈 수 있다면 어느나라에 가고 싶나요?");
            } else if (data.getQuestionId() == 5) {
                binding.questionTextView.setText("오늘 가장 재밌었던 일이 뭔가요?");
            } else if (data.getQuestionId() == 6) {
                binding.questionTextView.setText("가장 좋아하는 음식이 뭐예요?\n 누구랑 같이 먹고싶나요?");
            } else if (data.getQuestionId() == 7) {
                binding.questionTextView.setText("어렸을 때 꿈이 뭐였어요?");
            } else if (data.getQuestionId() == 8) {
                binding.questionTextView.setText("보고싶은 친구들이 있나요? \n친구들이랑 주로 뭐하고 노셨어요?");
            } else if (data.getQuestionId() == 9) {
                binding.questionTextView.setText("좋아하는 색깔이뭐예요?");
            } else {
                binding.questionTextView.setText("알 수 없는 질문입니다.");
            }
            binding.answerTextView.setText("[재생] " + data.getAnswer());


        }
    }
}
