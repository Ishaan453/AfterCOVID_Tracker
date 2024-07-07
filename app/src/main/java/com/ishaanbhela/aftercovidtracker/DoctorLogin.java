package com.ishaanbhela.aftercovidtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DoctorLogin extends AppCompatActivity {

    private EditText email;
    private EditText pass;
    private Button login;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);

        email = findViewById(R.id.emailEditText);
        pass = findViewById(R.id.passwordEditText);
        login = findViewById(R.id.loginButton);
        auth = FirebaseAuth.getInstance();

        login.setOnClickListener(v -> {
            String Email = email.getText().toString();
            String Pass = pass.getText().toString();
            if (TextUtils.isEmpty(Email)) {
                Toast.makeText(DoctorLogin.this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(Pass)) {
                Toast.makeText(DoctorLogin.this, "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(DoctorLogin.this, task -> {
                if(task.isSuccessful()){
                    startActivity(new Intent(DoctorLogin.this, DoctorHome.class));
                    finish();
                }
                else{
                    Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}