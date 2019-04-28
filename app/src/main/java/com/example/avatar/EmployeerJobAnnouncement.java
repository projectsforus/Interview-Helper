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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EmployeerJobAnnouncement extends AppCompatActivity {

    ImageView ProfilePic;
    TextView UserName, UserMail;
    ListView AnnouncementList;
    ImageButton BTNAdd, BTNRemove;

    FirebaseFirestore db;
    ProgressDialog PD;
    List<JobAnnouncementClass> Jobs;
    ArrayAdapter<String> adapter;
    List<String> Names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employeer_job_announcement);

        db = FirebaseFirestore.getInstance();
        PD = new ProgressDialog(this);
        Jobs = new ArrayList<>();

        ProfilePic = findViewById(R.id.imageView_job_announcement_user_picture);
        UserName = findViewById(R.id.job_seeker_job_announcement_user_name);
        UserMail = findViewById(R.id.job_seeker_job_announcement_user_mail);
        AnnouncementList = findViewById(R.id.employer_job_announcement_listview);
        BTNAdd = findViewById(R.id.employer_job_announcement_add_btn);
        BTNRemove = findViewById(R.id.employer_job_announcement_remove_btn);

        UserMail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        UserName.setText(getIntent().getStringExtra("Username"));

        GetData();

        BTNAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeerJobAnnouncement.this, EmployerNewAnnouncement.class);
                startActivity(intent);
            }
        });

        AnnouncementList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                BTNRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteJob(Jobs.get(position).ID);
                        Jobs.remove(position);
                        Names.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    protected void GetData(){
        PD.setMessage("جاري التحميل...");
        PD.show();

        db.collection("JobAnnouncements")
                .whereEqualTo("UID", (FirebaseAuth.getInstance()).getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Jobs.add(new JobAnnouncementClass
                                        (document.get("JobName").toString(),
                                                document.get("JobDept").toString(),
                                                document.get("JobDesc").toString(),
                                                document.getId(),
                                                document.get("UID").toString()));
                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                            afterGettingData();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
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
                (EmployeerJobAnnouncement.this, android.R.layout.simple_list_item_1, Names);
        AnnouncementList.setAdapter(adapter);
    }

    private void deleteJob(String Job_id){
        PD.setMessage("جاري التحميل...");
        PD.show();
        db.collection("JobAnnouncements").document(Job_id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error deleting document", e);
                    }
                });
        db.collection("JobApplications")
                .whereEqualTo("AnnID", Job_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                String idDelete = document.getId();
                                db.collection("JobApplications").document(idDelete)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error deleting document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
        PD.dismiss();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}