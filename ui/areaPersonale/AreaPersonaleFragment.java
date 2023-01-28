package com.example.twittokandroid.ui.areaPersonale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;

import com.example.twittokandroid.R;
import com.example.twittokandroid.databinding.FragmentAreaPersonaleBinding;

public class AreaPersonaleFragment extends Fragment {

    private FragmentAreaPersonaleBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAreaPersonaleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ProgressBar progressBar = new ProgressBar(getContext());


        RelativeLayout.LayoutParams layoutParams = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        RelativeLayout rl = new RelativeLayout(getContext());

        rl.setGravity(Gravity.CENTER);
        rl.addView(progressBar);

        progressBar.setLayoutParams(new RelativeLayout.LayoutParams(600, 600));
        binding.getRoot().addView(rl, layoutParams);

        Button button = binding.buttonInPersonale;
        button.setVisibility(View.GONE);
        binding.immagineProfilo.setVisibility(View.GONE);
        binding.textNotifications.setVisibility(View.GONE);


        ViewModelContextFactory factory = new ViewModelContextFactory(getContext());
        AreaPersonaleViewModel model = new ViewModelProvider((ViewModelStoreOwner) getViewLifecycleOwner(), (ViewModelProvider.Factory) factory).get(AreaPersonaleViewModel.class);

        model.getConnectionMutableLiveData().observe(getViewLifecycleOwner(), isConnected -> {
            if(!isConnected){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Abbiamo dei problemi di connessione!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("ONCLICKDIALOG", "il pulsante è schiacciato");
                        model.getConnectionMutableLiveData().setValue(true);
                        model.getNameMutableLiveData();
                        model.getProfilePictureMutableLiveData();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        model.getNameMutableLiveData().observe(getViewLifecycleOwner(), name ->{
            button.setVisibility(View.VISIBLE);
            binding.textNotifications.setVisibility(View.VISIBLE);
            if(name.equals("unnamed")){
                binding.textNotifications.setText("Non hai ancora scelto un nome utente");
            } else {
                binding.textNotifications.setText(name);
            }
        });

        model.getProfilePictureMutableLiveData().observe(getViewLifecycleOwner(), image ->{
            binding.immagineProfilo.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            if(image != null){
                byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                Log.d("PROFILO", decodedImage + " decodedImage");
                binding.immagineProfilo.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.immagineProfilo.setImageBitmap(decodedImage);
                    }
                });
            } else {
                binding.immagineProfilo.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.immagineProfilo.setImageResource(R.drawable.placeholder_girl);
                    }
                });
            }
        });

        button.setOnClickListener(v -> {
            Log.d("TAG", "pulsante schiacciato");
            Navigation.findNavController(v).navigate(R.id.action_navigation_area_personale_to_modificaProfilo);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}