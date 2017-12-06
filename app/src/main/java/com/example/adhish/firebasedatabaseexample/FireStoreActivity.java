package com.example.adhish.firebasedatabaseexample;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FireStoreActivity extends AppCompatActivity {

    TextView tvDataFromFireStore;
    Button btnAdd;
    FirebaseFirestore db;
    Map<String, Object> user;
    String firstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_store);

        tvDataFromFireStore = findViewById(R.id.tvDataFromFireStore);
        btnAdd = findViewById(R.id.btnAdd);

        db = FirebaseFirestore.getInstance();

        // Create a new user with a first and last name
        user = new HashMap<>();
        user.put("first", "Adhish");
        user.put("last", "Lal");
        user.put("born", 1988);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add a new document with a generated ID
                db.collection("users")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("FireAct", "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("FireAct", "Error adding document", e);
                            }
                        });
            }
        });




        //Reading the document

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentFirebaseUser != null)
        {
            Log.d("ReadFireStoreAuth", "Logged in detected");

            db.collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                firstName = "";
                                for (DocumentSnapshot document : task.getResult()) {
                                    Log.d("ReadFireStore", document.getId() + " => " + document.getData());

                                    try {
                                        JSONObject jsonObject = new JSONObject(document.getData());

                                        firstName += "\n"+jsonObject.getString("first");

                                        tvDataFromFireStore.setText(firstName);
                                    }
                                    catch (JSONException e)
                                    {e.printStackTrace();}
                                }
                            } else {
                                Log.w("ReadFireStore", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
        else
        {
            Log.d("ReadFireStoreAuth", "Failed to detect logged in user");
        }
    }
}
