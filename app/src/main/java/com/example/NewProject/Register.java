package com.example.NewProject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.runoassignment.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;

import static android.content.ContentValues.TAG;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;

    String name,phone,email,password;
    Button registerBtnId;
    EditText nameId,phoneId,passwordId;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        nameId=(EditText)findViewById(R.id.nameId);
        phoneId=(EditText)findViewById(R.id.phoneId);
        passwordId=(EditText)findViewById(R.id.passwordId);
        registerBtnId=(Button) findViewById(R.id.registerbtnId);
        progressBar=(ProgressBar)findViewById(R.id.progressBar) ;

        registerBtnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=nameId.getText().toString().trim();
                phone=phoneId.getText().toString().trim();
                email=phone+"@test.com";
                password=passwordId.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    nameId.setError("Enter name");
                    nameId.requestFocus();
                    return ;
//                    Toast.makeText(Register.this, "Enter Name", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(phone) || phone.length()!=10){
                    phoneId.setError("Enter Phone No.(10 digit)");
                    phoneId.requestFocus();
                    return;
//                    Toast.makeText(Register.this, "Please Enter Phone(10 digit)", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(password) || password.length()<=6 ){
                    passwordId.setError("Enter Password(> 6 digit)");
                    passwordId.requestFocus();
                    return;
//                    Toast.makeText(Register.this, "Enter Password", Toast.LENGTH_SHORT).show();
                }
                else{
//                    Toast.makeText(Register.this, name+" "+phone+" "+email, Toast.LENGTH_SHORT).show();
                    register();
                }
            }
        });


    }
    public static String base64encode(String input) {
        try {
            return new String(Base64.encode(input.getBytes("UTF-8"), Base64.NO_WRAP), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Got unsupported encoding: " + e);
            return "encode-error";
        }
    }
    public void register(){

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                User user;
                String notes="I am topic \nI am description.Click me to edit me or Click Add Note on top right to add new note. Click menu to logout.\n";
                String encodedNotes=base64encode(notes);
                user = new User(name,phone,email,encodedNotes);

                try{
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(Register.this,MainActivity.class));

                        } else {
                            Toast.makeText(Register.this, "Failed!!!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    });
                }
                catch (Throwable e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(Register.this, "Failed!!!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}