package com.example.postit;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button uploadButton = (Button) findViewById(R.id.upload_button);
        Button queryButton = (Button) findViewById(R.id.get_data_button);

        EditText keyEditText = (EditText) findViewById(R.id.query_key);
        TextView queryResultTextView = (TextView) findViewById(R.id.query_result_textview);

        EditText editText = findViewById(R.id.editText);

        // Access a Cloud Firestore instance from your Activity

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);
        uploadButton.setOnClickListener(v->{
            String temp = editText.getEditableText().toString();
            uploadData(db,user,temp);
        });



        queryButton.setOnClickListener(v->{
            String key = keyEditText.getEditableText().toString();
            DocumentReference docRef = db.collection("users").document(key);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("get!!!", "DocumentSnapshot data: " + document.getData());
                            queryResultTextView.setText(document.getData().toString());
                        } else {
                            Log.d("get!!!", "No such document");
                        }
                    } else {
                        Log.d("get!!!", "get failed with ", task.getException());
                    }
                }
            });
        });



    }

    private void uploadData(FirebaseFirestore db, Map<String, Object> user,String temp) {
        db.collection("users").document(temp)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("upload!!", "DocumentSnapshot successfully written!");
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