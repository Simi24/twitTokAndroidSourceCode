package com.example.twittokandroid.ui.seguiti;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.twittokandroid.Interface.OnRecyclerViewSeguitiClickListener;
import com.example.twittokandroid.R;
import com.example.twittokandroid.Repositories.Profile;
import com.example.twittokandroid.databinding.FragmentBachecaPersonaleBinding;
import com.example.twittokandroid.databinding.FragmentSeguitiBinding;

public class SeguitiFragment extends Fragment implements OnRecyclerViewSeguitiClickListener {

    private FragmentSeguitiBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentSeguitiBinding.inflate(inflater, container, false);

        View view = binding.getRoot();

        SeguitiViewModel seguitiViewModel = new ViewModelProvider(this).get(SeguitiViewModel.class);



        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AdapterSeguiti adapterSeguiti = new AdapterSeguiti(getContext(), seguitiViewModel, this::onRecyclerViewClick);
        recyclerView.setAdapter(adapterSeguiti);

        seguitiViewModel.getConnectionMutableLiveData().observe(getViewLifecycleOwner(), isConnected -> {
            if(!isConnected){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Abbiamo dei problemi di connessione!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("ONCLICKDIALOG", "il pulsante è schiacciato");
                        seguitiViewModel.getConnectionMutableLiveData().setValue(true);
                        seguitiViewModel.init();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        seguitiViewModel.getSeguitiMutableLiveData().observe(getViewLifecycleOwner(), seguiti ->{
            Log.d("SEGUITI", "viene chiamato il metodo observe");
            Log.d("SEGUITI", seguiti.size()+" siamo nel fragment");
            adapterSeguiti.notifyDataSetChanged();
            adapterSeguiti.updateList();
        });

        return view;
    }


    @Override
    public void onRecyclerViewClick(View v, int position, Profile profile) {
        Log.d("SEGUITICLICK", "List clicked at position: " + position + profile.getUid());
        Bundle bundle = new Bundle();
        bundle.putString("name", profile.getName());
        bundle.putInt("uid", profile.getUid());
        Navigation.findNavController(v).navigate(R.id.action_navigation_seguiti_to_bachecaPersonale, bundle);
    }
}