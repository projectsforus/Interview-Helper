package com.example.avatar;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class JobSeekerApplicationForm extends AppCompatActivity {

    TextView Ql, Q2, Q3, Q4;
    EditText A1, A2, A3;
    RadioGroup RG;
    Button BTNApply;
    ImageButton BTNAdd, BTNRemove;

    FirebaseFirestore db;
    ProgressDialog PD;
    String EmployerUID, AnnID;

    LinearLayout LL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_seeker_application_form);

        EmployerUID = getIntent().getStringExtra("EmployerUID");
        AnnID = getIntent().getStringExtra("AnnID");

        db = FirebaseFirestore.getInstance();
        PD = new ProgressDialog(this);

        LL = findViewById(R.id.job_seeker_q_form_parent_layout);
        Ql = findViewById(R.id.job_seeker_form_q1);
        Q2 = findViewById(R.id.job_seeker_form_q2);
        Q3 = findViewById(R.id.job_seeker_form_q3);
        Q4 = findViewById(R.id.job_seeker_form_q4);
        A1 = findViewById(R.id.job_seeker_form_a1);
        A2 = findViewById(R.id.job_seeker_form_a2);
        A3 = findViewById(R.id.job_seeker_form_a3);
        RG = findViewById(R.id.job_seeker_form_rg4);
        BTNApply = findViewById(R.id.job_seeker_form_btn_apply);
        BTNAdd = findViewById(R.id.job_seeker_form_btn_add);
        //BTNRemove = findViewById(R.id.job_seeker_form_btn_remove);

        PD.setMessage("جاري التحميل...");
        PD.show();

        GetQuestions();

        BTNApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAnswers();
            }
        });


        BTNAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ET = new EditText(JobSeekerApplicationForm.this);
                LinearLayout.LayoutParams LP =  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                LP.setMargins(10,10,10,10);
                LP.gravity = Gravity.CENTER;
                ET.setLayoutParams(LP);
                ET.setHint("Enter Your New Question Here...");
                LL.addView(ET, LL.getChildCount() - 3);
                LL.invalidate();
            }
        });
    }

    private void GetQuestions() {
        DocumentReference docRef = db.collection("QuestionForms").document(EmployerUID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Ql.setText(document.getString("Q1"));
                        Q2.setText(document.getString("Q2"));
                        Q3.setText(document.getString("Q3"));
                        Q4.setText(document.getString("Q4"));
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

    private void AddAnswers(){
        PD.setMessage("جاري الرفع...");
        PD.show();

        Map<String, Object> JobApplication = new HashMap<>();
        JobApplication.put("A1", A1.getText().toString());
        JobApplication.put("A2", A2.getText().toString());
        JobApplication.put("A3", A3.getText().toString());
        JobApplication.put("A4", ((RadioButton)findViewById(RG.getCheckedRadioButtonId())).getText());
        JobApplication.put("AnnID", AnnID);
        JobApplication.put("Status", "");
        JobApplication.put("UID", (FirebaseAuth.getInstance()).getCurrentUser().getUid());
        JobApplication.put("comment", "");

        // Add a new document with a generated ID
        db.collection("JobApplications")
                .add(JobApplication)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
        PD.dismiss();
        JobSeekerApplicationForm.this.finish();
    }
}
