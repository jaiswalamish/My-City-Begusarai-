package com.example.begusarai;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.begusarai.fragments.profileFragment;
import com.example.begusarai.model.user;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class  editActivity extends AppCompatActivity {
    private ImageView close;
    private ImageView check;
    private CircleImageView pic;
    private TextView pic_change;
    private EditText name;
    private EditText bio;
    private TextView email;
    private Uri imageUri;
    private String url;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        check = findViewById(R.id.save_change);
        pic = findViewById(R.id.image_change);
        pic_change = findViewById(R.id.change_text);
        name = findViewById(R.id.name_change);
        bio = findViewById(R.id.bio_change);
        email = findViewById(R.id.email);
        close=findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), profileFragment.class);
                startService(intent);
                finish();
            }
        });
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        addData();
        pic_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(editActivity.this)
                        .crop()
                        .maxResultSize(1080,1920)
                        .compress(1000)
                        .start(10);
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

    }

    private void updateData() {
        ProgressDialog pd=new ProgressDialog(editActivity.this);
        pd.setMessage("changes saving...");
        pd.show();
        if(imageUri!=null){
            try {
                StorageReference reference = FirebaseStorage.getInstance().getReference("profilepicuploads")
                        .child(firebaseUser.getUid()).child(System.currentTimeMillis() + ".jpeg");
                StorageTask task = reference.putFile(imageUri);
                task.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return reference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task != null) {
                            Uri download = task.getResult();
                            Map<String, Object> m = new HashMap<>();
                            if (download != null) {
                                url = download.toString();
                            }

                            m.put("imageUrl", url);

                            FirebaseFirestore.getInstance().collection("users")
                                    .document(firebaseUser.getUid()).update(m);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(editActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

        }
            Map<String,Object> map=new HashMap<>();
            map.put("name", name.getText().toString());
            map.put("bio", bio.getText().toString());
             map.put("email",email.getText().toString());

            FirebaseFirestore.getInstance().collection("users")
                    .document(firebaseUser.getUid()).update(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            firebaseUser.updateEmail(email.getText().toString());
                            pd.dismiss();
                            Intent intent=new Intent(getApplicationContext(), profileFragment.class);
                            startService(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Intent intent=new Intent(getApplicationContext(), profileFragment.class);
                    startService(intent);
                    finish();
                    Toast.makeText(editActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10 && resultCode==RESULT_OK){
            imageUri=data.getData();
            pic.setImageURI(imageUri);
        }
        else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void addData () {
            FirebaseFirestore.getInstance().collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    user user_detail = documentSnapshot.toObject(user.class);
                    name.setText(user_detail.getName());
                    bio.setText(user_detail.getBio());
                    email.setText(user_detail.getEmail());
                    if (user_detail.getImageUrl().equals("default")) {
                        pic.setImageResource(R.mipmap.ic_launcher_round);
                    } else {
                        Picasso.get().load(user_detail.getImageUrl()).into(pic);
                    }
                    url=user_detail.getImageUrl();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(editActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

