package com.example.captureimage.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.captureimage.Classes.Users;
import com.example.captureimage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextInputEditText username, password, email, phone;
    ProgressDialog progressDialog;
    Button submit;
//    ImageView background, image;
    TextView loginlink;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    TextView signInlink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

//        image = findViewById(R.id.signuplogo);
        username = findViewById(R.id.usernamesignup);
        password = findViewById(R.id.passwordsignup);
        email = findViewById(R.id.emailsignup);
        phone = findViewById(R.id.phonesignup);
        signInlink = findViewById(R.id.signin);
        submit = findViewById(R.id.submitsignup);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailId = email.getText().toString();
                String passwordId = password.getText().toString();
                final String name = username.getText().toString();
                final String phonenum = phone.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    username.setError("Please enter a name");
                } else if (TextUtils.isEmpty(emailId)) {
                    email.setError("Please enter an email id");
                    return;
                } else if (TextUtils.isEmpty(passwordId)) {
                    password.setError("Please enter a password");
                } else if (password.length() < 6) {
                    password.setError("Password too short, enter minimum 6 characters");
                    return;
                }
                else
                {
                    progressDialog.setMessage("Please wait.....");
                    progressDialog.show();

                    //create user
                    mAuth.createUserWithEmailAndPassword(emailId, passwordId)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (!task.isSuccessful()) {
                                progressDialog.dismiss();
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "This email already exists!", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            } else {
                                Users user = new Users(name, emailId, phonenum);

                                FirebaseDatabase.getInstance().getReference("Parent")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            ViewGroup viewGroup = findViewById(android.R.id.content);

                                            //then we will inflate the custom alert dialog xml that we created
                                            View dialogView = LayoutInflater.from(Signup.this).inflate(R.layout.alert_success_register, viewGroup, false);

                                            //Now we need an AlertDialog.Builder object
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);

                                            //setting the view of the builder to our custom view that we already inflated
                                            builder.setView(dialogView);

                                            //finally creating the alert dialog and displaying it
                                            AlertDialog alertDialog = builder.create();
                                            alertDialog.show();

                                            progressDialog.dismiss();

                                            Intent intent = new Intent(Signup.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(Signup.this, "User Not Created", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        signInlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
            }
        });
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser)
    {
    }
}



