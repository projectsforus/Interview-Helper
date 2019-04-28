package com.example.avatar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class EmployerAppliedPeople extends AppCompatActivity {

    ImageView ProfilePic;
    TextView UserName, UserMail;
    ListView AppliedPeopleList;

    FirebaseFirestore db;
    ProgressDialog PD;
    List<JobApplicationClass> Jobs;
    List<String> Names;
    AppliedPeopleListAdapter customAdapter;

    String AnnouncementID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_applied_people);

        AnnouncementID = getIntent().getStringExtra("AnnouncementID");

        db = FirebaseFirestore.getInstance();
        PD = new ProgressDialog(this);
        Jobs = new ArrayList<>();

        ProfilePic = findViewById(R.id.imageView_applied_people_user_picture);
        UserName = findViewById(R.id.job_seeker_applied_people_user_name);
        UserMail = findViewById(R.id.job_seeker_applied_people_user_mail);
        AppliedPeopleList = findViewById(R.id.employer_applied_people_listview);

        UserMail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        UserName.setText(getIntent().getStringExtra("Username"));

        GetData();
    }

    protected void GetData(){
        final List<String> Answers = new ArrayList<>();
        PD.setMessage("جاري التحميل...");
        PD.show();
        db.collection("JobApplications")
                .whereEqualTo("AnnID", AnnouncementID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().size() < 1)
                                PD.dismiss();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Answers.add(document.get("A1").toString());
                                Answers.add(document.get("A2").toString());
                                Answers.add(document.get("A3").toString());
                                Answers.add(document.get("A4").toString());
                                Jobs.add(new JobApplicationClass
                                        (document.get("AnnID").toString(),
                                                document.get("Status").toString(),
                                                document.get("UID").toString(),
                                                document.get("comment").toString(),
                                                document.getId(),
                                                Answers));
                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                            afterGettingData();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                            PD.dismiss();
                        }
                    }
                });
    }

    private void afterGettingData() {
        DocumentReference docRef;
        Names = new ArrayList<>();
        for(JobApplicationClass X : Jobs){
            //get UserNames
            docRef = db.collection("JobSeekers").document(X.ApplierID);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Names.add(document.getString("Name"));
                            Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                            AppliedPeopleListAdapter.Extras = Jobs;
                            customAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("TAG", "No such document");
                        }
                    } else {
                        Log.d("TAG", "get failed with ", task.getException());
                    }
                    PD.dismiss();
                }

            });
        }
        customAdapter = new AppliedPeopleListAdapter(EmployerAppliedPeople.this, R.layout.applied_people_list_item, Names);
        AppliedPeopleList.setAdapter(customAdapter);
    }

    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
