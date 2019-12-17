package com.example.krucils;

import android.app.Activity;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfilFragment extends Fragment {
    private FirebaseAuth mAuth;
    private Button registerhomeBtn, loginBtn, logoutBtn;
    private TextView logintitle;
    private final Beranda beranda = new Beranda();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main, container, false);
        //getActivity().invalidateOptionsMenu();

        mAuth = FirebaseAuth.getInstance();

        registerhomeBtn=v.findViewById(R.id.register);
        loginBtn=v.findViewById(R.id.login);
        logintitle=v.findViewById(R.id.main_title);
        logoutBtn=v.findViewById(R.id.logout);



        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            logintitle.setText(getText(R.string.welcome) + name);
            loginBtn.setVisibility(View.GONE);
            registerhomeBtn.setVisibility(View.GONE);
            logoutBtn.setVisibility(View.VISIBLE);
        }else{
            String name = getText(R.string.guest).toString();
            logintitle.setText(getText(R.string.welcome) + name);
            loginBtn.setVisibility(View.VISIBLE);
            registerhomeBtn.setVisibility(View.VISIBLE);
            logoutBtn.setVisibility(View.GONE);
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                ProfilFragment.this.startActivityForResult(loginIntent, 10001);
            }
        });

        registerhomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getActivity(), RegisterActivity.class);
                ProfilFragment.this.startActivityForResult(registerIntent, 10001);
            }
        });


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                //todo ini kan buat restart fragment jadi mungkin coba pindahin ke updateUI
                Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentFragment instanceof ProfilFragment) {
                    FragmentTransaction fragTransaction =   (getActivity()).getSupportFragmentManager().beginTransaction();
                    fragTransaction.detach(currentFragment);
                    fragTransaction.attach(currentFragment);
                    fragTransaction.commit();
                }

                //getActivity().invalidateOptionsMenu();//gak butuh kah karena udah update ?
                beranda.updateUI(getActivity(),mAuth);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 10001) && (resultCode == Activity.RESULT_OK))
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        beranda.updateUI(getActivity(),mAuth);
    }

}