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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText Username, Password;
    Button Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Username = findViewById(R.id.register_et_username);
        Password = findViewById(R.id.register_et_password);
        Register = findViewById(R.id.register_btn_register);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameValue = Username.getText().toString();
                String passwordValue = Password.getText().toString();
                if(usernameValue.isEmpty())
                    Toast.makeText(Register.this,"يجب إدخال اسم المستخدم قبل تسجيل الدخول", Toast.LENGTH_LONG).show();
                else if(passwordValue.isEmpty())
                    Toast.makeText(Register.this,"يجب إدخال كلمة المرور قبل تسجيل الدخول", Toast.LENGTH_LONG).show();
                else{
                    Intent intent = new Intent(Register.this, RegisterAdditionalInfo.class);
                    intent.putExtra("Uname", usernameValue);
                    intent.putExtra("Upass", passwordValue);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }
}
