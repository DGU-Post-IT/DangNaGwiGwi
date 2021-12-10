package com.example.postit;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PostItApplication extends Application {

    ExecutorService executorService = Executors.newFixedThreadPool(4);
}
