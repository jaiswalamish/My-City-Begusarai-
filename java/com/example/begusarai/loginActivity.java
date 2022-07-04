package com.example.begusarai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class loginActivity extends AppCompatActivity {

    private Button signin;
    private TextView register;
    private EditText email;
    private EditText pass;
    private FirebaseAuth auth;

    private ImageView icon;
    private TextView forgot_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signin = findViewById(R.id.signin_button);
        register = findViewById(R.id.register_text);
        email = findViewById(R.id.email_text);
        pass = findViewById(R.id.password);
        icon=findViewById(R.id.app_icon);
        forgot_pass=findViewById(R.id.forgot_pass);
        auth = FirebaseAuth.getInstance();

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().equals("")){
                    Toast.makeText(loginActivity.this, "please enter your email address first", Toast.LENGTH_SHORT).show();
                }else{
                    String e=email.getText().toString();
                    auth.sendPasswordResetEmail(e).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(loginActivity.this, "email sent", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(loginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
        Picasso.get().load("https://thebegusarai.in/wp-content/uploads/2020/10/Begusarai-Foundaton-Day.jpg").into(icon);

        if (auth.getCurrentUser() != null) {
            if(auth.getCurrentUser().isEmailVerified()){
                startActivity(new Intent(loginActivity.this, MainActivity.class));
                finish();
            }else{
                startActivity(new Intent(loginActivity.this,verificationActivity.class));
                finish();
            }

        } else {
            signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String user_email = email.getText().toString();
                    String user_pass = pass.getText().toString();
                    FirebaseDatabase.getInstance().getReference().setValue("amish");
                    login(user_email, user_pass);
                }
            });
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(loginActivity.this, registerActivity.class));
                    finish();
                }
            });
        }
    }

    private void login(String user_email, String user_pass) {
        if (user_email.equals("")) {
            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
        } else if (user_pass.equals("")) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        } else {
            auth.signInWithEmailAndPassword(user_email, user_pass).addOnCompleteListener(loginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        if(auth.getCurrentUser().isEmailVerified()){
                            Toast.makeText(loginActivity.this, "Loged In", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(loginActivity.this, MainActivity.class));
                            finish();
                        }else{
                            startActivity(new Intent(loginActivity.this,verificationActivity.class));
                            finish();
                        }

                    }
                }
            }).addOnFailureListener(loginActivity.this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(loginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
