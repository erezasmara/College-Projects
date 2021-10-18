package net.simplifiedlearning.firebaseauth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2nav);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

//Check if user is a guest
        if (user != null) {
            SetDisplayName();
            setUrlPic();
        }else{
            Toast.makeText(this,"You Are Login As Guest!",Toast.LENGTH_SHORT).show();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                finishAffinity();
                startActivity(new Intent(this, MainActivity.class));

                break;
        }

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final FirebaseUser user = mAuth.getCurrentUser();

        if (id == R.id.nav_profileActivity) {
            if(user!=null){
                Intent intent=new Intent(this,ProfileActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(Navigation.this, "You have to register before.", Toast.LENGTH_SHORT).show();

            }


        } else if (id == R.id.nav_search) {
            Intent intent=new Intent(this,MainActivityMap.class);
            startActivity(intent);

        } else if (id == R.id.PublishProducts) {
            if(user!=null){
                Intent intent = new Intent(this, UploadProductActivity.class);
                startActivity(intent);
            }else{
             Toast.makeText(Navigation.this, "You have to register before.", Toast.LENGTH_SHORT).show();

            }

        }else if (id == R.id.ViewProducts) {
            if(user!=null){
                Intent intent = new Intent(this, ViewProductActivity.class);
                startActivity(intent);

            }else{
                Toast.makeText(Navigation.this, "You have to register before.", Toast.LENGTH_SHORT).show();

            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//Display name on Header Navigation different way.

    public void SetDisplayName(){
       NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
       View headerView = navigationView.getHeaderView(0);

       TextView textView = (TextView) headerView.findViewById(R.id.textView);

       FirebaseUser user=mAuth.getCurrentUser();
       String displayName=user.getDisplayName();

       textView.setText(displayName);



    }

//Load picture profile to nav Header different way
    public void setUrlPic(){
        NavigationView navigationPic = (NavigationView) findViewById(R.id.nav_view);
        final View headerView = navigationPic.getHeaderView(0);


        final ImageView imageView=(ImageView) headerView.findViewById(R.id.imageView);

        final FirebaseUser user = mAuth.getCurrentUser();
        FirebaseStorage storage  = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("profilepics/").child(user.getUid()+".jpg");

        //picture profile bar
        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {

                if(task.isSuccessful())
                {

                    Glide.with(Navigation.this)
                            .load(task.getResult())
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                            .into(imageView);

                }
                else {
                    Toast.makeText(Navigation.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

}

