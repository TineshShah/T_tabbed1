package com.example.tinesh.t_tabbed;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.jaredrummler.android.device.DeviceName;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.example.tinesh.t_tabbed.R.id.container;
import static java.security.AccessController.getContext;

public class TabTin extends AppCompatActivity{

    int count = 0;
    private Button b;
    private TextView textView;
    private LocationManager locationManager;
    private LocationListener listener;
    private static final int REQ_CODE_SPEECH_INPUT = 100;

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
    //private static int RESULT_LOAD_IMAGE = 1;
    private File filetosend;
    private Uri fileuri;
    private int flag;
    private EditText mVoiceInputTv;
    private ImageButton mSpeakBtn;
    Button btnNext,btnPrevious;
    ViewAnimator viewAnimator;
    TextView tv;
    private String txtCompleteAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_tin);

        //location Manager for getting location Details
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //setContentView(R.layout.tab1);
                // textView = (TextView) findViewById(R.id.textView1);
                //textView.setText("\n " + location.getLongitude() + " " + location.getLatitude());
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if (null != addresses && addresses.size() > 0) {
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();

                        textView = (TextView) findViewById(R.id.textView7);
                        textView.setText(address + "City is :" + city + "state is :" + state + "country is " + country + "postal code " + postalCode + "known name " + knownName);
                        txtCompleteAddress=address + "  City is :"+city + "  state is :" + state + "  country is " + country +"  postal code " + postalCode + "  known name" + knownName;
                        Log.e("Address",txtCompleteAddress);

                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                /*Tab1 myFragment = new Tab1();
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

        Locale.getDefault().getDisplayLanguage();  //Load Default language

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //send button
        final FloatingActionButton fabSend = (FloatingActionButton) findViewById(R.id.floSend);
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Depending on the current position,Filename changes
                mViewPager = (ViewPager) findViewById(container);
                String FileName;
                if (mViewPager.getCurrentItem()==0)
                {
                    FileName="Issues_";
                }
                else if(mViewPager.getCurrentItem()==1)
                {
                    FileName="Suggestions_";

                }
                else if(mViewPager.getCurrentItem()==2)
                {
                    FileName="LookandFeel_";
                }
                else
                {
                    FileName="Default";
                }
           filetosend =generateNoteOnSD(TabTin.this,FileName,"hello"); //(context,Name of file,Content of file)
                new SaveAsyncTask().execute(); //new thread
            }
            class SaveAsyncTask extends AsyncTask<Object, Object, Void> {

                protected void onPostExecute(Intent i) {
                }
                @Override
                protected Void doInBackground(Object... params) {
                    String deviceName = DeviceName.getDeviceName();
                    String reqString = Build.MANUFACTURER
                            + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                            + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();
                    Log.e("ok", reqString);
                    ArrayList<Uri> imageUris = new ArrayList<Uri>();//create array list to store URI for image and Report
                    imageUris.add(selectedImageUri); // Add your image URIs to array
                    imageUris.add(Uri.parse("file://" + filetosend.getAbsoluteFile())); //add Report/textfile Uri to array
//                                       Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                                               .setAction("Action", null).show();
                    Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);//to send multiple attachments
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"Feedback_for_@gmail.com"});
                    i.putExtra(Intent.EXTRA_CC,new String[]{"Feedback_for_@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                    i.putExtra(Intent.EXTRA_TEXT, deviceName);
                    //i.setType("image/*");
                    Log.e("ok", filetosend.getAbsolutePath());
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_STREAM,imageUris); //all the Urls in the arraylist are added to the email application(text file and image)
                    //i.putExtra(Intent.EXTRA_STREAM,Uri.parse("file://" + filetosend.getAbsoluteFile()));
                    try {
                        startActivity(Intent.createChooser(i, "Select an Email application"));
                    }
                    catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(TabTin.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                    return null;
                }
            }
            //Gathering values
            });
    }

    //@Override
//    protected void onResume() {
//        super.onResume();
//        ArrayList<ItemData> list=new ArrayList<>();
//        list.add(new ItemData("En",R.mipmap.us_fllag));
//        list.add(new ItemData("De",R.mipmap.ic_launcher));
//        //list.add(new ItemData("Usd",R.drawable.usd));
//        //list.add(new ItemData("Jpy",R.drawable.jpy));
//        //list.add(new ItemData("Aud",R.drawable.aud));
//        Spinner sp=(Spinner)findViewById(R.id.spinner);
//        SpinnerAdapter adapter=new SpinnerAdapter(this,
//                R.layout.spinner_layout,R.id.txt,list);
//
//        sp.setAdapter(adapter);
//        final int iCurrentSelection = sp.getSelectedItemPosition();
//        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                switch (position) {
//                    case 0:
//                        Toast.makeText(parent.getContext(), "Spinner item 1!", Toast.LENGTH_SHORT).show();
//                        String languageToLoad = "default"; // your language
//                            Locale locale = new Locale(languageToLoad);
//                            Locale.setDefault(locale);
//                            Configuration config = new Configuration();
//                            config.locale = locale;
//                            getBaseContext().getResources().updateConfiguration(config,
//                                    getBaseContext().getResources().getDisplayMetrics());
//
//
//                        break;
//                    case 1:
//                        Toast.makeText(parent.getContext(), "Spinner item 2!", Toast.LENGTH_SHORT).show();
//                        break;
//                    case 2:
//                        Toast.makeText(parent.getContext(), "Spinner item 3!", Toast.LENGTH_SHORT).show();
//                        break;
//                }
////                    if (position == 0) {
////                        if(iCurrentSelection==1) {
////                            ImageView imageView = (ImageView) parent.findViewById(R.id.img);
////                            imageView.setImageResource(R.mipmap.ic_launcher);
////                            String languageToLoad = "de"; // your language
////                            Locale locale = new Locale(languageToLoad);
////                            Locale.setDefault(locale);
////                            Configuration config = new Configuration();
////                            config.locale = locale;
////                            getBaseContext().getResources().updateConfiguration(config,
////                                    getBaseContext().getResources().getDisplayMetrics());
////
////                        }
////                    }
////                    if (position == 1) {
////                        if (iCurrentSelection == 0) {
////                            String languageToLoad = "Default"; // your language
////                            Locale locale = new Locale(languageToLoad);
////                            Locale.setDefault(locale);
////                            Configuration config = new Configuration();
////                            config.locale = locale;
////                            getBaseContext().getResources().updateConfiguration(config,
////                                    getBaseContext().getResources().getDisplayMetrics());
////                            ImageView imageView = (ImageView) parent.findViewById(R.id.img);
////                            imageView.setImageResource(R.mipmap.us_fllag);
////                            finish();
////                            startActivity(getIntent());
////
////                        }
////                    }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//
//        });
//
//
//
//    }
    public File generateNoteOnSD(TabTin context, String sFileName, String sBody) {
        File gpxfile = null;
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return gpxfile;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) //Activity called after the Oncreate
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
                            .focusOn(findViewById(R.id.floatingAdd))
                            .title("Click this button to load pictures")
                            .build()
                    )
                    .show();


        return true;
    }


    private static final int RESULT_LOAD_IMAGE = 101;
    String picturePath ;
    Uri selectedImage;
    String uritv;
    private String selectedImagePath;
    Uri selectedImageUri;
    @Override  //Uploads the image after selection into the ImageView //Shows the text of the speech input.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RESULT_LOAD_IMAGE:
            {
                if (resultCode == RESULT_OK && null != data) {
                selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();

                ImageView imageView = (ImageView) findViewById(R.id.imgView);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                selectedImageUri = data.getData();
                }
                break;
             }
            case REQ_CODE_SPEECH_INPUT: {
                mVoiceInputTv = (EditText)findViewById(R.id.editTextDescription);
                     if (resultCode == RESULT_OK && null != data) {
                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        String text =result.get(0);
                        mVoiceInputTv.setText(text);
                    }

                    break;

            }//End of case2
        } //End of switch
    }
            //selectedImagePath = getPath(selectedImageUri);
            //uritv=selectedImagePath.toString();



    private String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    //Touch anywhere on Tab1
    public void tab1myMethod(View pView) {
        if (count == 0)
        {
            new FancyShowCaseQueue()
                    .add(new FancyShowCaseView.Builder(this)
                            .title("Click to send the report")
                            .focusOn(findViewById(R.id.btnSendTab1))
                            .focusCircleRadiusFactor(2.0)
                            .build()

                    )
                    .add(new FancyShowCaseView.Builder(this)
                            .focusOn(findViewById(R.id.tabs))
                            .title("Seperate Tabs")
                            .build()
                    )
                    .add(new FancyShowCaseView.Builder(this)
                            .focusOn(findViewById(R.id.floatingAdd))
                            .title("Click this button to load pictures")
                            .build()
                    )
                    .show();

            count++;
        }
    }

    public void Something(View view) {  //Click on the Button

        new FancyShowCaseView.Builder(this)
                .title("Focus")
                .focusOn(findViewById(R.id.btnSendTab1))
                //.focusOn(findViewById(R.id.tabs))
                .focusCircleRadiusFactor(2.0)
                .build()
                .show();

        makeText(this, "Clicked on Button", LENGTH_LONG).show();

        String languageToLoad  = "de"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());


        finish();//refresh the view
        startActivity(getIntent());

    }

       public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(TabTin.this, "Permission denied for location access", Toast.LENGTH_SHORT).show();
                        //return;
                    }
                    else
                        {
                    Toast.makeText(TabTin.this, "Permission granted for location access.Please switch on Location", Toast.LENGTH_SHORT).show();
                    // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
                         locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 5000, 0, listener);
                         }
                      return;
                }
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
    public void switchOn(View view) {
        Switch simpleSwitch1;
        simpleSwitch1 = (Switch) findViewById(R.id.sharelocation);
        if (simpleSwitch1.isChecked())
        {
            if (ActivityCompat.checkSelfPermission(TabTin.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                            , 10);
                }
                return;

            }
            // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
            //noinspection MissingPermission
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 5000, 0, listener);
        }
        else
         {
            Toast.makeText(TabTin.this, "Switch is off", Toast.LENGTH_SHORT).show();
         }
    }

    public void AddPic(View view) {
        ActivityCompat.requestPermissions(TabTin.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

    }
    public void btnhelp(View view) {

        mViewPager = (ViewPager) findViewById(container);
        if (mViewPager.getCurrentItem()==0) //display the following when the viewpager is in first Tab.
        {
            new FancyShowCaseQueue()
                    .add(new FancyShowCaseView.Builder(this)
                            .title("Click here to send the Report")
                            .focusOn(findViewById(R.id.btnSendTab1))
                            .focusCircleRadiusFactor(2.0)
                            .build()
                    )
                    .add(new FancyShowCaseView.Builder(this)
                            .focusOn(findViewById(R.id.tabs))
                            .title("Seperate Tabs for Reporting Issues or providing Suggestions or for UI design improvements")
                            .build()
                    )
                    .add(new FancyShowCaseView.Builder(this)
                            .focusOn(findViewById(R.id.floatingAdd))
                            .title("Click here to add pictures of the Issue faced")
                            .build()
                    )
                    .show();
        }


    }

    public void btnSpeech(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please describe your issue");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Speech recognition is not supported in this device.",
                    Toast.LENGTH_SHORT).show();

        }
    }

    public void switchOn3(View view) { //Switch for location in tab3
        Switch simpleSwitch1;
        simpleSwitch1 = (Switch) findViewById(R.id.switch1);
        if (simpleSwitch1.isChecked())
        {
            if (ActivityCompat.checkSelfPermission(TabTin.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                            , 10);
                }
                return;

            }
            // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
            //noinspection MissingPermission
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 5000, 0, listener);
        }
        else
        {
            Toast.makeText(TabTin.this, "Switch is off", Toast.LENGTH_SHORT).show();
        }
    }

    public void inGerman(View view) {
        String languageToLoad1 = "de"; // your language
        Locale locale1 = new Locale(languageToLoad1);
        Locale.setDefault(locale1);
        Configuration config1 = new Configuration();
        config1.locale = locale1;
        getBaseContext().getResources().updateConfiguration(config1,
                getBaseContext().getResources().getDisplayMetrics());
        finish();
        startActivity(getIntent());
        ImageButton imgb=new ImageButton(this);
        imgb.setVisibility(view.GONE);

    }

    public void inEnglish(View view) {
        String languageToLoad1 = "Default"; // your language
        Locale locale1 = new Locale(languageToLoad1);
        Locale.setDefault(locale1);
        Configuration config1 = new Configuration();
        config1.locale = locale1;
        getBaseContext().getResources().updateConfiguration(config1,
                getBaseContext().getResources().getDisplayMetrics());
        finish();
        startActivity(getIntent());
        //ImageButton imgb=new ImageButton(this);
        //imgb.setVisibility(view.GONE);



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
            String a = Locale.getDefault().getLanguage();
            switch (position) {

                case 0:
                    if (a == "de")
                        return "Probleme";
                    else
                        return "Issues";
                case 1:
                    if (a == "de")
                        return "Vorschläge";
                    else
                        return "Sugessions";
                case 2:
                    if (a == "de")
                        return "Optik und Haptik";
                    else
                        return "Look & Feel";
            }

            return null;
        }
    }
}
