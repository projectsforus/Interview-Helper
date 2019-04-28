package com.example.avatar;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EmployerNewAnnouncement extends AppCompatActivity {

    EditText JobName, JobDept, JobDesc;
    Button BTNPublish;
    ImageButton BTNAdd, BTNRemove;

    FirebaseFirestore db;
    ProgressDialog PD;

    LinearLayout LL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_new_announcement);

        db = FirebaseFirestore.getInstance();
        PD = new ProgressDialog(this);

        LL = findViewById(R.id.employer_new_announcement_parent_layout);
        JobName = findViewById(R.id.new_announcement_job_name);
        JobDept = findViewById(R.id.new_announcement_job_department);
        JobDesc = findViewById(R.id.new_announcement_job_description);
        BTNPublish = findViewById(R.id.new_announcement_btn_publish);
        BTNAdd = findViewById(R.id.employer_field_announcement_add_btn);
        //BTNRemove = findViewById(R.id.employer_field_announcement_remove_btn);

        BTNPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PD.setMessage("جاري الرفع...");
                PD.show();
                // Create a new user with a first and last name
                Map<String, Object> JobAnnouncement = new HashMap<>();
                JobAnnouncement.put("JobName", JobName.getText().toString());
                JobAnnouncement.put("JobDept", JobDept.getText().toString());
                JobAnnouncement.put("JobDesc", JobDesc.getText().toString());
                JobAnnouncement.put("UID", (FirebaseAuth.getInstance()).getCurrentUser().getUid());

                // Add a new document with a generated ID
                db.collection("JobAnnouncements")
                        .add(JobAnnouncement)
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
                EmployerNewAnnouncement.this.finish();
            }
        });


        BTNAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ET = new EditText(EmployerNewAnnouncement.this);
                LinearLayout.LayoutParams LP =  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                LP.setMargins(40,0,40,30);
                LP.gravity = Gravity.CENTER;
                ET.setLayoutParams(LP);
                ET.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                ET.setBackgroundResource(R.drawable.text_view);
                ET.setHint("Enter Info Here..");
                LL.addView(ET, LL.getChildCount() - 1);
                LL.invalidate();
            }
        });

        /*BTNRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LL.getChildCount() > 3)
                    LL.removeViewAt(LL.getChildCount() - 1);
            }
        });*/
    }
}
