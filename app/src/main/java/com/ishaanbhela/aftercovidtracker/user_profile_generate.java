package com.ishaanbhela.aftercovidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ishaanbhela.aftercovidtracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class user_profile_generate extends AppCompatActivity {

    private EditText etName;
    private Button btnSaveProfile;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_generate);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etName = findViewById(R.id.etName);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        btnSaveProfile.setOnClickListener(view -> saveUserProfile());
    }

    private void saveUserProfile(){
        String name = etName.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }

        FirebaseUser currentUser = auth.getCurrentUser();

        if(currentUser != null){
            String userId = currentUser.getUid();
            Map<String, Object> userProfile = new HashMap<>();
            userProfile.put("name", name);
            userProfile.put("UserType", "Patient");

            db.collection("users").document(userId)
                    .set(userProfile)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(user_profile_generate.this, "Profile saved successfully", Toast.LENGTH_SHORT).show();
                        // Redirect to home activity
                        startActivity(new Intent(user_profile_generate.this, Home.class));
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(user_profile_generate.this, "Error saving profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}