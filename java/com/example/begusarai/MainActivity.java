package com.example.begusarai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.begusarai.fragments.homeFragment;
import com.example.begusarai.fragments.profileFragment;
import com.example.begusarai.fragments.searchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigationView;
    private Fragment fragment;

    boolean res=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView=findViewById(R.id.navigation_view);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        fragment=new homeFragment();
                        res=false;
                        break;

                    case R.id.nav_search:
                        fragment=new searchFragment();
                        res=false;
                        break;

                    case R.id.nav_profile:
                        fragment=new profileFragment();
                        res=false;
                        break;

                    case R.id.nav_add:
                        fragment=null;
                        res=false;
                        startActivity(new Intent(MainActivity.this,addActivity.class));
                        break;
                }
                if(fragment!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment)
                            .addToBackStack(null)
                            .commit();

                }
                return true;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new homeFragment()).commit();

    }


    @Override
    public void onBackPressed() {
        if(res){
            finish();
        }
        res=true;
        navigationView.getMenu().getItem(0).setChecked(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new homeFragment()).commit();
    }


}