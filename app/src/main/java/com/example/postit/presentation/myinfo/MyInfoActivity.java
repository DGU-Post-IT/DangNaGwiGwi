package com.example.postit.presentation.myinfo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.postit.databinding.ActivityUserInfoRegisterBinding;
import com.example.postit.model.ParentUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Date;

public class MyInfoActivity extends AppCompatActivity {
    String TAG = "MyInfoActivity";

    ActivityUserInfoRegisterBinding binding;

    FirebaseAuth auth;
    FirebaseFirestore db;

    String token;
    ParentUser parent = new ParentUser();
    MyInfoViewModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInfoRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        model = new ViewModelProvider(this).get(MyInfoViewModel.class);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        getFcmToken();

        initEditTExt();
        bindSexSelectButton();
        bindSaveButton();
        model.fetchUserInfo();

        model.user.observe(this, new Observer<ParentUser>() {
            @Override
            public void onChanged(ParentUser parentUser) {
                if(parentUser!=null){
                    parent = parentUser;
                }
                binding.nameEditText.setText(parentUser.getName());
                binding.phoneNumberEditText.setText(parentUser.getPhone()!=null?parentUser.getPhone():"");
                if(parentUser.getSex()==1){
                    binding.maleButton.setSelected(true);
                    binding.femaleButton.setSelected(false);
                }else if(parentUser.getSex()==2){
                    binding.maleButton.setSelected(false);
                    binding.femaleButton.setSelected(true);
                }
                binding.birthYear.setText(String.valueOf(parentUser.getBirthdate().getYear()));
                binding.birthMonth.setText(String.valueOf(parentUser.getBirthdate().getMonth()+1));
                binding.birthDay.setText(String.valueOf(parentUser.getBirthdate().getDay()+1));

            }
        });
    }



    private void initEditTExt() {
        binding.birthYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 2) {
                    binding.birthYear.setText(editable.toString().substring(0, 2));
                }
            }
        });
        binding.birthMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 2) {
                    binding.birthMonth.setText(editable.toString().substring(0, 2));
                }
            }
        });
        binding.birthDay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 2) {
                    binding.birthDay.setText(editable.toString().substring(0, 2));
                }
            }
        });
    }

    private void bindSaveButton() {
        binding.saveButton.setOnClickListener((v) -> {
            int birthYear;
            int birthMonth;
            int birthDay;
            try {
                birthYear = Integer.parseInt(binding.birthYear.getText().toString());
                birthMonth = Integer.parseInt(binding.birthMonth.getText().toString());
                birthDay = Integer.parseInt(binding.birthDay.getText().toString());
                if(birthMonth<1||birthMonth>12||birthDay<1||birthDay>31) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "올바른 생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            Date birthdate = new Date(birthYear, birthMonth - 1, birthDay);
            parent.setBirthdate(birthdate);
            if (parent.getSex() == 0) {
                Toast.makeText(this, "성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (auth.getCurrentUser() == null) {
                Toast.makeText(this, "계정 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
            parent.setEmail(auth.getCurrentUser().getEmail());
            String name = binding.nameEditText.getText().toString();
            if (name.equals("")) {
                Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            parent.setName(name);

            if (token == null) {
                Toast.makeText(this, "알 수 없는 오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                getFcmToken();
            }
            parent.setFcm(token);

            registerUserInfo(parent, auth.getCurrentUser().getUid());


        });
    }


    private void bindSexSelectButton() {
        binding.maleButton.setOnClickListener((v) -> {
            binding.maleButton.setSelected(true);
            binding.femaleButton.setSelected(false);
            parent.setSex(1);
        });
        binding.femaleButton.setOnClickListener((v) -> {
            binding.maleButton.setSelected(false);
            binding.femaleButton.setSelected(true);
            parent.setSex(2);
        });
    }

    void getFcmToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                        Log.d(TAG, token);
//                        Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void registerUserInfo(ParentUser user, String key) {
        db.collection("users").document(key)
                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("upload!!", "Error writing document", e);
                    }
                });
    }

}
