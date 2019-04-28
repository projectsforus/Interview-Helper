package com.example.avatar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterAdditionalInfo extends AppCompatActivity {

    Button Start;
    EditText Name, Age, Job, Phone, EXP;

    FirebaseAuth mAuth;
    ProgressDialog PD;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_additional_info);

        PD = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Start = findViewById(R.id.additional_info_btn_start);
        Name = findViewById(R.id.additional_info_et_name);
        Age = findViewById(R.id.additional_info_et_age);
        Job = findViewById(R.id.additional_info_et_job);
        Phone = findViewById(R.id.additional_info_et_phone);
        EXP = findViewById(R.id.additional_info_et_exp);


        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Name.getText().toString().isEmpty()
                || Age.getText().toString().isEmpty()
                || Job.getText().toString().isEmpty()
                || Phone.getText().toString().isEmpty()
                || EXP.getText().toString().isEmpty())
                    Toast.makeText(RegisterAdditionalInfo.this,"يجب إدخال جميع الحقول قبل البدء", Toast.LENGTH_LONG).show();
                else{
                    PD.setMessage("جاري إنشاء حساب جديد...");
                    PD.show();
                    mAuth.createUserWithEmailAndPassword(getIntent().getStringExtra("Uname")
                            , getIntent().getStringExtra("Upass"))
                            .addOnCompleteListener(RegisterAdditionalInfo.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    PD.dismiss();
                                    if (task.isSuccessful()) {

                                        Map<String, Object> data = new HashMap<>();
                                        data.put("Identifier", mAuth.getCurrentUser().getEmail());
                                        data.put("Age", Age.getText().toString());
                                        data.put("Job", Job.getText().toString());
                                        data.put("Name", Name.getText().toString());
                                        data.put("Phone",Phone.getText().toString());
                                        data.put("EXP",EXP.getText().toString());

                                        db.collection("JobSeekers").document(mAuth.getCurrentUser().getUid())
                                                .set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                PD.dismiss();
                                                Intent intent = new Intent(RegisterAdditionalInfo.this, JobSeekerStart.class);
                                                finish();
                                                startActivity(intent);
                                            }
                                        });
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("TAG", "createUserWithEmail:success");
                                        //FirebaseUser user = mAuth.getCurrentUser();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterAdditionalInfo.this,
                                                "اسم المستخدم أو كلمة المرور خطأ",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterAdditionalInfo.this, Register.class);
                                        finish();
                                        startActivity(intent);
                                    }
                                }
                            });
                }
            }
        });
    }
}
