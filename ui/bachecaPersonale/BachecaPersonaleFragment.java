package com.example.twittokandroid.ui.bachecaPersonale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.twittokandroid.DataSources.Followed;
import com.example.twittokandroid.DataSources.RetrofitInstance;
import com.example.twittokandroid.DataSources.SidUid;
import com.example.twittokandroid.R;
import com.example.twittokandroid.Repositories.PictureRepository;
import com.example.twittokandroid.Repositories.SidRepository;
import com.example.twittokandroid.databinding.FragmentBachecaPersonaleBinding;
import com.example.twittokandroid.ui.seguiti.SeguitiFragment;
import com.example.twittokandroid.ui.seguiti.SeguitiViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BachecaPersonaleFragment extends Fragment {

    private static final String NAME = "name";

    private String name;
    private TextView nameTextView;
    private ProgressBar progressBar;
    private FragmentBachecaPersonaleBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);


        SidRepository.initSid(getContext(), sid1 -> {
            Log.d("TAGSID", sid1+" inizializzo il sid!");
        }, e -> {
            e.printStackTrace();
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentBachecaPersonaleBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.buttonFollow.setVisibility(View.GONE);
        binding.headerBachecaUtente.setVisibility(View.GONE);
        binding.userPictureImageView.setVisibility(View.GONE);

        progressBar = new ProgressBar(getContext());

        RelativeLayout.LayoutParams layoutParams = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        RelativeLayout rl = new RelativeLayout(getContext());

        rl.setGravity(Gravity.CENTER);
        rl.addView(progressBar);

        progressBar.setLayoutParams(new RelativeLayout.LayoutParams(600, 600));

        //progressBar.setLayoutParams(layoutParams);
        //progressBar.setLayoutParams(new ViewGroup.LayoutParams(600, 600));

        binding.getRoot().addView(rl, layoutParams);


        //setHeader();


        binding.buttonFollow.setOnClickListener(l -> {
            binding.buttonFollow.setEnabled(false);
            Log.d("TESTPRESS", "bottone premuto");
            Integer uid = getArguments().getInt("uid");
            SidUid sidUid = new SidUid();

            sidUid.setSid(SidRepository.getSid().getSid());
            sidUid.setUid(getArguments().getInt("uid"));

            Call<Followed> followedCall = RetrofitInstance.getRetrofitInstance().isFollowed(sidUid);
            followedCall.enqueue(new Callback<Followed>() {
                @Override
                public void onResponse(Call<Followed> call, Response<Followed> response) {
                    if(!response.body().isFollowed()){

                        Call<Void> callFollow = RetrofitInstance.getRetrofitInstance().follow(sidUid);
                        callFollow.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Log.d("FOLLOW", "Followato " + uid);
                                SeguitiViewModel.setSeguitiMutableLiveData();
                                binding.buttonFollow.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.buttonFollow.setText("UnFollow");
                                    }
                                });
                                binding.buttonFollow.setEnabled(true);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    } else {
                        Call<Void> callUnFollow = RetrofitInstance.getRetrofitInstance().unFollow(sidUid);
                        callUnFollow.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Log.d("FOLLOW", "Unfollowato " + uid);
                                SeguitiViewModel.setSeguitiMutableLiveData();
                                binding.buttonFollow.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.buttonFollow.setText("Follow");
                                    }
                                });
                                binding.buttonFollow.setEnabled(true);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });

                    }
                }

                @Override
                public void onFailure(Call<Followed> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        });


        BachecaPersonaleViewModel model = new BachecaPersonaleViewModel(getArguments().getInt("uid"));

        ViewPager2 viewPager2 = binding.viewPagerBAchecaPersonale;


        AdapterBachecaPersonale adapterBachecaPersonale = new AdapterBachecaPersonale(getContext(), model);

        viewPager2.setAdapter(adapterBachecaPersonale);

        //BachecaPersonaleViewModel model = new ViewModelProvider(this).get(BachecaPersonaleViewModel.class);

        model.getConnectionMutableLiveData().observe(getViewLifecycleOwner(), isConnected -> {
            if(!isConnected){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Abbiamo dei problemi di connessione!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("ONCLICKDIALOG", "il pulsante è schiacciato");
                        model.getConnectionMutableLiveData().setValue(true);
                        model.init();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        model.getTwoksMutableLiveData().observe(getViewLifecycleOwner(), name -> {
            setHeader();
            adapterBachecaPersonale.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        });


        return view;
    }

    private void setHeader(){
        binding.buttonFollow.setVisibility(View.VISIBLE);
        binding.buttonFollow.setEnabled(false);
        binding.headerBachecaUtente.setVisibility(View.VISIBLE);
        binding.userPictureImageView.setVisibility(View.VISIBLE);

        String name = String.valueOf(getArguments().get(NAME));
        binding.headerBachecaUtente.setBackgroundColor(Color.parseColor("#B2BEB5"));
        Integer uid = getArguments().getInt("uid");
        binding.userName.setText(name);

        SidUid sidUid = new SidUid();

        sidUid.setSid(SidRepository.getSid().getSid());
        sidUid.setUid(getArguments().getInt("uid"));

        Log.d("FOLLOWED", SidRepository.getSid().getSid() + " " + uid);

        Call<Followed> followedCall = RetrofitInstance.getRetrofitInstance().isFollowed(sidUid);
        followedCall.enqueue(new Callback<Followed>() {
            @Override
            public void onResponse(Call<Followed> call, Response<Followed> response) {
                if(response.body().isFollowed()){
                    binding.buttonFollow.post(new Runnable() {
                        @Override
                        public void run() {
                            binding.buttonFollow.setText("UnFollow");
                        }
                    });
                    binding.buttonFollow.setEnabled(true);
                } else {
                    binding.buttonFollow.post(new Runnable() {
                        @Override
                        public void run() {
                            binding.buttonFollow.setText("Follow");
                        }
                    });
                    binding.buttonFollow.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<Followed> call, Throwable t) {
                t.printStackTrace();
            }
        });


        PictureRepository.getUserPicture(getContext(),SidRepository.getSid().getSid(), name, uid, 0, image -> {
            if(image != null){
                try{
                    byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    Log.d("IMAGEWIDTH", decodedImage.getWidth() + " " + name);
                    binding.userPictureImageView.post(new Runnable() {
                        @Override
                        public void run() {
                            binding.userPictureImageView.setImageBitmap(decodedImage);
                        }
                    });
                }catch (Exception e){
                Log.d("SETPICTURE", "errore durante il decoding dell'immagine");
                binding.userPictureImageView.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.userPictureImageView.setImageResource(R.drawable.placeholder_girl);
                    }
                });
            }

            } else {
                binding.userPictureImageView.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.userPictureImageView.setImageResource(R.drawable.placeholder_girl);
                    }
                });
            }

        }, t -> {
            t.printStackTrace();
        });
    }
}