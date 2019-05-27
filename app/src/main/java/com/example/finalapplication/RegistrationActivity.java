package com.example.finalapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText UserName, UserPassword, UserEmail;
    private Button regButton;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();

        regButton = findViewById(R.id.btnRegister);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    //upload to database
                    String user_email = UserEmail.getText().toString().trim();
                    String user_password = UserPassword.getText().toString().trim();
                    progressDialog.setMessage("Registration Progress.");
                    progressDialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if ((task.isSuccessful()))
                            {
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Registration Sucessfull", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                            }

                            {
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });

                }
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });
    }

    private void setupUIViews() {
        UserName = (EditText) findViewById(R.id.etUserName);
        UserPassword = (EditText) findViewById(R.id.etPassword);
        UserEmail = (EditText) findViewById(R.id.etEmail);
        userLogin = (TextView) findViewById(R.id.tvLogin);
        progressDialog = new ProgressDialog(this);

    }

    private Boolean validate() {
        Boolean result = false;

        String name = UserName.getText().toString();
        String password = UserPassword.getText().toString();
        String email = UserEmail.getText().toString();

        if (name.isEmpty() && password.isEmpty() && email.isEmpty()) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }
        return result;
    }


}
