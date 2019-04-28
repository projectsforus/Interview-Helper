package com.example.avatar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class JobSeekerApplicationsList extends AppCompatActivity {

    ImageView UserPic;
    TextView UserName;
    ListView UserApplications;

    FirebaseFirestore db;
    ProgressDialog PD;
    List<JobApplicationClass> Applications;
    List<JobSeekerApplicationsState> Names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_seeker_applications_list);

        db = FirebaseFirestore.getInstance();
        PD = new ProgressDialog(this);
        Applications = new ArrayList<>();

        UserPic = findViewById(R.id.job_seeker_applications_list_user_picture);
        UserName = findViewById(R.id.job_seeker_applications_list__user_name);
        UserApplications = findViewById(R.id.job_seeker_applications_list__listview);

        UserName.setText(getIntent().getStringExtra("Username"));


        getUserAnnouncements();
    }

    private void getUserAnnouncements() {
        PD.setMessage("جاري التحميل...");
        PD.show();

        db.collection("JobApplications")
                .whereEqualTo("UID", (FirebaseAuth.getInstance()).getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().size() < 1)
                                PD.dismiss();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayList<String> Answers = new ArrayList<>();
                                Answers.add(document.get("A1").toString());
                                Answers.add(document.get("A2").toString());
                                Answers.add(document.get("A3").toString());
                                Answers.add(document.get("A4").toString());
                                Applications.add(new JobApplicationClass
                                        (document.get("AnnID").toString(),
                                                document.get("Status").toString(),
                                                document.get("UID").toString(),
                                                document.get("comment").toString(),
                                                document.getId(),
                                                Answers));
                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                            GetJobNames();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                            PD.dismiss();
                        }
                    }
                });
    }

    private void GetJobNames() {
        Names = new ArrayList<>();
        for(final JobApplicationClass X : Applications) {

            DocumentReference docRef = db.collection("JobAnnouncements")
                    .document(X.AnnouncementID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Names.add(new JobSeekerApplicationsState(document.getString("JobName"), X.Status, X.Comment));
                            Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d("TAG", "No such document");
                        }
                    } else {
                        Log.d("TAG", "get failed with ", task.getException());
                    }
                    afterGettingData();
                }
            });
        }
    }

    private void afterGettingData() {
        PD.dismiss();
        JobSeekerApplicationsListAdapter listAdapter = new JobSeekerApplicationsListAdapter(this, R.layout.job_seeker_applications_list_item, Names);
        UserApplications.setAdapter(listAdapter);
    }
}
