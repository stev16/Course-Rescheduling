package com.example.coursereschedule;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginPage extends AppCompatActivity {

    Button signIn;
    TextView register;
    EditText username, userpassword;
    ProgressBar loadingBar;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        loadingBar=  findViewById(R.id.loadingProgress);

        register= findViewById(R.id.signUp_txt);
        SpannableString content = new SpannableString("Click here to SIGN UP");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        register.setText(content);


        signIn= findViewById(R.id.signIn_btn);
        username=findViewById(R.id.userEmail_login);
        userpassword=findViewById(R.id.Password_text);
        auth= FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            Intent intent = new Intent(loginPage.this, homePage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{
            Log.d(TAG, "onAuthStateChanged:sign_out");
        }

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUser();
            }

        });


    }

    private void loginUser() {
        loadingBar.setVisibility(View.VISIBLE);
        String userName= username.getText().toString();
        String userPassword= userpassword.getText().toString();

        if(TextUtils.isEmpty(userName)){
            loadingBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Username is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(userPassword)){
            loadingBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(userPassword.length()<8){
            loadingBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Your Password need at least 8 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        //login process
        auth.signInWithEmailAndPassword(userName, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(loginPage.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.setVisibility(View.INVISIBLE);
                            Intent intent= new Intent(loginPage.this, homePage.class);
                            startActivity(intent);
                        }else {
                            loadingBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(loginPage.this, "Check Your Login Access", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signUpPage(View view) {
        Intent signUp= new Intent(loginPage.this, registerPage.class);
        startActivity(signUp);
    }
}