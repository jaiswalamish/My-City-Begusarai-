package com.example.begusarai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class verificationActivity extends AppCompatActivity {
    private AppCompatButton resend_email;
    private AppCompatButton edit_email;
    private ImageView more;
    private AppCompatImageView face;
    private LinearLayout before;
    private LinearLayout after;
    private TextView email_;
    private EditText new_email;
    private AppCompatButton update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        edit_email=findViewById(R.id.edit_email);
        resend_email=findViewById(R.id.resend_email);
        before=findViewById(R.id.previous);
        after=findViewById(R.id.after);
        email_=findViewById(R.id.email_);
        new_email=findViewById(R.id.email_after);
        update=findViewById(R.id.update_email);

        email_.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        face=findViewById(R.id.sad_face);
        Picasso.get().load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTyJiVAMaPeiCg49hOuqk6ROByzM8_V2-qaag&usqp=CAU").into(face);
        more=findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu=new PopupMenu(verificationActivity.this,view);
                menu.inflate(R.menu.refresh);
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        FirebaseAuth.getInstance().getCurrentUser().reload();
                        if(menuItem.getItemId()==R.id.refresh){
                            if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                                startActivity(new Intent(verificationActivity.this,MainActivity.class));
                                finish();
                            }else{
                                Toast.makeText(verificationActivity.this, "email yet not verified", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if(menuItem.getItemId()==R.id.email){
                            String user_email=FirebaseAuth.getInstance().getCurrentUser().getEmail();
                            Toast.makeText(verificationActivity.this,user_email , Toast.LENGTH_SHORT).show();
                        }
                        if(menuItem.getItemId()==R.id.sign_out){

                            new AlertDialog.Builder(verificationActivity.this)
                                    .setIcon(R.drawable.ic_alert)
                                    .setTitle("Logout")
                                    .setMessage("Are You Sure!!")
                                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            FirebaseAuth.getInstance().signOut();
                                            startActivity(new Intent(verificationActivity.this, loginActivity.class));
                                            finish();
                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                        }

                        return true;
                    }
                });
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e=new_email.getText().toString();
                FirebaseAuth.getInstance().getCurrentUser().updateEmail(e)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                before.setVisibility(View.VISIBLE);
                                after.setVisibility(View.GONE);
                                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Map<String,Object> map=new HashMap<>();
                                                map.put("email",e);
                                                FirebaseFirestore.getInstance().collection("users")
                                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(map);
                                                Toast.makeText(verificationActivity.this, "email verification link sent", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull  Exception e) {
                                        Toast.makeText(verificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(verificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        edit_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               before.setVisibility(View.GONE);
               after.setVisibility(View.VISIBLE);
            }
        });
        resend_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(verificationActivity.this, "email verification sent", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(verificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

}