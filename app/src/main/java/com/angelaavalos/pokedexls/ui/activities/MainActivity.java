package com.angelaavalos.pokedexls.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.angelaavalos.pokedexls.R;
import com.angelaavalos.pokedexls.ui.fragments.FirstFragment;
import com.angelaavalos.pokedexls.ui.fragments.SecondFragment;
import com.angelaavalos.pokedexls.ui.fragments.ThirdFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;



public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.pokedex);

    }
    FirstFragment firstFragment = new FirstFragment();
    SecondFragment secondFragment = new SecondFragment();
    ThirdFragment thirdFragment = new ThirdFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.pokedex) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, firstFragment).commit();
            return true;
        } else if (item.getItemId() == R.id.entrenador) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, secondFragment).commit();
            return true;
        } else if (item.getItemId() == R.id.tienda) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, thirdFragment).commit();
            return true;
        }
        return false;
    }

}