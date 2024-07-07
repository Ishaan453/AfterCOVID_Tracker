package com.ishaanbhela.aftercovidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class FormView extends AppCompatActivity {

    private TextView nameTextView;
    private TextView localityTextView, contactNumberTextView, genderTextView, ageTextView, diabeticTextView;
    private TextView hypersensitiveTextView, cadTextView, yearsOfSufferingTextView, coughAloneTextView, chronicFatigueTextView;
    private TextView nauseaLossAppetiteTextView, abdominalDiscomfortTextView, cough_with_breathlessness, restlessness;
    private TextView severe_cough_with_breathlessness, chest_pain, confusion, foggy_thoughts, dizziness, heart_rate, respiratory_rate, spo2;
    private EditText advise;
    private TextView adviseHead;
    private Button submitAdvise;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_view);

        nameTextView = findViewById(R.id.name);
        localityTextView = findViewById(R.id.locality);
        contactNumberTextView = findViewById(R.id.contact_number);
        genderTextView = findViewById(R.id.gender);
        ageTextView = findViewById(R.id.age);
        diabeticTextView = findViewById(R.id.diabetic);
        hypersensitiveTextView = findViewById(R.id.hypersensitive);
        cadTextView = findViewById(R.id.cad);
        yearsOfSufferingTextView = findViewById(R.id.years_of_suffering);
        coughAloneTextView = findViewById(R.id.cough_alone);
        chronicFatigueTextView = findViewById(R.id.chronic_fatigue);
        nauseaLossAppetiteTextView = findViewById(R.id.nausea_loss_appetite);
        abdominalDiscomfortTextView = findViewById(R.id.abdominal_discomfort);
        cough_with_breathlessness = findViewById(R.id.cough_with_breathlessness);
        restlessness = findViewById(R.id.restlessness_sweating_numbness);
        severe_cough_with_breathlessness = findViewById(R.id.severe_cough_with_breathlessness);
        chest_pain = findViewById(R.id.chest_pain);
        confusion = findViewById(R.id.confusion_thoughts);
        foggy_thoughts = findViewById(R.id.foggy_thoughts);
        dizziness = findViewById(R.id.dizziness);
        heart_rate = findViewById(R.id.heart_rate);
        respiratory_rate = findViewById(R.id.respiratory_rate);
        spo2 = findViewById(R.id.spo2);
        advise = findViewById(R.id.advise);
        adviseHead = findViewById(R.id.AdviseHeading);
        submitAdvise = findViewById(R.id.submitAdvise);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        String documentId = getIntent().getStringExtra("documentId");
        String isDoc = getIntent().getStringExtra("isDoc");
        if(isDoc.equals("true")){
            adviseHead.setText("Give Advise");
            advise.setEnabled(true);
            advise.setBackgroundResource(R.drawable.edit_text_background);
            submitAdvise.setVisibility(View.VISIBLE);
        }
        else{
            adviseHead.setText("Advise");
            advise.setEnabled(false);
            submitAdvise.setVisibility(View.INVISIBLE);
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        assert documentId != null;
        DocumentReference docRef = db.collection("Forms").document(documentId);

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                nameTextView.setText("Name of the Patient:\n" + documentSnapshot.getString("Name"));
                localityTextView.setText("Locality/Residence:\n" + documentSnapshot.getString("Address"));
                contactNumberTextView.setText("Contact Number:\n" + documentSnapshot.getString("Mobile Number"));
                genderTextView.setText("Gender:\n" + documentSnapshot.getString("Gender"));
                ageTextView.setText("Age:\n" + documentSnapshot.getString("Age"));
                diabeticTextView.setText("Diabetic:\n" + documentSnapshot.getString("Diabetic"));
                hypersensitiveTextView.setText("Hypersensitive:\n" + documentSnapshot.getString("Hypersensitivity"));
                cadTextView.setText("Coronary Artery Disease:\n" + documentSnapshot.getString("Coronary Artery Disease"));
                yearsOfSufferingTextView.setText("Years of suffering from Diabetes/Hypertension:\n" + documentSnapshot.get(FieldPath.of("Years of suffering from Diabetes/Hypertension")));
                coughAloneTextView.setText("Cough alone:\n" + documentSnapshot.getString("Cough Alone"));
                chronicFatigueTextView.setText("Chronic fatigue after recovery from the COVID-19 infection:\n" + documentSnapshot.getString("Chronic fatigue"));
                nauseaLossAppetiteTextView.setText("Nausea and Loss of appetite:\n" + documentSnapshot.getString("Nausea and Loss of appetite"));
                abdominalDiscomfortTextView.setText("Abdominal Discomfort:\n" + documentSnapshot.getString("Abdominal Discomfort"));
                cough_with_breathlessness.setText("Cough with Breathlessness:\n" + documentSnapshot.getString("Cough with Breathlessness"));
                restlessness.setText("Restlessness, Sweating, and Numbness in extremities:\n" + documentSnapshot.getString("Restlessness, Sweating, and Numbness in extremities"));
                severe_cough_with_breathlessness.setText("Severe Cough with Breathlessness:\n" + documentSnapshot.getString("Sever Cough with Breathlessness"));
                chest_pain.setText("Chest Pain, and Breathlessness with Palpitations:\n" + documentSnapshot.getString("Chest Pain, and Breathlessness with Palpitations"));
                confusion.setText("Confusion with thoughts:\n" + documentSnapshot.getString("Confusion with thoughts"));
                foggy_thoughts.setText("Foggy thoughts:\n" + documentSnapshot.getString("Foggy Thoughts"));
                dizziness.setText("Dizziness:\n" + documentSnapshot.getString("Dizziness"));
                heart_rate.setText("Heart Rate:\n" + documentSnapshot.getString("Heart Rate"));
                respiratory_rate.setText("Respiratory Rate:\n" + documentSnapshot.getString("Respiratory Rate"));
                spo2.setText("SpO2:\n" + documentSnapshot.getString("Spo2"));
                if(Objects.equals(documentSnapshot.getString("Advised"), "true")){
                    advise.setText(documentSnapshot.getString("Advise"));
                    advise.setEnabled(false);
                }
                else{
                    if(isDoc.equals("false")){
                        advise.setText("Advise not given yet. Please check later");
                    }
                    else{
                        advise.setHint("Give Advise");
                    }
                }
            }
        });

        submitAdvise.setOnClickListener(v -> {
            if(advise.getText().toString().isEmpty()){
                Toast.makeText(this, "Advise cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            DocumentReference doc = db.collection("Forms").document(documentId);
            doc.get().addOnSuccessListener(documentSnapshot -> {
               if(documentSnapshot.getString("Advised").equals("true")){
                   Toast.makeText(this, "Advise Already Given", Toast.LENGTH_SHORT).show();
                   return;
               }

                doc.update("Advised", "true");
                doc.update("Advise", advise.getText().toString());
                Toast.makeText(this, "Advise given successfully", Toast.LENGTH_SHORT).show();
            });
            finish();
        });
    }
}