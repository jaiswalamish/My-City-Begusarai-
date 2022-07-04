package com.example.begusarai.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.begusarai.BuildConfig;
import com.example.begusarai.MainActivity;
import com.example.begusarai.R;
import com.example.begusarai.infoActivity;
import com.example.begusarai.reportActivity;
import com.example.begusarai.settingActivity;
import com.example.begusarai.your_upload_activity;
import com.example.begusarai.editActivity;
import com.example.begusarai.loginActivity;
import com.example.begusarai.model.user;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class profileFragment extends Fragment {

    private TextView logoutText;
    private ImageView logout;
    private CircleImageView image;
    private ImageView close;
    private TextView name;
    private TextView edit;
    private TextView your_upload_text;
    private ImageView your_upload_image;
    private TextView version;
    private TextView reportText;
    private ImageView report;
    private TextView settingText;
    private  ImageView setting;
    private TextView infoText;
    private ImageView info;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        logoutText=view.findViewById(R.id.profile_logout_text);
        logout= view.findViewById(R.id.ic_logOut);
        image= view.findViewById(R.id.profile_pic);
        close= view.findViewById(R.id.close);
        name=view.findViewById(R.id.profile_name);
        edit=view.findViewById(R.id.profile_edit);
        your_upload_image=view.findViewById(R.id.profile_your_upload_pic);
        your_upload_text=view.findViewById(R.id.profile_your_upload_text);
        reportText=view.findViewById(R.id.profile_report_text);
        report=view.findViewById(R.id.ic_report);
        settingText=view.findViewById(R.id.profile_setting_text);
        setting=view.findViewById(R.id.ic_setting);
        infoText=view.findViewById(R.id.profile_info_text);
        info =view.findViewById(R.id.ic_info);



        version=view.findViewById(R.id.version);
        String versionName = BuildConfig.VERSION_NAME;
        version.setText("version :  "+versionName);
        addData();

        infoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), infoActivity.class));
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), infoActivity.class));
            }
        });

        settingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), settingActivity.class));
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), settingActivity.class));
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), reportActivity.class));
            }
        });
        reportText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), reportActivity.class));
            }
        });

        your_upload_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu=new PopupMenu(getContext(),view);
                menu.inflate(R.menu.addchose);
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Intent intent=new Intent(getContext(),your_upload_activity.class);
                        intent.putExtra("selected",menuItem.getTitle());
                        startActivity(intent);
                        return  true;
                    }
                });
            }
        });
        your_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu=new PopupMenu(getContext(),view);
                menu.inflate(R.menu.addchose);
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Intent intent=new Intent(getContext(),your_upload_activity.class);
                        intent.putExtra("selected",menuItem.getTitle());
                        startActivity(intent);
                        return  true;
                    }
                });
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut(view);
            }
        });
        logoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut(view);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), editActivity.class));
            }
        });

        return view;
    }

    private void addData() {

        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                      user user_detail=documentSnapshot.toObject(user.class);
                        name.setText(user_detail.getName());
                        if(user_detail.getImageUrl().equals("default")){
                            image.setImageResource(R.mipmap.ic_launcher_round);
                        }else{
                            Picasso.get().load(user_detail.getImageUrl()).into(image);
                        }
                    }
                });

    }

    private void signOut(View view) {
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_alert)
                .setTitle("Logout")
                .setMessage("Are You Sure!!")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getContext(), loginActivity.class));
                        getActivity().finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

}