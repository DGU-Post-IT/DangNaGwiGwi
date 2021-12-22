package com.example.postit.presentation.childrenmanage;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.postit.repository.RequestRepository;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

public class ChildrenManageViewModel extends AndroidViewModel {

    RequestRepository requestRepository = new RequestRepository();

    MutableLiveData<ArrayList<String>> requests = new MutableLiveData<>(new ArrayList<>());
    MutableLiveData<ArrayList<String>> children = new MutableLiveData<>(new ArrayList<>());

    public ChildrenManageViewModel(Application application){
        super(application);
    }

    public void confirmRequest(String email){
        requestRepository.confirmRequest(email).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
fetchRequest();
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    public void fetchRequest(){
        requestRepository.fetchRequest().subscribe(new Consumer<DocumentSnapshot>() {
            @Override
            public void accept(DocumentSnapshot doc) throws Throwable {
                ArrayList<String> follower = (ArrayList<String>) doc.get("follower");
                ArrayList<String> request = (ArrayList<String>) doc.get("requested");
                if (follower != null) {
                    children.setValue(follower);
                }
                if(request !=null){
                    requests.setValue(request);
                }

            }
        });
    }



}
