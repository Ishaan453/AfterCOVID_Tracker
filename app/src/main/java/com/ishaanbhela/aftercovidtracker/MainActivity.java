package com.ishaanbhela.aftercovidtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ishaanbhela.aftercovidtracker.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private EditText phno;
    private EditText otp;
    private Button login;
    private Button otpbtn;
    private Button docLogin;

    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phno = findViewById(R.id.phno);
        otp = findViewById(R.id.otp);
        login = findViewById(R.id.login);
        otpbtn = findViewById(R.id.otpbtn);
        docLogin = findViewById(R.id.doctorLogin);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        otp.setVisibility(View.INVISIBLE);
        otpbtn.setVisibility(View.INVISIBLE);

        if(currentUser != null){
            checkUserProfile(currentUser.getUid());
        }

        login.setOnClickListener(v -> {
            String Phone = phno.getText().toString().trim();
            if(Phone.length() < 10){
                Toast.makeText(this, "Enter correct phone number", Toast.LENGTH_SHORT).show();
                return;
            }
            String completePhno = "+91" + Phone;

            startPhoneNumberVerification(completePhno);
        });

        otpbtn.setOnClickListener(v -> {
            String otpstr = otp.getText().toString().trim();
            if (otpstr.isEmpty()) {
                otp.setError("OTP is required");
                otp.requestFocus();
                return;
            }

            verifyPhoneNumberWithCode(verificationId, otpstr);
        });

        docLogin.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, DoctorLogin.class));
            finish();
        });



    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                    // Automatically handle the SMS verification
                    signInWithPhoneAuthCredential(credential);
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(MainActivity.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(e.getMessage());
                }

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    verificationId = s;
                    Toast.makeText(MainActivity.this, "Verification code sent", Toast.LENGTH_SHORT).show();
                    otp.setVisibility(View.VISIBLE);
                    otpbtn.setVisibility(View.VISIBLE);
                }
            };

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = task.getResult().getUser();
                        Toast.makeText(MainActivity.this, "Authentication successful.", Toast.LENGTH_SHORT).show();

                        // Check user profile existence in Firestore
                        checkUserProfile(user.getUid());

                    } else {
                        // Sign in failed, display a message and update the UI
                        Toast.makeText(MainActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserProfile(String userId){
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentSnapshot doc = task.getResult();
                        if(doc.exists()){
                            // Intent to home page
                            if(doc.getString("UserType").equals("Patient")) {
                                startActivity(new Intent(MainActivity.this, Home.class));
                            }
                            else if(doc.getString("UserType").equals("Doctor")){
                                startActivity(new Intent(MainActivity.this, DoctorHome.class));
                            }
                            finish();
                        }
                        else{
                            // Intent to data page
                            Toast.makeText(this, "User profile dosent exist", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, user_profile_generate.class));
                            finish();
                        }
                    }
                });

    }
}