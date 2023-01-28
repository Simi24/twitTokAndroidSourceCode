package com.example.twittokandroid.ui.bachecaTwok;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.example.twittokandroid.Interface.OnRecyclerViewClickListener;
import com.example.twittokandroid.R;
import com.example.twittokandroid.Repositories.SidRepository;
import com.example.twittokandroid.Repositories.Twok;
import com.example.twittokandroid.databinding.FragmentHomeBinding;
import com.example.twittokandroid.ui.seguiti.SeguitiViewModel;

public class BachecaTwokFragment extends Fragment implements OnRecyclerViewClickListener {

    private FragmentHomeBinding binding;
    private TwokRepository twokRepository;

    private static String SIDSHEREDPREFERENCES = "MySid";
    private static String SIDTABLE = "SID";
    private static SharedPreferences prefs;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BachecaTwokViewModel bachecaTwokViewModel =
                new ViewModelProvider(this).get(BachecaTwokViewModel.class);

        Log.d("TAG", bachecaTwokViewModel.getText().getValue());

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ProgressBar progressBar = new ProgressBar(getContext());
        //binding.getRoot().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);


        RelativeLayout.LayoutParams layoutParams = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        RelativeLayout rl = new RelativeLayout(getContext());

        rl.setGravity(Gravity.CENTER);
        rl.addView(progressBar);

        progressBar.setLayoutParams(new RelativeLayout.LayoutParams(600, 600));
        binding.getRoot().addView(rl, layoutParams);

        SidRepository.initSid(getContext(), sid1 -> {
            Log.d("TAGSID", sid1+" inizializzo il sid!");
        }, e -> {
            e.printStackTrace();
        });

        ViewPager2 viewPager2 = binding.ViewPagerTwok;

        prefs = getContext().getSharedPreferences(SIDSHEREDPREFERENCES, MODE_PRIVATE);

        twokRepository = new ViewModelProvider(this).get(TwokRepository.class);
        twokRepository.setSid(prefs.getString(SIDTABLE, ""));

        AdapterTwok adapterTwok = new AdapterTwok(getContext(), this::onRecyclerViewClick, twokRepository);

        viewPager2.setAdapter(adapterTwok);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if(twokRepository.getTwoksMutableLiveData().getValue().size() - position < 7){
                    twokRepository.addTwok();

                }
            }

        });

        twokRepository.getConnectionMutableLiveData().observe(getViewLifecycleOwner(), isConnected -> {
            Log.d("TESTCONNECTION", isConnected + " connection state in fragment");
            if(!isConnected){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Abbiamo dei problemi di connessione!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("ONCLICKDIALOG", "il pulsante è schiacciato");
                        twokRepository.getConnectionMutableLiveData().setValue(true);
                        twokRepository.init();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        twokRepository.getTwoksMutableLiveData().observe(getViewLifecycleOwner(), twoks ->{
            progressBar.setVisibility(View.GONE);
            adapterTwok.updateList(twoks);
            adapterTwok.notifyDataSetChanged();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onRecyclerViewClick(View v, int position, Twok twok) {
        Log.d("ONCLICK", "Utente schiacciato "+twok.getName()+twok.getUid());
        Bundle bundle = new Bundle();
        bundle.putString("name", twok.getName());
        bundle.putInt("uid", twok.getUid());
        Navigation.findNavController(v).navigate(R.id.action_navigation_bacheca_twok_to_bachecaPersonale, bundle);
    }
}