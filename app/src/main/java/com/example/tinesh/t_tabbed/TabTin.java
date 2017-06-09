package com.example.tinesh.t_tabbed;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

public class TabTin extends AppCompatActivity {
   int count=0;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_tin);
        addListenerOnButton();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        }
        );

    }

    @Override  //Uploads the image after selection into the ImageView
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imgView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }


    }
    private void addListenerOnButton() {


    }


    public void setLogin(View view) {

    }
    //Touch anywhere on Tab1

    public void tab1myMethod(View pView) {
        if(count==0)

        {
            new FancyShowCaseQueue()
                    .add(new FancyShowCaseView.Builder(this)
                            .focusOn(findViewById(R.id.btnSendTab1))
                            .title("Click to send the report")
                            .build()

                                  )
                    .add(new FancyShowCaseView.Builder(this)
                            .focusOn(findViewById(R.id.tabs))
                            .title("Seperate Tabs")
                            .build()
                    )
                    .add(new FancyShowCaseView.Builder(this)
                            .focusOn(findViewById(R.id.buttonLoadPicture))
                            .title("Click this button to load pictures")
                            .build()
                    )
                    .show();

            count++;
        }
    }
    public void Something(View view) {

        new FancyShowCaseView.Builder(this)
                .title("Focus")
                .focusOn(findViewById(R.id.btnSendTab1))
                //.focusOn(findViewById(R.id.tabs))
                .focusCircleRadiusFactor(2.0)
                .build()
                .show();

        makeText(this, "Clicked on Button", LENGTH_LONG).show();
    }
             //click on loadpic button
    public void LoadPic(View view) {
        //Opens permission dialogue for android 6 Marshmellow and then calls OnRequestPermissionResult();
        ActivityCompat.requestPermissions(TabTin.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);


        //Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
        //buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            //@Override
            //public void onClick(View arg0) {


            //}
        //});

    }
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                //Request granted , so open the gallary and pick up ny picture
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                    //After this it calls onActivityResult() to upload the picture

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else

                    {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(TabTin.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    /**
     * A placeholder fragment containing a simple view.
     */


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
         switch(position)
         {case 0:
             Tab1 tab1=new Tab1();
             return tab1;
             case 1:
             Tab2 tab2=new Tab2();
             return tab2;
             case 2:
             Tab3 tab3=new Tab3();
             return tab3;

         }
         return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Issues";
                case 1:
                    return "Suggesions";
                case 2:
                    return "Interface";
            }
            return null;
        }
    }
}
