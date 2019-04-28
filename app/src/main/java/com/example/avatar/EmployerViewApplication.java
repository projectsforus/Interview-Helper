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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EmployerViewApplication extends AppCompatActivity {

    ImageView ProfilePic;
    TextView UserName, UserMail;
    ListView ApplicationsList;

    FirebaseFirestore db;
    ProgressDialog PD;
    List<JobAnnouncementClass> Jobs;
    ArrayAdapter<String> adapter;
    List<String> Names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_view_application);

        db = FirebaseFirestore.getInstance();
        PD = new ProgressDialog(this);
        Jobs = new ArrayList<>();

        ProfilePic = findViewById(R.id.imageView_view_application_user_picture);
        UserName = findViewById(R.id.job_seeker_view_application_user_name);
        UserMail = findViewById(R.id.job_seeker_view_application_user_mail);
        ApplicationsList = findViewById(R.id.employer_view_application_listview);

        UserMail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        UserName.setText(getIntent().getStringExtra("Username"));

        GetData();

        ApplicationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EmployerViewApplication.this, EmployerAppliedPeople.class);
                intent.putExtra("AnnouncementID", Jobs.get(position).ID);
                intent.putExtra("Username", getIntent().getStringExtra("Username"));
                startActivity(intent);
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
                (EmployerViewApplication.this, android.R.layout.simple_list_item_1, Names);

        ApplicationsList.setAdapter(adapter);
    }

    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
