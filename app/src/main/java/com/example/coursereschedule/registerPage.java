package com.example.coursereschedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coursereschedule.Model.userModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class registerPage extends AppCompatActivity {


    Button Cancel, signUp;
    EditText username, userID, userEmail, userPW;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        Cancel=findViewById(R.id.Cancel_btn);
        signUp=findViewById(R.id.signUp_btn);
        username=findViewById(R.id.username_register);
        userID=findViewById(R.id.userid_register);
        userEmail=findViewById(R.id.email_register);
        userPW=findViewById(R.id.password_register);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });


    }

    private void createUser() {
        String userName= username.getText().toString();
        String userIDregister= userID.getText().toString();
        String useremail= userEmail.getText().toString();
        String userPassword= userPW.getText().toString();

        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this, "Username is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(userIDregister)){
            Toast.makeText(this, "Your userID is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(useremail)){
            Toast.makeText(this, "Useremail is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(userPassword.length()<8){
            Toast.makeText(this, "Your Password is not enough strong", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(useremail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    userModel userModel= new userModel(userName, userIDregister, useremail, userPassword);
                    String id = task.getResult().getUser().getUid();
                    database.getReference().child("User").child(id).setValue(userModel);
                    Toast.makeText(registerPage.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                    Intent login= new Intent(registerPage.this, loginPage.class);
                    startActivity(login);

                }else {
                    Toast.makeText(registerPage.this, "Your Email is Registered", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}