package com.example.begusarai;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.begusarai.fragments.homeFragment;
import com.example.begusarai.fragments.profileFragment;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.dhaval2404.imagepicker.ImagePickerActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class addActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{


    private EditText name;
    private EditText address;
    private EditText number;
    private EditText email;
    private EditText description;
    private EditText link;
    private LinearLayout detail;
    private LinearLayout image;
    private TextView add_type;
    private Button add_image;
    private Button upload;
    private Button select_again;
    private TextView final_upload;
    private ImageView pic_upload;
    private Uri imageUrl;
    private BottomNavigationView navigationView;


    private ImageView close;

    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        detail=findViewById(R.id.detail_layout);
        image=findViewById(R.id.pic_layout);
        add_image=findViewById(R.id.add_image);
        add_type=findViewById(R.id.add_type);
        upload=findViewById(R.id.pic_upload);
        pic_upload=findViewById(R.id.add_pic_upload);
        select_again=findViewById(R.id.select_again);
        final_upload=findViewById(R.id.final_upload);
        navigationView=findViewById(R.id.navigation_view);

        link=findViewById(R.id.add_link);
        description=findViewById(R.id.add_description);
        email=findViewById(R.id.add_email);
        number=findViewById(R.id.add_number);
        address=findViewById(R.id.add_address);
        name=findViewById(R.id.add_name);
        close=findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(addActivity.this,homeFragment.class));
                finish();
            }
        });

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUrl!=null){
                    String final_name=name.getText().toString();
                    String final_address=address.getText().toString();
                    String final_number=number.getText().toString();
                    String final_link=link.getText().toString();
                    String final_email=email.getText().toString();
                    String final_description=description.getText().toString();
                    String final_type=add_type.getText().toString().toLowerCase();
                    String final_imageurl=imageUrl.toString();
                    if(final_name.equals("") || final_address.equals("") || final_number.equals("") || final_email.equals("")
                            || final_description.equals("") || final_type.equals("")){

                        Toast.makeText(addActivity.this, "You have left something\n to fill", Toast.LENGTH_LONG).show();
                    }else{
                        if(isEmailValid(final_email)){
                            uploadData(final_name,final_address ,final_number,final_link,final_email,
                                    final_description,final_type,final_imageurl);
                        }else{
                            Toast.makeText(addActivity.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                        }

                    }
                }else{
                    Toast.makeText(addActivity.this, "please select a image", Toast.LENGTH_SHORT).show();
                }

            }
        });



        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detail.setVisibility(View.GONE);
                final_upload.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
                ImagePicker.with(addActivity.this)
                        .crop().start(100);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image.setVisibility(View.GONE);
                final_upload.setVisibility(View.VISIBLE);
                detail.setVisibility(View.VISIBLE);
                add_image.setText(imageUrl.toString());
                add_image.setTextSize(10);
            }
        });
        add_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup_menu=new PopupMenu(addActivity.this,view);
                popup_menu.setOnMenuItemClickListener(addActivity.this::onMenuItemClick);
                popup_menu.inflate(R.menu.addchose);
                popup_menu.show();
            }
        });
        select_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(addActivity.this)
                        .maxResultSize(1080,1920)
                        .compress(1000)
                        .crop().start(100);
            }
        });
    }

    private boolean isEmailValid(String final_email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +  //part before @
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;

        return pat.matcher(final_email).matches();
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        add_type.setText(menuItem.getTitle());
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==100 && resultCode==RESULT_OK){
            imageUrl=data.getData();
            pic_upload.setImageURI(imageUrl);


        }  else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }

    }
    private void uploadData(String final_name, String final_address, String final_number,
                            String final_link, String final_email, String final_description,
                            String final_type, String final_imageurl) {

        ProgressDialog pd=new ProgressDialog(addActivity.this);
        pd.setMessage("uploading...");
        pd.show();

        StorageReference reference = FirebaseStorage.getInstance().getReference("dataUpload")
                        .child(firebaseUser.getUid()).child(System.currentTimeMillis() + ".jpeg");
                StorageTask task = reference.putFile(imageUrl);
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

                            m.put("imageUrl",download.toString());
                            m.put("name",final_name);
                            m.put("address",final_address);
                            m.put("link",final_link);
                            m.put("phone",final_number);
                            m.put("email",final_email);
                            m.put("userId",firebaseUser.getUid());
                            m.put("description",final_description);
//                            FirebaseDatabase.getInstance().getReference("posts").child(final_type+"s")
//                                    .setValue(m);
                            FirebaseFirestore.getInstance().collection(final_type+"s")
                                    .document().set(m)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            pd.dismiss();
                                            Toast.makeText(addActivity.this, "uploaded", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(addActivity.this, MainActivity.class));
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull  Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(addActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(addActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

}
