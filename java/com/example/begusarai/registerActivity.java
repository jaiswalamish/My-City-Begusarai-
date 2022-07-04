package com.example.begusarai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.j2objc.annotations.ObjectiveCName;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class registerActivity extends AppCompatActivity {
    private EditText fullname;
    private EditText email;
    private EditText pass;
    private EditText cnfrm_pass;
    private Button register;
    private EditText bio;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private ImageView cnfrm_pass_vis;
    private TextView login_again;

    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullname=findViewById(R.id.fullname_text);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.pass);
        bio=findViewById(R.id.bio);
        login_again=findViewById(R.id.login_again);
        cnfrm_pass=findViewById(R.id.cnfrm_pass);
        register=findViewById(R.id.signup_button);
        pd=new ProgressDialog(registerActivity.this);
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        cnfrm_pass_vis=findViewById(R.id.cnfrm_pass_vis);

        login_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(registerActivity.this,loginActivity.class));
                finish();
            }
        });
        cnfrm_pass_vis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cnfrm_pass_vis.getTag().equals("vis")){
                    cnfrm_pass_vis.setImageResource(R.drawable.ic_cnfrm_pass_invis);
                    cnfrm_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    cnfrm_pass_vis.setTag("invis");
                }else{
                   cnfrm_pass_vis.setImageResource(R.drawable.ic_cnfrm_pass);
                   cnfrm_pass.setTransformationMethod(null);
                    cnfrm_pass_vis.setTag("vis");
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=fullname.getText().toString();
                String user_email=email.getText().toString();

                String user_pass=pass.getText().toString();
                String cnfrm=cnfrm_pass.getText().toString();
                String about=bio.getText().toString();
                if(name.equals("") || user_email.equals("") || user_pass.equals("") || cnfrm.equals("")){
                    Toast.makeText(registerActivity.this, "Invalid Format", Toast.LENGTH_SHORT).show();
                }else if(user_pass.length()<6 || cnfrm.length()<6){
                    Toast.makeText(registerActivity.this, "Password is too short", Toast.LENGTH_SHORT).show();
                }else if(!user_pass.equals(cnfrm)){
                    Toast.makeText(registerActivity.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                }else {
                    if(isEmailValid(user_email)){
                        pd.setMessage("Please Wait...");
                        pd.show();
                        signUp(name,user_email,user_pass,about);

                    }else{
                        Toast.makeText(registerActivity.this, "Enter correct email", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }

    private boolean isEmailValid(String user_email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +  //part before @
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;

        return pat.matcher(user_email).matches();
    }

    private void signUp(String name, String user_email, String user_pass, String about) {
        auth.createUserWithEmailAndPassword(user_email, user_pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("name", name);
                                map.put("email", user_email);
                                map.put("password", user_pass);
                                map.put("id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                map.put("bio", about);
                                map.put("imageUrl", "default");

                                db.collection("users").document(auth.getCurrentUser().getUid()).set(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    if(auth.getCurrentUser().isEmailVerified()){
                                                        pd.dismiss();
                                                        startActivity(new Intent(registerActivity.this, MainActivity.class));
                                                        finish();
                                                    }else{
                                                        pd.dismiss();
                                                        startActivity(new Intent(registerActivity.this,verificationActivity.class));
                                                    }

                                                } else {
                                                    Toast.makeText(registerActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                                    pd.dismiss();
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(registerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(registerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(registerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }




}