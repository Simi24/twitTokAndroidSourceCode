package com.example.twittokandroid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.twittokandroid.Repositories.SidRepository;
import com.example.twittokandroid.Room.ProfileRepository;
import com.example.twittokandroid.ui.seguiti.SeguitiViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.twittokandroid.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ProgressBar progressBar;
    static ProfileRepository myDB;
    private SharedPreferences prefs;
    private String SIDTABLE = "SID";
    private String SIDSHEREDPREFERENCES = "MySid";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(SIDSHEREDPREFERENCES, MODE_PRIVATE);


        myDB = ProfileRepository.getInstance(getApplicationContext());

        if(prefs.getBoolean("firstrun", true)){
            Log.d("PROVASID", "Firstrun");
            SidRepository.initSid(this, sid -> {
                Log.d("PROVASID", "inizializzato il sid: " + sid);
                binding = ActivityMainBinding.inflate(getLayoutInflater());
                setContentView(binding.getRoot());
                SeguitiViewModel.getSeguitiMutableLiveData();
                loadFragments();
                progressBar = binding.progressBar3;
                progressBar.setVisibility(View.GONE);
                prefs.edit().putString(SIDTABLE, sid).commit();
                prefs.edit().putBoolean("firstrun", false).commit();

            }, t -> {
                Log.d("PROVASID", "errore nell'inizializzazione sid");
            });
        } else {
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            progressBar = binding.progressBar3;
            progressBar.setVisibility(View.VISIBLE);
            Log.d("PROVASID", "notFirstRun: "+prefs.getString(SIDTABLE, "non esiste"));
            SeguitiViewModel.getSeguitiMutableLiveData();
            loadFragments();
            progressBar.setVisibility(View.GONE);
        }

    }

    //GESTIRE COSA SUCCEDE QUANDO VIENE PREMUTO IL PULSANTE DI BACK
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
    }

    //Mi serve per fare funzionare la freccia di back nella bar
    @Override
    public boolean onSupportNavigateUp() {
        findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }



    private void loadFragments(){
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_bacheca_twok, R.id.navigation_add_twok, R.id.navigation_area_personale, R.id.navigation_seguiti)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //NASCONDO IL BOTTOM NAVIGATOR AI FRAGMENT BACHECAPERSONALE E MODIFICAPROFILO
        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            switch (navDestination.getId()){
                case R.id.bachecaPersonale:
                case R.id.modificaProfilo:
                    findViewById(R.id.nav_view).setVisibility(View.GONE);
                    break;
                default:
                    findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
                    break;
            }
        });
    }


}