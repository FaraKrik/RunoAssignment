package com.example.NewProject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.runoassignment.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Button loginBtnId;
    EditText phoneId,passwordId;
    TextView registerId;
    ProgressBar progressBar;

    String email,password,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        phoneId=(EditText)findViewById(R.id.phoneId);
        passwordId=(EditText)findViewById(R.id.passwordId);
        loginBtnId=(Button) findViewById(R.id.registerbtnId);
        registerId=(TextView) findViewById(R.id.registerId);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);

        loginBtnId.setOnClickListener(v -> {
            phone=phoneId.getText().toString().trim();
            password=passwordId.getText().toString().trim();
            email=phone+"@test.com";



            if(TextUtils.isEmpty(phone) || phone.length()!=10)
            {
//                Toast.makeText(MainActivity.this, "Please Enter Phone No.", Toast.LENGTH_SHORT).show();
                phoneId.setError("Enter phone(10 digits)");
                phoneId.requestFocus();
                return;
            }
            else if(password.isEmpty() || password.length() <=6){
                passwordId.setError("Enter password(>6)");
                passwordId.requestFocus();
                return;
            }
            else {
//                    Toast.makeText(MainActivity.this, phone, Toast.LENGTH_SHORT).show();
                login();

            }
        });
        registerId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Register.class);
                startActivity(intent);

            }
        });


    }
    public void login()
    {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(MainActivity.this,ViewNotes.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
            Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,ViewNotes.class));
        }
    }

}