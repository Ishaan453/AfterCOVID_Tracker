package com.ishaanbhela.aftercovidtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DoctorHome extends AppCompatActivity implements unReviewedFormListAdapter.onUnReviewedItemClicked {

    RecyclerView unReviewedForms;
    TextView doctor;
    List<unRevievedFormListModel> forms = new ArrayList<>();
    Button signout;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);
        unReviewedForms = findViewById(R.id.doctorRecyclerView);
        doctor = findViewById(R.id.doctorWelcomeText);
        signout = findViewById(R.id.signOutButton);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fetchUserName();

        CollectionReference collRef= db.collection("Forms");
        Query query = collRef.whereEqualTo("Advised", "false");
        query.get().addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               for(QueryDocumentSnapshot docSnap : task.getResult()){
                   forms.add(new unRevievedFormListModel(docSnap.getString("Name") + " : " + docSnap.getString("MonthYear"), docSnap.getId()));
               }
               unReviewedForms.setLayoutManager(new LinearLayoutManager(this));
               unReviewedFormListAdapter unReviewedAdapter = new unReviewedFormListAdapter(forms, this);
               unReviewedForms.setAdapter(unReviewedAdapter);
           }
        });

        signout.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(DoctorHome.this, MainActivity.class));
            finish();
        });

    }

    public void fetchUserName(){
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DocumentReference userRef = db.collection("users").document(userId);
            userRef.get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists()) {
                                String name = doc.getString("name");
                                doctor.setText("Welcome,\n" + name);
                            } else {
                                Toast.makeText(DoctorHome.this, "No profile found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(DoctorHome.this, "Error getting profile: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onClick(int position) {
        unRevievedFormListModel clickedItem = forms.get(position);
        Intent i = new Intent(DoctorHome.this, FormView.class);
        i.putExtra("documentId", clickedItem.getUID());
        i.putExtra("isDoc", "true");
        startActivity(i);
    }
}