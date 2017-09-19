package com.example.tinesh.t_tabbed;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;
import android.content.Context;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.tinesh.t_tabbed.R.layout.activity_tab_tin;
import static com.example.tinesh.t_tabbed.R.layout.tab_1;

/**
 * Created by Tinesh on 5/29/2017.
 */
//OK //ok
public class
Tab_1 extends Fragment{
    Button buttonPrev, buttonNext;

    TextView txtView7;
    TextView txtviewIssue;
    TextView txtviewPictureLocation;
    TextView txtviewSend;
    TextView tv;
    TextView textthankyou;
    private LocationListener listener;
    ViewAnimator viewAnimator;
    Animation slide_in_left, slide_out_right;
    private LocationManager locationManager3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        View view=inflater.inflate(tab_1,container,false);


        tv = (TextView) view.findViewById(R.id.mywidget);
        tv.setSelected(true);
        tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tv.setText("Please provide us all the details for a faster resolution of the Issue..");
        tv.setSelected(true);
        tv.setSingleLine(true);

        //setContentView(R.layout.activity_main);
        locationManager3 = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //setContentView(R.layout.tab_3);
                // textView = (TextView) findViewById(R.id.textView1);
                //textView.setText("\n " + location.getLongitude() + " " + location.getLatitude());
                Geocoder geocoder = new Geocoder(getContext().getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if (null != addresses && addresses.size() > 0) {
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();

                        //txtView7 = (TextView)getView().findViewById(R.id.textView7);
                        //txtView7.setText(address + "City is :" + city + "state is :" + state + "country is " + country + "postal code " + postalCode + "known name " + knownName);

                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                /*Tab_3 myFragment = new Tab_3();
                myFragment.setTextViewText("\n " + location.getLongitude() + " " + location.getLatitude());
                */
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override //checks if gps is off,if so it takes user to settings where he enables gps
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        ((TabTin) getActivity()).hideFloatingActionButton(); //Hide send flo button initially
        buttonPrev = (Button)view.findViewById(R.id.prev);
        buttonNext = (Button)view.findViewById(R.id.next);
        viewAnimator = (ViewAnimator)view.findViewById(R.id.viewanimator);
        textthankyou=(TextView)view.findViewById(R.id.textView9);
        //text to display
        Spanned text = Html.fromHtml("<font color=\"purple\"><i>Issue Description</i></font>-->Picture & Location-->Send");
        Spanned text1 = Html.fromHtml("Issue Description--><font color=\"purple\"><i>Picture & Location</i></font>-->Send");
        Spanned text2 = Html.fromHtml("Issue Description-->Picture & Location--><font color=\"purple\"><i>Send</i></font>");
        txtviewPictureLocation =(TextView)view.findViewById(R.id.textView4);
        txtviewPictureLocation.setText(text1);

        txtviewIssue =(TextView)view.findViewById(R.id.textView3);
        txtviewIssue.setText(text);

        txtviewSend =(TextView)view.findViewById(R.id.textView5);
        txtviewSend.setText(text2);

        slide_in_left = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right);

        final Animation animation = new AlphaAnimation(1, 0); 
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        ImageView img = (ImageView)view.findViewById(R.id.imageView4);

        img.startAnimation(animation);
        buttonPrev.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view1) {
                viewAnimator.setInAnimation(slide_in_left);
                viewAnimator.showPrevious();
                getView(view1);
            }});

        buttonNext.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view1) {
                viewAnimator.setOutAnimation(slide_out_right);
                viewAnimator.showNext();
                getView(view1);
            }});
        return view;
    }

    private View getView(View view) {
        if(viewAnimator.getDisplayedChild()==2)
        {
            buttonNext.setVisibility(view.INVISIBLE);
            buttonPrev.setVisibility(view.VISIBLE);
            textthankyou.setVisibility(view.VISIBLE);
            ((TabTin) getActivity()).showFloatingActionButton(); //Show send button finally
        }
        if(viewAnimator.getDisplayedChild()==1)
        {
            buttonNext.setVisibility(view.VISIBLE);
            buttonPrev.setVisibility(view.VISIBLE);
            textthankyou.setVisibility(view.INVISIBLE);
            ((TabTin) getActivity()).hideFloatingActionButton();
        }
        if(viewAnimator.getDisplayedChild()==0)
        {
            buttonNext.setVisibility(view.VISIBLE);
            buttonPrev.setVisibility(view.INVISIBLE);
            textthankyou.setVisibility(view.INVISIBLE);
            ((TabTin) getActivity()).hideFloatingActionButton();//hide send button
        }
        return view;
    }

}