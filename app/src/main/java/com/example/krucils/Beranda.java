package com.example.krucils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


//todo kalo teken back langsung ke close appsnya, coba bikin konfirmasi kalo mau close apps
public class Beranda extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    private boolean role=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beranda);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = FirebaseFirestore.getInstance();
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mAuth = FirebaseAuth.getInstance();

        if (userLoggedin()) {
            String currentuser = mAuth.getCurrentUser().getUid();
            getRole(currentuser);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new BerandaFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_beranda);

        }


        //todo bikin kelas getter userLoggedIn sama buat profile dari firebase ?

        //cari cara buat invalidateoptionmenu kalo logout dari action bar
        //ini udah

        //cari cara buat automatis manggil sesuatu abis layout/data change pada fragment
        //ini sepertinya udah



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //TODO bingung pake userloggedin atau cara lain
        //TODO tiap ngeklik awal tombol action barnya loadingnya lama, kemungkinan kepanggil berkali2 ?

        getMenuInflater().inflate(R.menu.main, menu);
        if (!userLoggedin()) {
            menu.findItem(R.id.action_login).setVisible(true);
            menu.findItem(R.id.action_daftar).setVisible(true);
            menu.findItem(R.id.action_logout).setVisible(false);


        }else{
            menu.findItem(R.id.action_login).setVisible(false);
            menu.findItem(R.id.action_daftar).setVisible(false);
            menu.findItem(R.id.action_logout).setVisible(true);



        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //todo bikin kelas baru buat reload fragment (atau masukkin updateUI), login dari profil fragment ke reload profil fragmentnya tapi dari action bar nggak
        switch (item.getItemId()) {
            case R.id.action_login:

                Intent loginIntent = new Intent(Beranda.this, LoginActivity.class);
                Beranda.this.startActivity(loginIntent);
                updateUI(this,mAuth);

                return true;
            case R.id.action_daftar:
                Intent registerIntent = new Intent(Beranda.this, RegisterActivity.class);
                Beranda.this.startActivity(registerIntent);
                updateUI(this,mAuth);

                return true;


            case R.id.action_logout:
               /* mAuth.signOut();
                updateUI(this,mAuth);
                */
                Intent logout = new Intent(Beranda.this, LogoutActivity.class);
                startActivity(logout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_beranda:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BerandaFragment()).commit();
                updateUI(this,mAuth);
                break;
            case R.id.nav_kelas:
                if(userLoggedin()){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new KelasFragment()).commit();
                } else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfilFragment()).commit();
                }

                updateUI(this,mAuth);
                break;
            case R.id.nav_notif:
                if(userLoggedin()){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new NotifikasiFragment()).commit();
                } else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfilFragment()).commit();
                }
                break;

            case R.id.nav_profil:
                if(userLoggedin()){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfilFragment()).commit();
                } else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfilFragment()).commit();
                }

                updateUI(this,mAuth);
                break;

            case R.id.nav_groupchat:
                //todo kalo log in baru bisa liat groupchat, tapi harusnya kalo dia ada access di database baru bisa liat
                if(userLoggedin()){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new GroupChatListFragment()).commit();
                } else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfilFragment()).commit();
                }
                updateUI(this,mAuth);
                break;


            case R.id.nav_keranjang:
                if(userLoggedin()){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new KeranjangFragment()).commit();
                } else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfilFragment()).commit();
                }
                updateUI(this,mAuth);
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void ReadUser(String s) {

        DocumentReference user = db.collection("users").document(s);

        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override

            public void onComplete(@NonNull Task< DocumentSnapshot > task) {

                if (task.isSuccessful()) {

                    DocumentSnapshot doc = task.getResult();

                    //role = doc.getBoolean("role");

                }

            }

        })

                .addOnFailureListener(new OnFailureListener() {

                    @Override

                    public void onFailure(@NonNull Exception e) {
                        // ajaib ini


                    }

                });

    }
    @Override
    public void onBackPressed() {

        if(drawer.isDrawerOpen(GravityCompat.START)){

            drawer.closeDrawer(GravityCompat.START);
        }
        super.onBackPressed();
    }

    public boolean userLoggedin(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        //gak butuh kah karena udah ada updateui ?
        updateUI(this,mAuth);

    }

    public void updateUI(Activity activity, FirebaseAuth auth){
        //update header name
        NavigationView navigationView = activity.findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.username);
        TextView email = headerView.findViewById(R.id.email);

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            username.setText(R.string.guest);
            email.setText("");
        }else{
            username.setText(user.getDisplayName());
            email.setText(user.getEmail());
        }


        //update action bar
        activity.invalidateOptionsMenu();

        //todo bikin buat reset fragment yang lagi di opennya, atau bikinnya di authuser change aja, cek API apa yang keganti pas user login
    }

    private void getRole (String UID){

        DocumentReference user = db.collection("users").document(UID);

        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override

            public void onComplete(@NonNull Task< DocumentSnapshot > task) {

                if (task.isSuccessful()) {

                    DocumentSnapshot doc = task.getResult();


                    role= doc.getBoolean("admin");





                }

            }

        })

                .addOnFailureListener(new OnFailureListener() {

                    @Override

                    public void onFailure(@NonNull Exception e) {
                        // ajaib ini


                    }

                });
    }
}
