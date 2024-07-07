package com.ishaanbhela.aftercovidtracker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ishaanbhela.aftercovidtracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity implements prevFormItemAdapter.OnItemClickListener, ArticlesAdapter.OnArticleItemClickListener, VideosAdapter.OnVideoItemClickListener {
    private TextView username;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private RecyclerView prevForms;
    private RecyclerView articles;
    private RecyclerView videos;
    private Button signOut;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    private TextView selfAssess;
    List<prevFormModel> formItemList = new ArrayList<>();
    List<ArticlesModel> articlesModelList = new ArrayList<>();
    List<VideoModel> videoModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        username = findViewById(R.id.username);
        prevForms = findViewById(R.id.prevFormList);
        selfAssess = findViewById(R.id.selfAssess);
        articles = findViewById(R.id.ArticleList);
        videos = findViewById(R.id.VideoList);
        signOut = findViewById(R.id.signOutBtn);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fetchUserName();


        // Setting Previous Form Adapters
        prevForms.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        FetchPreviousForms();
//        CollectionReference colRef = db.collection("Forms");
//        Query query = colRef.whereEqualTo("UserID", auth.getCurrentUser().getUid());
//        query.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                for (QueryDocumentSnapshot document : task.getResult()) {
//                    String MonthYear = document.getString("MonthYear");
//                    formItemList.add(new prevFormModel(MonthYear, document.getId()));
//                }
//                prevFormItemAdapter formItemAdapter = new prevFormItemAdapter(formItemList, this);
//                prevForms.setAdapter(formItemAdapter);
//            }
//        });

        articles.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        articlesModelList.add(new ArticlesModel("https://www.healthline.com/health/coronavirus-prevention", "https://i.imgur.com/qz7Uh7p.png"));
        ArticlesAdapter articlesAdapter = new ArticlesAdapter(articlesModelList, this);
        articles.setAdapter(articlesAdapter);

        videos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        videoModelList.add(new VideoModel("https://www.youtube.com/watch?v=FC4soCjxSOQ&pp=ygUWY29yb25hdmlydXMgcHJldmVudGlvbg%3D%3D", "https://i.imgur.com/4rYIo7V.png"));
        VideosAdapter videosAdapter = new VideosAdapter(videoModelList, this);
        videos.setAdapter(videosAdapter);


        selfAssess.setOnClickListener(v -> {
            SimpleDateFormat monthYearFormat = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
            String currentMonthYear = monthYearFormat.format(new Date());

            CollectionReference formsRef = db.collection("Forms");
            Query q = formsRef
                    .whereEqualTo("UserID", auth.getCurrentUser().getUid())
                    .whereEqualTo("MonthYear", currentMonthYear)
                    .limit(1);

            q.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        Toast.makeText(this, "You can only submit one form per month.", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(Home.this, formFill.class));
                    }
                } else {
                    Toast.makeText(this, "Error checking form submissions", Toast.LENGTH_SHORT).show();
                }

            });

        });


        signOut.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(Home.this, MainActivity.class));
            finish();
        });


        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result ->{
                    if(result.getResultCode() == RESULT_OK){
                        System.out.println("Hello World");
                        FetchPreviousForms();
                    }
                }
        );
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
                                username.setText("Hello, " + name);
                            } else {
                                Toast.makeText(Home.this, "No profile found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Home.this, "Error getting profile: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onItemClick(int position) {
        prevFormModel clickedItem = formItemList.get(position);
        Intent i = new Intent(Home.this, FormView.class);
        i.putExtra("documentId", clickedItem.getFormID());
        i.putExtra("isDoc", "false");
        startActivity(i);
    }

    @Override
    public void onArticleItemClick(int position) {
        ArticlesModel clickedItem = articlesModelList.get(position);
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(clickedItem.getURL()));
        startActivity(i);
    }

    @Override
    public void onVideoItemClick(int position) {
        VideoModel clickedItem = videoModelList.get(position);
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(clickedItem.getVidURL()));
        startActivity(i);
    }


    public void FetchPreviousForms(){
        formItemList.clear();
        CollectionReference colRef = db.collection("Forms");
        Query query = colRef.whereEqualTo("UserID", auth.getCurrentUser().getUid());
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String MonthYear = document.getString("MonthYear");
                    formItemList.add(new prevFormModel(MonthYear, document.getId()));
                }
                prevFormItemAdapter formItemAdapter = new prevFormItemAdapter(formItemList, this);
                prevForms.setAdapter(formItemAdapter);
            }
        });
    }
}