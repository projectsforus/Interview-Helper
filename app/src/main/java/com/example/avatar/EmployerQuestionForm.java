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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EmployerQuestionForm extends AppCompatActivity {

    TextView Ql, Q2, Q3, Q4;
    EditText A1, A2, A3;
    RadioGroup RG;
    Button BTNPublish;
    ImageButton BTNAdd, BTNRemove;

    FirebaseFirestore db;
    ProgressDialog PD;

    LinearLayout LL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_question_form);

        db = FirebaseFirestore.getInstance();
        PD = new ProgressDialog(this);


        LL = findViewById(R.id.employer_q_form_parent_layout);
        Ql = findViewById(R.id.employer_form_q1);
        Q2 = findViewById(R.id.employer_form_q2);
        Q3 = findViewById(R.id.employer_form_q3);
        Q4 = findViewById(R.id.employer_form_q4);
        A1 = findViewById(R.id.employer_form_a1);
        A2 = findViewById(R.id.employer_form_a2);
        A3 = findViewById(R.id.employer_form_a3);
        RG = findViewById(R.id.employer_form_rg4);
        BTNPublish = findViewById(R.id.employer_form_btn_publish);
        BTNAdd = findViewById(R.id.employer_form_btn_add);
        //BTNRemove = findViewById(R.id.employer_form_btn_remove);

        BTNPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PD.setMessage("جاري الرفع...");
                PD.show();
                Map<String, Object> Questions = new HashMap<>();
                Questions.put("Q1", Ql.getText().toString());
                Questions.put("Q2", Q2.getText().toString());
                Questions.put("Q3", Q3.getText().toString());
                Questions.put("Q4", Q4.getText().toString());

                db.collection("QuestionForms").document(
                        (FirebaseAuth.getInstance()).getCurrentUser().getUid()
                )
                        .set(Questions)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error writing document", e);
                            }
                        });
                PD.dismiss();
                EmployerQuestionForm.this.finish();
            }
        });

        BTNAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ET = new EditText(EmployerQuestionForm.this);
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
}
