package com.example.postit.presentation.childrenmanage;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.postit.databinding.ActivityChildrenManageBinding;
import com.example.postit.presentation.history.HistoryViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;

public class ChildrenManageActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    ActivityChildrenManageBinding binding;
    ChildrenAdapter adapter;
    ChildrenAdapter followerAdapter;
    ChildrenManageViewModel model;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChildrenManageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        model = new ViewModelProvider(this).get(ChildrenManageViewModel.class);

        initRequestRecyclerView();
        fetchRequest();

        bindView();


    }

    private void bindView() {
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchRequest();
                binding.refreshLayout.setRefreshing(false);
            }
        });
    }

    private void initRequestRecyclerView() {
        adapter = new ChildrenAdapter();
        adapter.setOnButtonClickListener((v, email) -> {
            confirmRequest(email);
        });

        binding.requestRecyclerView.setAdapter(adapter);
        binding.requestRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        followerAdapter = new ChildrenAdapter();
        followerAdapter.setOnButtonClickListener((v, email) -> {
        });
        binding.childrenRecyclerView.setAdapter(followerAdapter);
        binding.childrenRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        model.requests.observe(this,(array)->{
            adapter.addData(array);
        });
        model.children.observe(this,(array)->{
            followerAdapter.addData(array);
        });
    }

    void fetchRequest() { //자식으로부토 온 요청 가져오기
        model.fetchRequest();
    }

    private void confirmRequest(String email) {//확인버튼 누르면 follower로 등록
        model.confirmRequest(email);
    }
}
