package com.example.postit;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.postit.databinding.ActivityChildrenManageBinding;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChildrenManageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRequestRecyclerView();
        fetchRequest();

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
        adapter.setOnButtonClickListener((v,email)->{
            confirmRequest(email);
        });
        binding.requestRecyclerView.setAdapter(adapter);
        binding.requestRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
    }

    void fetchRequest(){ //자식으로부토 온 요청 가져오기
        db.collection("users").document(auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener((task)->{
                   if(task.isSuccessful()){
                       ArrayList<String> arr = (ArrayList<String>)task.getResult().get("requested");
                       if(arr==null) return;
                       adapter.addData(arr);
                       for (String a :
                               arr) {
                           Log.d("arr",a);
                       }

                   }
                });
    }

    private void confirmRequest(String email) {//확인버튼 누르면 follower로 등록
        //일괄작업으로 시행
        WriteBatch batch = db.batch();
        //request 배열에서 삭제하고 follower 배열에 추가
        DocumentReference ref = db.collection("users").document(auth.getCurrentUser().getUid());
        batch.update(ref,"requested",FieldValue.arrayRemove(email));
        batch.update(ref,"follower",FieldValue.arrayUnion(email));
        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                fetchRequest();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"failed!!!",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
