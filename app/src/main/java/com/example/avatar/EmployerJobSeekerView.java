package com.example.avatar;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EmployerJobSeekerView extends AppCompatActivity {

    ImageView UserPicture;
    TextView UserName;
    EditText Description;
    Button ContactInfo;
    ImageView Add, Remove;

    FirebaseFirestore db;
    ProgressDialog PD;
    JobApplicationClass CurrentApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_job_seeker_view);

        db = FirebaseFirestore.getInstance();
        PD = new ProgressDialog(this);

        CurrentApplication = getIntent().getParcelableExtra("CurrentJob");

        UserPicture = findViewById(R.id.employer_jobSeeker_view_user_picture);
        UserName = findViewById(R.id.employer_jobSeeker_view_user_name);
        Description = findViewById(R.id.employer_jobSeeker_view_description);
        ContactInfo = findViewById(R.id.employer_jobSeeker_view_btn_contact);
        Add = findViewById(R.id.employer_jobSeeker_view_add_btn);
        Remove = findViewById(R.id.employer_jobSeeker_view_remove_btn);

        Description.setText(CurrentApplication.Comment);
        GetUsername();

        final DocumentReference washingtonRef = db.collection("JobApplications").document(CurrentApplication.ApplicationID);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                washingtonRef
                        .update("Status", "Y",
                                "comment", Description.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error updating document", e);
                            }
                        });
                Toast.makeText(EmployerJobSeekerView.this, "تمت الموافقة على الطلب", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                washingtonRef
                        .update("Status", "N",
                                "comment", Description.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error updating document", e);
                            }
                        });
                Toast.makeText(EmployerJobSeekerView.this, "تم رفض الطلب", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void GetUsername() {
        PD.setMessage("جاري التحميل...");
        PD.show();
        DocumentReference docRef;
        docRef = db.collection("JobSeekers").document(CurrentApplication.ApplierID);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserName.setText(document.getString("Name"));
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
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
}
