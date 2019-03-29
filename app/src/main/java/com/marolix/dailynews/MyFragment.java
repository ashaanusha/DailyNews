package com.marolix.dailynews;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.TimerTask;

public class MyFragment extends Fragment {
  Map<String,String> text;
    private ImageView[] dots;
    private static final String TEXT = "text";
    private TextView textView,textView1,time;
    String[] arr;
    File imagePath;
    static boolean flag=false;
  LinearLayout  sliderDotspanel;
    ImageView imageView;
    public MyFragment() {
        // Required empty public constructor
    }
    public static MyFragment newInstance(Map<String,String> al) {
        MyFragment fragment = new MyFragment();
        Bundle bundle = new Bundle(1);
        bundle.putSerializable("LIST", (Serializable) al);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle myBundle=getArguments();
      text = (Map<String,String>)myBundle.getSerializable("LIST");
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my, container, false);
        textView = view.findViewById(R.id.textview);
        time=view.findViewById(R.id.time);
        textView1 = view.findViewById(R.id.text);
        imageView=view.findViewById(R.id.img);
//        onStop();
//        onResume();
//        flag=false;
        final RelativeLayout relativeLayout = view.findViewById(R.id.relative);

        final RelativeLayout relativeLayout1 = view.findViewById(R.id.relative1);
        ImageView share=view.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!text.get("urltovideo").equals("null"))
                {
                    shareItVideo();
                }
                else
                {
                    relativeLayout1.setVisibility(view.GONE);
                    Bitmap bitmap = takeScreenshot(v);
                    saveBitmap(bitmap);
                    shareIt();
                    relativeLayout1.setVisibility(view.VISIBLE);
                }

            }
        });
//      relativeLayout.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if(flag==false)
//                {
//                    onStop();
//                    flag=true;
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            onResume();
//                        }
//                    }, 3000);
//                }
//                else
//                {
//                    flag=false;
//                    onResume();
//                }
//
//
//            }
//        });
        sliderDotspanel = (LinearLayout)view. findViewById(R.id.SliderDots);

        textView.setText(text.get("heading"));
        textView1.setText(text.get("description"));
        time.setText(text.get("publishdate"));
        FrameLayout youtube_fragment=view.findViewById(R.id.youtube_fragment);
        FrameLayout imgframe=view.findViewById(R.id.img1);
        Log.e("type",text.get("type"));
        if(text.get("type").equals("wholepage"))
        {


            Picasso.with(getActivity()).load(text.get("urltoimage")).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    relativeLayout.setBackground(new BitmapDrawable(bitmap));
                  LinearLayout lay = (LinearLayout)view.findViewById(R.id.desc);
                  LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)lay.getLayoutParams();
                  lp.setMargins(0, 250, 0, 0);
                  lay.setLayoutParams(lp);
                  textView.setTextColor(Color.parseColor("#ffffff"));
                  textView1.setTextColor(Color.parseColor("#ffffff"));

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }

        else if(text.get("type").equals("multipleimages"))
        {

            ViewPager viewPager = (ViewPager)view. findViewById(R.id.view_pager);
            viewPager.setVisibility(view.VISIBLE);
            arr=new String[Integer.valueOf(text.get("arraylength"))];
            for(int i=0;i<Integer.valueOf(text.get("arraylength"));i++)
            {
                arr[i]=text.get("array"+i);
            }

            ViewPagerAdapter6 adapter = new ViewPagerAdapter6(getActivity(),arr);

            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(0);


           final  int dotscount = adapter.getCount();
            dots = new ImageView[dotscount];

            for(int i = 0; i < dotscount; i++){

                dots[i] = new ImageView(getActivity());
                dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.non_active_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                params.setMargins(8, 0, 8, 0);

                sliderDotspanel.addView(dots[i], params);

            }

            dots[0].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    for(int i = 0; i< dotscount; i++){
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.non_active_dot));
                    }

                    dots[position].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }
    else if(!(text.get("urltoimage").equals("null")))
        {
         Picasso.with(getContext())
            .load(text.get("urltoimage"))
            .into(imageView);
    //imageView.setMaxWidth(200);
   imageView.setVisibility(View.VISIBLE);

}
    else
{
    MediaController mc= new MediaController(getActivity());
    final String path = text.get("urltovideo");
    youtube_fragment.setVisibility(View.VISIBLE);
    YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

    youTubePlayerFragment.initialize("AIzaSyCAcBppHFZ1WsVJ1id2O4DyabI4bHMdbYw", new YouTubePlayer.OnInitializedListener() {

        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {

            if (!wasRestored) {
                player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                player.loadVideo(path);
//             player.setFullscreen(true);
//                player.loadVideo(path);
               player.play();

            }

        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        }


    });

    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
    transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();

//    youTubePlayerFragment.initialize("AIzaSyCAcBppHFZ1WsVJ1id2O4DyabI4bHMdbYw", new YouTubePlayer.OnInitializedListener() {
//
//        @Override
//        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
//                                            boolean wasRestored) {
//            if (!wasRestored) {
//
//
//                //set the player style default
//                player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
//
//                //cue the 1st video by default
//                player.cueVideo(text.get(path));
//            }
//        }
//
//        @Override
//        public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
//
//            //print or show error if initialization failed
//            Log.e("youtube", "Youtube Player View initialization failed");
//        }
//    });

}

        return view;
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
//    }
//    @Override
//    public void onStop() {
//        super.onStop();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
//    }
private void shareIt() {
    Uri uri = Uri.fromFile(imagePath);
    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
    sharingIntent.setType("image/*");
    //String shareBody = "My highest score with screen shot";
    //sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Catch score");
    //sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
    sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

    startActivity(Intent.createChooser(sharingIntent, "Share via"));
}
    private void shareItVideo() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v="+text.get("urltovideo")+"\n"+text.get("heading"));
        sendIntent.setType("text/plain");

        startActivity(sendIntent);


//        ShareCompat.IntentBuilder
//                .from(getActivity())
//                .setType("text/plain")
//                .setChooserTitle("Share URL")
//                .setText("https://www.youtube.com/watch?v="+text.get("urltovideo"))
//                .startChooser();
//            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);

        //String shareBody = "My highest score with screen shot";
//        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Catch score");
//        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "asha");
        //sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
//        sharingIntent.setType("video/*");
//        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,text.get("heading"));
//        sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM,Uri.parse("https://www.youtube.com/watch?v="+text.get("urltovideo")));
//        startActivity(Intent.createChooser(sharingIntent,"share:"));

    }
    private void saveBitmap(Bitmap bitmap) {
        imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png"); ////File imagePath
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }
    public Bitmap takeScreenshot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

}
