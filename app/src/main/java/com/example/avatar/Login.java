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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText Username, Password;
    Button Login, NewUser;

    private FirebaseAuth mAuth;
    ProgressDialog PD;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            Username.setText(currentUser.getEmail());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        PD = new ProgressDialog(this);

        Username = findViewById(R.id.login_et_username);
        Password = findViewById(R.id.login_et_password);
        Login = findViewById(R.id.login_btn_login);
        NewUser = findViewById(R.id.login_btn_new_user);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameValue = Username.getText().toString();
                String passwordValue = Password.getText().toString();
                if(usernameValue.isEmpty())
                    Toast.makeText(Login.this,"يجب إدخال اسم المستخدم قبل الدخول", Toast.LENGTH_LONG).show();
                else if(passwordValue.isEmpty())
                    Toast.makeText(Login.this,"يجب إدخال كلمة المرور قبل الدخول", Toast.LENGTH_LONG).show();
                else{
                    PD.setMessage("جاري تسجيل الدخول...");
                    PD.show();
                    mAuth.signInWithEmailAndPassword(usernameValue, passwordValue)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    PD.dismiss();
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("TAG", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        GoToStart(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(Login.this, "اسم المستخدم أو كلمة المرور خطأ",
                                                Toast.LENGTH_LONG).show();
                                        Username.setText("");
                                        Password.setText("");
                                    }
                                }
                            });
                }
            }
        });

        NewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }

    void GoToStart(FirebaseUser user){
        Intent intent;
        if(user.getUid().equals("gUXc2c3xnSXCqOajaDU7Hks8Dw23") ||
           user.getUid().equals("fGvKEnp0AqRguXjYRPV6alw4AxO2") ||
           user.getUid().equals("AmeC5hWcWjcwMYs7rCSK1ux1E9i2"))
            intent = new Intent(Login.this, EmployerStart.class);
        else
            intent = new Intent(Login.this, JobSeekerStart.class);
        startActivity(intent);
    }
}
