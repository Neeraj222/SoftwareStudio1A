package com.example.finalapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordActivity extends AppCompatActivity {

    private EditText Email;
    private FirebaseAuth firebaseAuth;
    private Button resetPassword;
    private Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        Email = findViewById(R.id.etEmail);
        button1 = findViewById(R.id.btnLoginPage);
        resetPassword = findViewById(R.id.btnPasswordReset);
        firebaseAuth = FirebaseAuth.getInstance();

        button1 = findViewById(R.id.btnLoginPage);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenLoginPage();
            }
        });





        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = Email.getText().toString().trim();

                if (useremail.equals("")) {
                    Toast.makeText(PasswordActivity.this, "Please enter Email", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(PasswordActivity.this, "Password Reset Email Sent", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(PasswordActivity.this, LoginActivity.class));
                            } else {
                                Toast.makeText(PasswordActivity.this, "Error is Sending Email", Toast.LENGTH_SHORT);
                            }

                        }
                    });
                }

            }
        });

    }

    public void  OpenLoginPage(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}
