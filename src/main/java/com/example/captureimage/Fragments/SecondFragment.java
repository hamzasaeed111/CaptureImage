package com.example.captureimage.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.captureimage.Classes.UserDetails;
import com.example.captureimage.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SecondFragment extends Fragment
{
    Button SubmitButton;
    EditText NameEditText, EmailEditText, MessageEditText;

    // Declaring String variable ( In which we are storing firebase server URL ).
    public static final String Firebase_Server_URL = "https://feedback-12157.firebaseio.com/";

    // Declaring String variables to store name & phone number get from EditText.
    String NameHolder, EmailHolder, MessageHolder;

    DatabaseReference databaseReference;

    // Root Database Name for Firebase Database.
    String Database_Path = "User_Bug-Report_Feedback_Database";

    //EditText user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment, container, false);
        //user = view.findViewById(R.id.name);
        //user.setHint("Your name");

        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        SubmitButton = view.findViewById(R.id.submit);
        NameEditText = view.findViewById(R.id.name);
        EmailEditText = view.findViewById(R.id.email);
        MessageEditText = view.findViewById(R.id.message);

        SubmitButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                NameHolder = NameEditText.getText().toString();
                EmailHolder = EmailEditText.getText().toString().trim();
                MessageHolder = MessageEditText.getText().toString();

                if (NameHolder.length() == 0)
                    NameEditText.setError("Please enter a user name");
                else if(EmailHolder.length() == 0)
                    EmailEditText.setError("Please enter an email id");
                else if(MessageHolder.length() == 0)
                    MessageEditText.setError("Please enter a message");
                else
                    upload();
            }

            private void upload()
            {
                UserDetails details = new UserDetails();
                // Adding name into class function object.
                details.setName(NameHolder);

                // Adding email into class function object.
                details.setEmail(EmailHolder);

                // Adding message into class function object.
                details.setMessage(MessageHolder);

                // Getting the ID from firebase database.
                String UserRecordIDFromServer = databaseReference.push().getKey();

                // Adding the both name and number values using student details class object using ID.
                databaseReference.child(UserRecordIDFromServer).setValue(details);

                ViewGroup viewGroup = (ViewGroup) getActivity().findViewById(android.R.id.content);
                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.success_dialog, viewGroup, false);


                //Now we need an AlertDialog.Builder object
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                //setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView);

                //finally creating the alert dialog and displaying it
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                NameEditText.getText().clear();
                EmailEditText.getText().clear();
                MessageEditText.getText().clear();
            }
        });
        return view;
    }

}
