package com.example.begusarai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.begusarai.fragments.profileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.CredentialsProvider;

public class settingActivity extends AppCompatActivity {
    private EditText old_pass;
    private EditText new_pass;
    private EditText cnfrm_pass;
    private TextView save;
    private TextView cancel;
    private FirebaseUser firebaseUser;
    private TextView reset_pass;
    private ImageView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        old_pass=findViewById(R.id.old_pass_text);
        new_pass=findViewById(R.id.new_pass_text);
        cnfrm_pass=findViewById(R.id.cnfrm_pass_text);
        save=findViewById(R.id.save);
        cancel=findViewById(R.id.cancel);
        close=findViewById(R.id.close);


        reset_pass=findViewById(R.id.reset_pass);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(settingActivity.this,profileFragment.class));
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(settingActivity.this,profileFragment.class));
                finish();
            }
        });

        reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e=FirebaseAuth.getInstance().getCurrentUser().getEmail();
                FirebaseAuth.getInstance().sendPasswordResetEmail(e)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(settingActivity.this, "email sent", Toast.LENGTH_SHORT).show();
                                startService(new Intent(settingActivity.this,profileFragment.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        Toast.makeText(settingActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String old=old_pass.getText().toString();
                String pass=new_pass.getText().toString();
                String cnfrm=cnfrm_pass.getText().toString();
                if(pass.length()<6 || cnfrm.length()<6){
                    Toast.makeText(settingActivity.this, "minimum length of password should be 6", Toast.LENGTH_SHORT).show();
                }
                else if(!pass.equals(cnfrm)){
                    Toast.makeText(settingActivity.this, "confirm password is different", Toast.LENGTH_SHORT).show();
                }else{
                    AuthCredential credential= EmailAuthProvider
                            .getCredential(firebaseUser.getEmail(),old_pass.getText().toString());
                    firebaseUser.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        firebaseUser.updatePassword(new_pass.getText().toString());
                                        Toast.makeText(settingActivity.this, "successfully Updated", Toast.LENGTH_SHORT).show();
                                        startService(new Intent(settingActivity.this, profileFragment.class));
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull  Exception e) {
                            Toast.makeText(settingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }
}