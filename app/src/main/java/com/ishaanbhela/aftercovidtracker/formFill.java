package com.ishaanbhela.aftercovidtracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class formFill extends AppCompatActivity {

    // Demographic Data
    EditText nameInput, localityInput, contactNumberInput;
    RadioGroup genderRadioGroup, ageRadioGroup, diabeticRadioGroup, hypersensitiveRadioGroup, cadRadioGroup, yearsOfSufferingRadioGroup;

    // Mild Symptoms of Post COVID Complications
    RadioGroup coughAloneRadioGroup, chronicFatigueRadioGroup, nauseaLossAppetiteRadioGroup, abdominalDiscomfortRadioGroup;

    // Moderate Symptoms of Post COVID Complications
    RadioGroup coughBreathlessnessRadioGroup, restlessnessRadioGroup;

    // Severe Symptoms of Post COVID Complications
    RadioGroup severeCoughBreathlessnessRadioGroup, chestPainRadioGroup, confusionRadioGroup, foggyThoughtsRadioGroup, dizzinessRadioGroup;

    // Vital Signs
    RadioGroup heartRateRadioGroup, respiratoryRateRadioGroup, spo2RadioGroup;

    // Variables to hold selected values
    String gender = "";
    String age = "";
    String diabetic = "";
    String hypersensitive = "";
    String cad = "";
    String yearsOfSuffering = "";

    String coughAlone = "";
    String chronicFatigue = "";
    String nauseaLossAppetite = "";
    String abdominalDiscomfort = "";

    String coughBreathlessness = "";
    String restlessness = "";

    String severeCoughBreathlessness = "";
    String chestPain = "";
    String confusion = "";
    String foggyThoughts = "";
    String dizziness = "";

    String heartRate = "";
    String respiratoryRate = "";
    String spo2 = "";
    Button submit;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_fill);

        // Initialize EditText inputs
        nameInput = findViewById(R.id.name);
        localityInput = findViewById(R.id.locality);
        contactNumberInput = findViewById(R.id.contact_number);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize RadioGroups for demographic data
        genderRadioGroup = findViewById(R.id.gender);
        ageRadioGroup = findViewById(R.id.age);
        diabeticRadioGroup = findViewById(R.id.diabetic);
        hypersensitiveRadioGroup = findViewById(R.id.hypersensitive);
        cadRadioGroup = findViewById(R.id.cad);
        yearsOfSufferingRadioGroup = findViewById(R.id.years_of_suffering);

        // Initialize RadioGroups for mild symptoms
        coughAloneRadioGroup = findViewById(R.id.cough_alone);
        chronicFatigueRadioGroup = findViewById(R.id.chronic_fatigue);
        nauseaLossAppetiteRadioGroup = findViewById(R.id.nausea_loss_appetite);
        abdominalDiscomfortRadioGroup = findViewById(R.id.abdominal_discomfort);

        // Initialize RadioGroups for moderate symptoms
        coughBreathlessnessRadioGroup = findViewById(R.id.cough_breathlessness);
        restlessnessRadioGroup = findViewById(R.id.restlessness);

        // Initialize RadioGroups for severe symptoms
        severeCoughBreathlessnessRadioGroup = findViewById(R.id.severe_cough_breathlessness);
        chestPainRadioGroup = findViewById(R.id.chest_pain);
        confusionRadioGroup = findViewById(R.id.confusion);
        foggyThoughtsRadioGroup = findViewById(R.id.foggy_thoughts);
        dizzinessRadioGroup = findViewById(R.id.dizziness);

        // Initialize RadioGroups for vital signs
        heartRateRadioGroup = findViewById(R.id.heart_rate);
        respiratoryRateRadioGroup = findViewById(R.id.respiratory_rate);
        spo2RadioGroup = findViewById(R.id.spo2);

        submit = findViewById(R.id.submit);

        // Set initial values to ""
        clearRadioGroupSelection(genderRadioGroup);
        clearRadioGroupSelection(ageRadioGroup);
        clearRadioGroupSelection(diabeticRadioGroup);
        clearRadioGroupSelection(hypersensitiveRadioGroup);
        clearRadioGroupSelection(cadRadioGroup);
        clearRadioGroupSelection(yearsOfSufferingRadioGroup);

        clearRadioGroupSelection(coughAloneRadioGroup);
        clearRadioGroupSelection(chronicFatigueRadioGroup);
        clearRadioGroupSelection(nauseaLossAppetiteRadioGroup);
        clearRadioGroupSelection(abdominalDiscomfortRadioGroup);

        clearRadioGroupSelection(coughBreathlessnessRadioGroup);
        clearRadioGroupSelection(restlessnessRadioGroup);

        clearRadioGroupSelection(severeCoughBreathlessnessRadioGroup);
        clearRadioGroupSelection(chestPainRadioGroup);
        clearRadioGroupSelection(confusionRadioGroup);
        clearRadioGroupSelection(foggyThoughtsRadioGroup);
        clearRadioGroupSelection(dizzinessRadioGroup);

        clearRadioGroupSelection(heartRateRadioGroup);
        clearRadioGroupSelection(respiratoryRateRadioGroup);
        clearRadioGroupSelection(spo2RadioGroup);

        // Set OnClickListener for each RadioGroup
        setRadioGroupClickListener(genderRadioGroup, "gender");
        setRadioGroupClickListener(ageRadioGroup, "age");
        setRadioGroupClickListener(diabeticRadioGroup, "diabetic");
        setRadioGroupClickListener(hypersensitiveRadioGroup, "hypersensitive");
        setRadioGroupClickListener(cadRadioGroup, "cad");
        setRadioGroupClickListener(yearsOfSufferingRadioGroup, "yearsOfSuffering");

        setRadioGroupClickListener(coughAloneRadioGroup, "coughAlone");
        setRadioGroupClickListener(chronicFatigueRadioGroup, "chronicFatigue");
        setRadioGroupClickListener(nauseaLossAppetiteRadioGroup, "nauseaLossAppetite");
        setRadioGroupClickListener(abdominalDiscomfortRadioGroup, "abdominalDiscomfort");

        setRadioGroupClickListener(coughBreathlessnessRadioGroup, "coughBreathlessness");
        setRadioGroupClickListener(restlessnessRadioGroup, "restlessness");

        setRadioGroupClickListener(severeCoughBreathlessnessRadioGroup, "severeCoughBreathlessness");
        setRadioGroupClickListener(chestPainRadioGroup, "chestPain");
        setRadioGroupClickListener(confusionRadioGroup, "confusion");
        setRadioGroupClickListener(foggyThoughtsRadioGroup, "foggyThoughts");
        setRadioGroupClickListener(dizzinessRadioGroup, "dizziness");

        setRadioGroupClickListener(heartRateRadioGroup, "heartRate");
        setRadioGroupClickListener(respiratoryRateRadioGroup, "respiratoryRate");
        setRadioGroupClickListener(spo2RadioGroup, "spo2");

        fetchNameAndPhNo();

        submit.setOnClickListener(v -> {
            if(gender.isEmpty() || age.isEmpty() || diabetic.isEmpty() || hypersensitive.isEmpty() || cad.isEmpty() ||
                    yearsOfSuffering.isEmpty() || coughAlone.isEmpty() || chronicFatigue.isEmpty() || nauseaLossAppetite.isEmpty() ||
                    abdominalDiscomfort.isEmpty() || coughBreathlessness.isEmpty() || restlessness.isEmpty() || severeCoughBreathlessness.isEmpty() ||
                    chestPain.isEmpty() || confusion.isEmpty() || foggyThoughts.isEmpty() || dizziness.isEmpty() || heartRate.isEmpty() ||
                    respiratoryRate.isEmpty() || spo2.isEmpty()){
                Toast.makeText(this, "Please Select all the options", Toast.LENGTH_SHORT).show();
            }
            else{
                storeFormInFirebase();
            }
        });
    }

    private void storeFormInFirebase() {

        SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
        String currentMonthYear = monthYearFormat.format(new Date());

        Map<String, String> data = new HashMap<>();
        data.put("Name", nameInput.getText().toString());
        data.put("Address", localityInput.getText().toString());
        data.put("Mobile Number", contactNumberInput.getText().toString());
        data.put("Gender", gender);
        data.put("Age", age);
        data.put("Diabetic", diabetic);
        data.put("Hypersensitivity", hypersensitive);
        data.put("Coronary Artery Disease", cad);
        data.put("Years of suffering from Diabetes/Hypertension", yearsOfSuffering);
        data.put("Cough Alone", coughAlone);
        data.put("Chronic fatigue", chronicFatigue);
        data.put("Nausea and Loss of appetite", nauseaLossAppetite);
        data.put("Abdominal Discomfort", abdominalDiscomfort);
        data.put("Cough with Breathlessness", coughBreathlessness);
        data.put("Restlessness, Sweating, and Numbness in extremities", restlessness);
        data.put("Sever Cough with Breathlessness", severeCoughBreathlessness);
        data.put("Chest Pain, and Breathlessness with Palpitations", chestPain);
        data.put("Confusion with thoughts", confusion);
        data.put("Foggy Thoughts", foggyThoughts);
        data.put("Dizziness", dizziness);
        data.put("Heart Rate", heartRate);
        data.put("Respiratory Rate", respiratoryRate);
        data.put("Spo2", spo2);
        data.put("Date", new Date().toString());
        data.put("MonthYear", currentMonthYear);
        data.put("Advised", "false");
        data.put("Advise", "");
        data.put("UserID", Objects.requireNonNull(auth.getCurrentUser()).getUid());

        db.collection("Forms")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Form Submitted Successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, new Intent());
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error Submitting Form", Toast.LENGTH_SHORT).show();
                });

    }

    // Method to clear selection in a RadioGroup
    private void clearRadioGroupSelection(RadioGroup radioGroup) {
        radioGroup.clearCheck();
    }

    // Method to set OnClickListener for a RadioGroup
    private void setRadioGroupClickListener(RadioGroup radioGroup, String variableName) {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadioButton = findViewById(checkedId);
            if (checkedRadioButton != null) {
                // Update the corresponding variable with selected value
                switch (variableName) {
                    case "gender":
                        gender = checkedRadioButton.getText().toString();
                        break;
                    case "age":
                        age = checkedRadioButton.getText().toString();
                        break;
                    case "diabetic":
                        diabetic = checkedRadioButton.getText().toString();
                        break;
                    case "hypersensitive":
                        hypersensitive = checkedRadioButton.getText().toString();
                        break;
                    case "cad":
                        cad = checkedRadioButton.getText().toString();
                        break;
                    case "yearsOfSuffering":
                        yearsOfSuffering = checkedRadioButton.getText().toString();
                        break;
                    case "coughAlone":
                        coughAlone = checkedRadioButton.getText().toString();
                        break;
                    case "chronicFatigue":
                        chronicFatigue = checkedRadioButton.getText().toString();
                        break;
                    case "nauseaLossAppetite":
                        nauseaLossAppetite = checkedRadioButton.getText().toString();
                        break;
                    case "abdominalDiscomfort":
                        abdominalDiscomfort = checkedRadioButton.getText().toString();
                        break;
                    case "coughBreathlessness":
                        coughBreathlessness = checkedRadioButton.getText().toString();
                        break;
                    case "restlessness":
                        restlessness = checkedRadioButton.getText().toString();
                        break;
                    case "severeCoughBreathlessness":
                        severeCoughBreathlessness = checkedRadioButton.getText().toString();
                        break;
                    case "chestPain":
                        chestPain = checkedRadioButton.getText().toString();
                        break;
                    case "confusion":
                        confusion = checkedRadioButton.getText().toString();
                        break;
                    case "foggyThoughts":
                        foggyThoughts = checkedRadioButton.getText().toString();
                        break;
                    case "dizziness":
                        dizziness = checkedRadioButton.getText().toString();
                        break;
                    case "heartRate":
                        heartRate = checkedRadioButton.getText().toString();
                        break;
                    case "respiratoryRate":
                        respiratoryRate = checkedRadioButton.getText().toString();
                        break;
                    case "spo2":
                        spo2 = checkedRadioButton.getText().toString();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void fetchNameAndPhNo(){
        FirebaseUser user = auth.getCurrentUser();
        String PhoneNumber = user.getPhoneNumber();
        contactNumberInput.setText(PhoneNumber);
        contactNumberInput.setEnabled(false);
        String uid = user.getUid();
        DocumentReference userRef = db.collection("users").document(uid);
        userRef.get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentSnapshot doc = task.getResult();
                        if(doc.exists()){
                            nameInput.setText(doc.getString("name"));
                            nameInput.setEnabled(false);
                        }
                        else{
                            Toast.makeText(this, "User Profile Not Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(this, "Error getting User Profile", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
