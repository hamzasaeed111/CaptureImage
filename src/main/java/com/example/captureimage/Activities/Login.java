package com.example.captureimage.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.captureimage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity
{
    ImageView background, image;
    TextInputEditText userEmail, userPassword;
    Button loginBtn;
    TextView signuplink;

    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail = findViewById(R.id.emaillogin);
        userPassword = findViewById(R.id.passwordlogin);
        loginBtn = findViewById(R.id.loginsubmit);
        signuplink = findViewById(R.id.signup);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        signuplink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userEmail.getText().toString();
                String password = userPassword.getText().toString();

                if (email.length() == 0)
                    userEmail.setError("Please enter an email id");
                else if (password.length() == 0)
                    userPassword.setError("Please enter a password");
                else {
                    progressDialog.setMessage("Please wait.....");
                    progressDialog.show();

                    try {
                        firebaseAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task)
                                    {
                                        progressDialog.dismiss();
                                        if (task.isSuccessful())
                                        {
                                            Intent i = new Intent(Login.this, Home.class);
                                            startActivity(i);
                                        }

                                        else if (!task.isSuccessful())
                                        {
                                            // Showing alert after successfully data submit.
                                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Email or password is invalid. Try again!", Snackbar.LENGTH_LONG);
                                            snackbar.show();
                                        }
                                    }
                                });
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Login.this, "Enter email and password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
