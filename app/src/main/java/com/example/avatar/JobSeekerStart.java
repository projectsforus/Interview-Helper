package com.example.avatar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class JobSeekerStart extends AppCompatActivity {

    ImageView ProfilePic;
    ImageButton BTNCall, BTNMail, BTNMap;
    TextView TVName, TVJob, TVJobAnnouncement, TVMyApplication;

    FirebaseFirestore db;
    ProgressDialog PD;

    String Age, Name, Phone, Job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_seeker_start);

        db = FirebaseFirestore.getInstance();
        PD = new ProgressDialog(this);

        ProfilePic = findViewById(R.id.imageView_job_seeker_user_picture);
        BTNCall = findViewById(R.id.button_job_seeker_call);
        BTNMail = findViewById(R.id.button_job_seeker_message);
        //BTNMap = findViewById(R.id.button_job_seeker_map);
        TVName = findViewById(R.id.job_seeker_start_user_name);
        TVJob = findViewById(R.id.job_seeker_start_user_job);
        TVJobAnnouncement = findViewById(R.id.job_seeker_start_view_list_of_job_ann);
        TVMyApplication = findViewById(R.id.job_seeker_start_my_application);

        GetJobSeekerDetails();

        TVJobAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobSeekerStart.this, JobSeekerAvailableAnnouncements.class);
                intent.putExtra("Username", Name);
                startActivity(intent);
            }
        });

        TVMyApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobSeekerStart.this, JobSeekerApplicationsList.class);
                intent.putExtra("Username", Name);
                startActivity(intent);
            }
        });

        BTNCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + Phone));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        BTNMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{FirebaseAuth.getInstance().getCurrentUser().getEmail()});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    void GetJobSeekerDetails(){
        PD.setMessage("جاري التحميل...");
        PD.show();
        DocumentReference docRef = db.collection("JobSeekers").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    PD.dismiss();
                    if (document.exists()) {
                        Age = document.getString("Age");
                        Name = document.getString("Name");
                        Phone = document.getString("Phone");
                        Job = document.getString("Job");
                        TVJob.setText(Job);
                        TVName.setText(Name);
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }
}
