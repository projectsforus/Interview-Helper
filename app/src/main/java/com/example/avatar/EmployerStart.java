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

public class EmployerStart extends AppCompatActivity {

    ImageView ProfilePic;
    ImageButton BTNCall, BTNMail, BTNMap;
    TextView TVViewApplication, TVJobAnnouncement, TVQuestionForm;

    FirebaseFirestore db;
    ProgressDialog PD;


    String Age, Name, Phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_start);

        db = FirebaseFirestore.getInstance();
        PD = new ProgressDialog(this);

        ProfilePic = findViewById(R.id.imageView_employer_user_picture);
        BTNCall = findViewById(R.id.button_employer_call);
        BTNMail = findViewById(R.id.button_employer_message);
        //BTNMap = findViewById(R.id.button_employer_map);
        TVViewApplication = findViewById(R.id.employer_start_view_application);
        TVJobAnnouncement = findViewById(R.id.employer_start_job_announcement);
        TVQuestionForm = findViewById(R.id.employer_start_question_form);

        GetEmployerDetails();

        TVJobAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployerStart.this, EmployeerJobAnnouncement.class);
                intent.putExtra("Username", Name);
                startActivity(intent);
            }
        });

        TVViewApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployerStart.this, EmployerViewApplication.class);
                intent.putExtra("Username", Name);
                startActivity(intent);
            }
        });
        TVQuestionForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployerStart.this, EmployerQuestionForm.class);
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

    void GetEmployerDetails(){
        PD.setMessage("جاري التحميل...");
        PD.show();
        DocumentReference docRef = db.collection("Employers").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                PD.dismiss();
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Age = document.getString("Age");
                        Name = document.getString("Name");
                        Phone = document.getString("Phone");
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
