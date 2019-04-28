package com.example.avatar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class JobSeekerAvailableAnnouncements extends AppCompatActivity {

    ImageView UserPic;
    TextView UserName;
    ListView AvailableJobs;

    FirebaseFirestore db;
    ProgressDialog PD;
    List<JobAnnouncementClass> Jobs;
    ArrayAdapter<String> adapter;
    List<String> Names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_seeker_available_announcements);

        db = FirebaseFirestore.getInstance();
        PD = new ProgressDialog(this);
        Jobs = new ArrayList<>();

        UserPic = findViewById(R.id.job_seeker_available_announcements_user_picture);
        UserName = findViewById(R.id.job_seeker_available_announcements_user_name);
        AvailableJobs = findViewById(R.id.job_seeker_available_announcements_listview);


        UserName.setText(getIntent().getStringExtra("Username"));

        GetData();

        AvailableJobs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                db.collection("JobApplications")
                        .whereEqualTo("AnnID", Jobs.get(position).ID)
                        .whereEqualTo("UID", FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("TAG", document.getId() + " => " + document.getData());
                                    }

                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                                if(task.getResult().size() > 0)
                                    Toast.makeText(JobSeekerAvailableAnnouncements.this, "لقد قمت بالتقديم على هذه الوظيفة من قبل.", Toast.LENGTH_LONG).show();
                                else{
                                    Intent intent = new Intent(JobSeekerAvailableAnnouncements.this, JobSeekerAnnouncementDescription.class);
                                    //intent.putExtra("JobName", Jobs.get(position).JobName);
                                    intent.putExtra("CurrentJob", Jobs.get(position));
                                    startActivity(intent);
                                }
                            }
                        });
            }
        });
    }

    protected void GetData(){
        PD.setMessage("جاري التحميل...");
        PD.show();
        db.collection("JobAnnouncements")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().size() < 1)
                                PD.dismiss();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                    Jobs.add(new JobAnnouncementClass
                                            (document.get("JobName").toString(),
                                                    document.get("JobDept").toString(),
                                                    document.get("JobDesc").toString(),
                                                    document.getId(),
                                                    document.get("UID").toString()));
                                afterGettingData();
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void afterGettingData() {
        PD.dismiss();
        Names = new ArrayList<>();
        for(JobAnnouncementClass X : Jobs){
            Names.add(X.JobName);
        }
        adapter = new ArrayAdapter<>
                (JobSeekerAvailableAnnouncements.this, android.R.layout.simple_list_item_1, Names);

        AvailableJobs.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
