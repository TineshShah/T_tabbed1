package com.example.tinesh.t_tabbed;
import android.Manifest;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.wifi.WifiManager;
import android.os.StrictMode;
import android.support.v4.content.FileProvider;
import android.widget.CheckBox;
import android.widget.MediaController;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
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
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewAnimator;
import com.jaredrummler.android.device.DeviceName;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.FocusShape;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.example.tinesh.t_tabbed.R.id.container;
import static com.example.tinesh.t_tabbed.RecordService.MP4File;


public class MyActivity extends AppCompatActivity  implements ConnectivityReceiver.ConnectivityReceiverListener {
    private String Connection_Status="Not Detected";
    private String SelectedImageName;

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);

    }

    public static class MyApplication extends Application {
        private static MyApplication mInstance;

        @Override
        public void onCreate() {
            super.onCreate();

            mInstance = this;
        }

        public static synchronized MyApplication getInstance() {
            return mInstance;
        }

        public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
            ConnectivityReceiver.connectivityReceiverListener = listener;
        }

        // This class can access everything from its parent...
    }
    //For recording
    private Button btnCheck;

    private static final int RECORD_REQUEST_CODE  = 105;
    private static final int STORAGE_REQUEST_CODE = 102;
    private static final int AUDIO_REQUEST_CODE   = 103;
    private static final long DELAY = 31000;
    private MediaProjectionManager projectionManager;
    private MediaProjection mediaProjection;
    private RecordService recordService;

    //end of recording
    int count = 0;
    private Button b;
    private TextView textView;
    private LocationManager locationManager;
    private LocationListener listener;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private CheckBox chkIos;

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
    private File textfiletosend;
    private Uri fileuri;
    private int flag;
    private EditText mVoiceInputTv;
    private EditText mVoiceInputTv1;
    private ImageButton mSpeakBtn;
    Button btnNext,btnPrevious;
    ViewAnimator viewAnimator;
    TextView tv;
    public String CompleteAddress;
    private String Fail_Per_other;
    private String IssueOccursOn;//type of issue
    private FloatingActionButton VideoStartBtn;
    private String lookandfeel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_tin);
        hideFloatingActionButton();//Hide send button
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
//        if(Build.VERSION.SDK_INT>=24){
//            try{
//                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
//                m.invoke(null);
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }


        projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        //location Manager for getting location Details
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
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
                        //textView = (TextView) findViewById(R.id.textView7);
                        //textView.setText(address + "City is :" + city + "state is :" + state + "country is " + country + "postal code " + postalCode + "known name " + knownName);
                        CompleteAddress=address+"_City_"+city + "_state_" + state + "_country_" + country +"_postalcode_" + postalCode + "_knownname_" + knownName;
                        Log.e("Address",CompleteAddress);
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
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
        //check internet connection
        // Manually checking internet connection
        checkConnection();
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
        //For Video Recording

        if (ContextCompat.checkSelfPermission(MyActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)    {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MyActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(getApplicationContext(),"Permission denied again.Providing the permission helps in storing & sending the required data", Toast.LENGTH_SHORT).show();
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            }
            else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
            }
        }
        Intent intent = new Intent(this, RecordService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
        final String[] FileName = new String[1];
        //send button
        final FloatingActionButton fabSend = (FloatingActionButton) findViewById(R.id.floSend);
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Depending on the current position,Filename changes
                mViewPager = (ViewPager) findViewById(container);
                //rating
                RatingBar ratingBar1;
                RatingBar ratingBar2;
                RatingBar ratingBar3;
                //device details
                String devicedetails = Build.MANUFACTURER
                        + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                        + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();
                //DeviceName
                String deviceName = DeviceName.getDeviceName();

                //Wifi&Signal strength in number
                WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
                int linkSpeed = wifiManager.getConnectionInfo().getRssi();

                //WIFI signal strength in words
                String Wifisignalstrength="Null";
                if (linkSpeed>-50)
                {
                    Wifisignalstrength="Excellent";
                }
                else if (linkSpeed<-50 && linkSpeed>-60)
                {
                    Wifisignalstrength="Good";
                }
                else if(linkSpeed<-60 && linkSpeed>-70)
                {
                    Wifisignalstrength="Fair";
                }
                else
                {
                    Wifisignalstrength="Weak";
                }

//                TelephonyManager telephonyManager =(TelephonyManager)getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
//                CellInfoGsm cellinfogsm = (CellInfoGsm)telephonyManager.getAllCellInfo().get(0);
//                CellSignalStrengthGsm cellSignalStrengthGsm = cellinfogsm.getCellSignalStrength();
//                int mobilesignalstrength=cellSignalStrengthGsm.getDbm();

                if (mViewPager.getCurrentItem()==0)
                {   EditText IssueDescription;
                    String issuedesc;
                    FileName[0] ="Issues_";
                    //issues desc
                    IssueDescription   = (EditText)findViewById(R.id.editTextDescription1);
                    issuedesc=IssueDescription.getText().toString();
                    //Ratings
                    ratingBar1 = (RatingBar) findViewById(R.id.ratingBar1);
                    String ratings=String.valueOf(ratingBar1.getRating());
                    if(CompleteAddress!=null) {
                        //Do Nothing()
                    }else
                    {
                        CompleteAddress="Not Shared"+"_City_"+"Not Shared"+ "_state_" +"Not Shared"+ "_country_" +"Not Shared"+"_postalcode_" +"Not Shared"+ "_knownname_" +"Not Shared";
                    }
                    textfiletosend = generateNoteOnSD(MyActivity.this, FileName[0], "ReportedIssueType_" + Fail_Per_other + "_IssueDescription_" + issuedesc + "_Address_" + CompleteAddress + "_Rating_" + ratings + "_DeviceName_" + deviceName + "_IssueOccursOn_" + IssueOccursOn + "_DeviceDetails_" + devicedetails + "_WIFILinkSpeed_" + linkSpeed + "_WifiSignalStrength_" + Wifisignalstrength + "_ConnectionStatus_" + Connection_Status+"_SelectedImageName_"+SelectedImageName+"_End"); //(context,Name of file,Content of file)
                    new SaveAsyncTask().execute(); //new thread
                }
                else if(mViewPager.getCurrentItem()==1)
                {
                    FileName[0] ="Suggestions_";
                    EditText mEdit;
                    String like="";
                    String dontlike="";
                    String haveidea="";
                    mEdit   = (EditText)findViewById(R.id.txtLike);
                    like="_Liked_"+mEdit.getText().toString();
                    mEdit   = (EditText)findViewById(R.id.txtDontLike);
                    dontlike="_NotLiked_"+mEdit.getText().toString();
                    mEdit   = (EditText)findViewById(R.id.txtHaveidea);
                    haveidea = "_Idea_" + mEdit.getText().toString();
                    ratingBar2 = (RatingBar) findViewById(R.id.ratingBar2);
                    String rating2=String.valueOf(ratingBar2.getRating());
                    textfiletosend =generateNoteOnSD(MyActivity.this, FileName[0],like+dontlike+haveidea+"_DeviceName_"+deviceName+"_DeviceDetails_"+devicedetails+"_WIFILinkSpeed_"+linkSpeed+"_WifiSignalStrength_"+Wifisignalstrength+"_ConnectionStatus_"+Connection_Status+"_Ratings_"+rating2+"_End"); //(context,Name of file,Content of file)
                    new SaveAsyncTask().execute(); //new thread

                }
                else if(mViewPager.getCurrentItem()==2)
                {
                    FileName[0] ="LookandFeel_";
                    String FDetails;
                    EditText featuredetails=(EditText)findViewById(R.id.featureLookandFeel);
                    FDetails=featuredetails.getText().toString();
                    ratingBar3 = (RatingBar) findViewById(R.id.ratingBar3);
                    String rating3=String.valueOf(ratingBar3.getRating());
                    textfiletosend =generateNoteOnSD(MyActivity.this, FileName[0],"LookandFeelIssueType_"+lookandfeel+"_LookandFeelDetails_"+ FDetails+"_DeviceName_"+deviceName+"_DeviceDetails_"+devicedetails+"_WIFILinkSpeed_"+linkSpeed+"_WifiSignalStrength_"+Wifisignalstrength+"_ConnectionStatus_"+Connection_Status+"_Ratings_"+rating3+"_End"); //(context,Name of file,Content of file)
                    new SaveAsyncTask().execute(); //new thread
                }
                else
                {
                    FileName[0] ="Default";
                    textfiletosend =generateNoteOnSD(MyActivity.this, FileName[0],"Nothing"); //(context,Name of file,Content of file)
                    new SaveAsyncTask().execute(); //new thread
                }

            }
            class SaveAsyncTask extends AsyncTask<Object, Object, Void> {
                protected void onPostExecute(Intent i) {
                }
                @Override
                protected Void doInBackground(Object... params) {

                    String devicedetails = Build.MANUFACTURER
                            + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                            + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();
                    ArrayList<Uri> imageUris = new ArrayList<Uri>();//create array list to store URI for image and Report
                    if(selectedImageUri!=null) {
                        imageUris.add(selectedImageUri); // Add your image URIs to array
                    }
                    if(textfiletosend!=null) {
                         imageUris.add(Uri.parse("file://" + textfiletosend.getAbsoluteFile())); //add Report/textfile Uri to array
//                                       Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                                               .setAction("Action", null).show();
                        //imageUris.add(FileProvider.getUriForFile(getApplicationContext(),getApplicationContext().getPackageName() + ".my.package.name.provider", textfiletosend.getAbsoluteFile()));
                        Log.e("ok", textfiletosend.getAbsolutePath());
                    }
                    if(MP4File!=null) {
                    imageUris.add(Uri.parse("file://"+MP4File));
                     Log.e("Link of Mp4",MP4File);
                    }
                    Log.e("DEVICE_DETAILS", devicedetails);
                    Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);//to send multiple attachments
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"feedbacksysstem@gmail.com"});
                    i.putExtra(Intent.EXTRA_CC,new String[]{"feedbacksysstem@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, FileName[0]);
                    i.putExtra(Intent.EXTRA_TEXT, "This Email is to report the feedback."+"Feedback Type reported is"+FileName[0]);
                    //i.setData(Uri.parse("mailto:"));
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_STREAM,imageUris);
                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    //i.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                    //all the Urls in the arraylist are added to the email application(text file and image)
                    //i.putExtra(Intent.EXTRA_STREAM,Uri.parse("file://" + filetosend.getAbsoluteFile()));
                    try {
                            startActivity(Intent.createChooser(i,"Select an Email Application to send your feedback" ));
                    }
                    catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(MyActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                    return null;
                }
            }
            //Gathering values
            });
    }

    private void checkConnection() {

            boolean isConnected = ConnectivityReceiver.isConnected();
            showSnack(isConnected);

    }

    private void showSnack(boolean isConnected) {
        int color;
        if (isConnected) {
            Connection_Status="Connected";
            Toast.makeText(getApplicationContext(), "Connected to Internet", Toast.LENGTH_SHORT).show();

        } else {
            Connection_Status="Disconnected";
            Toast.makeText(getApplicationContext(), "Is Not Connected to Internet", Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener(Internet connection)
        MyApplication.getInstance().setConnectivityListener(this);
    }

    public void haveIdea(View view) {
        EditText haveidea;
        haveidea = (EditText)findViewById(R.id.txtHaveidea);
        haveidea.setVisibility(view.VISIBLE);
    }

    public class GenericFileProvider extends FileProvider {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
    public void showFloatingActionButton() {
        FloatingActionButton fabSend = (FloatingActionButton) findViewById(R.id.floSend);
        fabSend.show();
    };

    public void hideFloatingActionButton() {
        FloatingActionButton fabSend = (FloatingActionButton) findViewById(R.id.floSend);
        fabSend.hide();
    };
    public File generateNoteOnSD(MyActivity context, String sFileName, String sBody) {
        File gpxfile = null;
        try
        {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes"); //save in Notes folder
            if (!root.exists()) {
                root.mkdirs();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_FFF");
            Date now = new Date();
            String yymmdd = formatter.format(now) ;
            gpxfile = new File(root, sFileName+yymmdd+".txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return gpxfile;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) //Activity called after the Oncreate
    {
            new FancyShowCaseQueue()
                    .add(new FancyShowCaseView.Builder(this)
                            .focusOn(findViewById(R.id.radiogrp1))
                            .focusShape(FocusShape.ROUNDED_RECTANGLE)
                            .titleStyle(0, Gravity.BOTTOM | Gravity.CENTER)
                            .title("Select the type of issue faced")
                            .build()
                    )
                    .add(new FancyShowCaseView.Builder(this)
                            .focusOn(findViewById(R.id.tabs))
                            .titleStyle(0, Gravity.BOTTOM | Gravity.CENTER)
                            .title("Select one of these to Report issues ,Share Suggestions or for Interface related query")
                            .build()
                    )
                    .add(new FancyShowCaseView.Builder(this)
                            .focusCircleRadiusFactor(2.0)
                            .focusOn(findViewById(R.id.imageButton6))
                            .title("Choose preferred language here")
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
                SelectedImageName=picturePath.substring(picturePath.lastIndexOf("/")+1);

                }
                break;
             }
            case REQ_CODE_SPEECH_INPUT: {
                //mVoiceInputTv = (EditText)findViewById(R.id.editTextDescription);
                mVoiceInputTv1 = (EditText)findViewById(R.id.editTextDescription1);
                     if (resultCode == RESULT_OK && null != data) {
                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        String text =result.get(0);
                        //mVoiceInputTv.setText(text);
                         mVoiceInputTv1.setText(text);
                    }

                    break;

            }
            case RECORD_REQUEST_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    mediaProjection = projectionManager.getMediaProjection(resultCode, data);
                    recordService.setMediaProject(mediaProjection);
                    recordService.startRecord();
                    VideoStartBtn = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
                    VideoStartBtn.setColorFilter(Color.argb(255, 255, 0, 0));//starts and becomes red to indicate it is running and busy.
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {//Stops and becomes Green again in 30 seconds
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "30 seconds finished.", Toast.LENGTH_SHORT).show();
                            recordService.stopRecord();

                            VideoStartBtn = (FloatingActionButton) findViewById(R.id.floatingActionButton2);
                            VideoStartBtn.setColorFilter(Color.argb(255, 0, 255, 0));//Green one.It is available again.

                        }
                    }, DELAY);
                }
                break;
            }

            //End of case2
        } //End of switch

    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            RecordService.RecordBinder binder = (RecordService.RecordBinder) service;
            recordService = binder.getRecordService();
            recordService.setConfig(metrics.widthPixels, metrics.heightPixels, metrics.densityDpi);
           }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {}
    };

    private String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    //Touch anywhere on Tab_3
    public void tab1myMethod(View pView) {
        if (count == 0)
        {
            new FancyShowCaseQueue()
                    .add(new FancyShowCaseView.Builder(this)
                            .title("Click to send the report")
                            .focusOn(findViewById(R.id.floSend))
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
                .focusOn(findViewById(R.id.floSend))
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
            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    finish();
                    return;
                }
            case AUDIO_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    finish();

                }
                    return;
            case 10:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(MyActivity.this, "Permission denied for location access. Note:Providing access will help in quicker resolution of the issue.", Toast.LENGTH_SHORT).show();
                        //return;
                    }
                    else
                    {
                        Toast.makeText(MyActivity.this, "Permission granted for location access.Please switch on Location", Toast.LENGTH_SHORT).show();
                    // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
                         locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 5000, 0, listener);
                    }
                      return;
                }
            case 1: {
                // If request is cancelled, the result arrays are empty.
                //Request granted , so open the gallary and pick up any picture
                 if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                    //After this it calls onActivityResult() to upload the picture
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                 }
                else
                    {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MyActivity.this, "Permission has been denied.Please accept the Permission to share the picture from your storage.Sharing screenshot increases the resolution time of issue by 20%", Toast.LENGTH_SHORT).show();
                    }
                    return;
            }
            case 11: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    Toast.makeText(MyActivity.this, "Permission denied to read your storage Files.Providing the permission would help in quicker issue resolution", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // permissions this app might request
        }
    }
    public void switchOn(View view) {
        Switch simpleSwitch1;
        simpleSwitch1 = (Switch) findViewById(R.id.sharelocation);
        if (simpleSwitch1.isChecked())
        {
            if (ActivityCompat.checkSelfPermission(MyActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                            , 10);
                }
                return;
            }
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 5000, 0, listener);
        }
            else
        {
            Toast.makeText(MyActivity.this, "Switch is off", Toast.LENGTH_SHORT).show();
        }
    }

    public void AddPic(View view) {
        ActivityCompat.requestPermissions(MyActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

    }
    public void btnhelp(View view) {

        mViewPager = (ViewPager) findViewById(container);
        viewAnimator = (ViewAnimator)findViewById(R.id.viewanimator);
        if (mViewPager.getCurrentItem()==0 ) //display the following when the viewpager is in first Tab.
        {
            if(viewAnimator.getDisplayedChild()==0) {
                new FancyShowCaseQueue()
                        .add(new FancyShowCaseView.Builder(this)
                                .focusOn(findViewById(R.id.radiogrp1))
                                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                                .titleStyle(0, Gravity.BOTTOM | Gravity.CENTER)
                                .title("Select the type of issue faced")
                                .build()
                        )
                        .add(new FancyShowCaseView.Builder(this)
                                .focusOn(findViewById(R.id.tabs))
                                .titleStyle(0, Gravity.BOTTOM | Gravity.CENTER)
                                .title("select one of the tabs to report issues, provide suggestions or for any interface query")
                                .build()
                        )
                        .add(new FancyShowCaseView.Builder(this)
                                .focusCircleRadiusFactor(2.0)
                                .focusOn(findViewById(R.id.imageButton6))
                                .title("Change language here")
                                .build()
                        )
                        .show();
            }
            if(viewAnimator.getDisplayedChild()==1) {
                new FancyShowCaseQueue()
                        .add(new FancyShowCaseView.Builder(this)
                                .focusOn(findViewById(R.id.floatingActionButton))
                                .focusShape(FocusShape.CIRCLE)
                                .titleStyle(0, Gravity.BOTTOM | Gravity.CENTER)
                                .title("Select this to include the Screenshot of the issue & increase the quality of bug report by 10%")
                                .build()
                        )
                        .add(new FancyShowCaseView.Builder(this)
                                .focusOn(findViewById(R.id.floatingActionButton2))
                                .focusShape(FocusShape.CIRCLE)
                                .titleStyle(0, Gravity.BOTTOM | Gravity.CENTER)
                                .title("Tap here to start recording screen and tap again to stop.")
                                .build()
                        )
                        .add(new FancyShowCaseView.Builder(this)
                                //.focusOn(findViewById(R.id.switch1))
                                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                                .focusRectAtPosition(400,1300,1200,100)
                                .titleStyle(0, Gravity.BOTTOM | Gravity.CENTER)
                                .title("Switch on to share your location here(Note:location details may help in faster resolution of the issue)")
                                .build()
                        )
                        .show();

            }
            if(viewAnimator.getDisplayedChild()==2) {
                new FancyShowCaseQueue()
                        .add(new FancyShowCaseView.Builder(this)
                                .focusOn(findViewById(R.id.radiogrp2))
                                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                                .titleStyle(0, Gravity.BOTTOM | Gravity.CENTER)
                                .title("Select where you see the issue")
                                .build()
                        )
                        .add(new FancyShowCaseView.Builder(this)
                                .focusOn(findViewById(R.id.ratingBar))
                                .focusShape(FocusShape.ROUNDED_RECTANGLE)
                                .titleStyle(0, Gravity.BOTTOM | Gravity.CENTER)
                                .title("Provide overall ratings of the App")
                                .build()
                )
                        .show();

            }
        }

        if (mViewPager.getCurrentItem()==1) //display the following when the viewpager is in first Tab.
        {
            new FancyShowCaseQueue()
                    .add(new FancyShowCaseView.Builder(this)
                            .title("Select to describe the feature/design you would like to see in future")
                            .focusOn(findViewById(R.id.cbLike))
                            .focusShape(FocusShape.ROUNDED_RECTANGLE)
                            .titleStyle(0, Gravity.BOTTOM | Gravity.CENTER)
                            .build()
                    )
                    .add(new FancyShowCaseView.Builder(this)
                            .focusOn(findViewById(R.id.txtDontLike))
                            .focusShape(FocusShape.ROUNDED_RECTANGLE)
                            .title("Select to describe the function/design you dont like or want to improve in future")
                            .titleStyle(0, Gravity.BOTTOM | Gravity.CENTER)
                            .build()
                    )
                    .add(new FancyShowCaseView.Builder(this)
                            .focusOn(findViewById(R.id.cbHaveidea))
                            .focusOn(findViewById(R.id.txtHaveidea))
                            .focusShape(FocusShape.ROUNDED_RECTANGLE)
                            .title("select if you have any intersting ideas or suggestions")
                            .titleStyle(0, Gravity.BOTTOM | Gravity.CENTER)
                            .roundRectRadius(100)
                            .build()
                    )
                    .add(new FancyShowCaseView.Builder(this)
                            .focusOn(findViewById(R.id.floSend))
                            .title("Once done,click here to send the feedback")
                            .build()
                    )
                    .show();
        }
        if (mViewPager.getCurrentItem()==2) //display the following when the viewpager is in second Tab.
        {
            new FancyShowCaseQueue()
                    .add(new FancyShowCaseView.Builder(this)
                            .title("Select the type of question")
                            .focusOn(findViewById(R.id.radiogrp3))
                            .focusShape(FocusShape.ROUNDED_RECTANGLE)
                            .titleStyle(0, Gravity.BOTTOM | Gravity.CENTER)
                            .build()
                    )
                    .add(new FancyShowCaseView.Builder(this)
                            .focusOn(findViewById(R.id.featureLookandFeel))
                            .focusShape(FocusShape.ROUNDED_RECTANGLE)
                            .title("Mention your question regarding the app here")
                            .roundRectRadius(100)
                            .titleStyle(0, Gravity.BOTTOM | Gravity.CENTER)
                            .build()

                    )
                    .add(new FancyShowCaseView.Builder(this)
                            .focusOn(findViewById(R.id.floatingActionButton2))
                            .focusShape(FocusShape.CIRCLE)
                            .focusCircleRadiusFactor(2)
                            .title("Tap to record your screen where you are facing the trouble")
                            .titleStyle(0, Gravity.BOTTOM | Gravity.CENTER)
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

    public void switchOn3(View view) { //Switch for location in tab_1
        Switch simpleSwitch1;
        simpleSwitch1 = (Switch) findViewById(R.id.switch1);
        if (simpleSwitch1.isChecked())
        {
            if (ActivityCompat.checkSelfPermission(MyActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            Toast.makeText(MyActivity.this, "Switch is off.Location data wont be shared", Toast.LENGTH_SHORT).show();
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

    public void OnRadioClick_Issue(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.Doesnt_work:
                if (checked)
                    Fail_Per_other ="Failure";
                    // Pirates are the best
                    break;
            case R.id.Too_slow:
                if (checked)
                    Fail_Per_other ="Performance";
                    // Ninjas rule
                    break;
            case R.id.Other_Issues:
                if (checked)
                    Fail_Per_other ="OtherMalfunction";
                    // Ninjas rule
                    break;
        }
    }

    public void recordVideo(View view) {
        if (recordService.isRunning()) { //if already running
            recordService.stopRecord();
            Toast.makeText(getApplicationContext(),"Recording stopped and File is saved", Toast.LENGTH_SHORT).show();
            Uri uri = Uri.parse(MP4File); //Declare your url here.

            VideoView mVideoView  = (VideoView)findViewById(R.id.videoView2);
            mVideoView.setMediaController(new MediaController(this));
            //mVideoView.setLayoutParams(new LinearLayout.LayoutParams(900,500));
            mVideoView.setVideoURI(uri);
            mVideoView.requestFocus();
            //mVideoView.setZOrderOnTop(true);
            mVideoView.start();

            VideoStartBtn.setColorFilter(Color.argb(255, 0, 255, 0)); // Green again Tint

        } else {

            if (ContextCompat.checkSelfPermission(MyActivity.this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.RECORD_AUDIO}, AUDIO_REQUEST_CODE);
            }
            Intent captureIntent = projectionManager.createScreenCaptureIntent();
            startActivityForResult(captureIntent, RECORD_REQUEST_CODE);

        }

    }

    public void IssueOccursOn(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.IssueOntheScreen:
                if (checked)
                    IssueOccursOn="Issue on the Screen";
                // Pirates are the best
                break;
            case R.id.IssueInBackground:
                if (checked)
                    IssueOccursOn="Issue in the Background";
                // Ninjas rule
                break;
        }

    }

    public void click_Rbtnlookandfeel(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.dontUnderstand:
                if (checked)
                    lookandfeel="Unawarness";
                // Pirates are the best
                break;
            case R.id.isConfusing:
                if (checked)
                    lookandfeel="Confusion";
                // Ninjas rule
                break;
            case R.id.isInconvenient:
                if (checked)
                    lookandfeel="Inconvenience";
                // Ninjas rule
                break;

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
             return new Tab_1();
          case 1:
             Tab2 tab2=new Tab2();

                 //On startup request for permissions for reading content on the device.
             ActivityCompat.requestPermissions(MyActivity.this,
                         new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                         11);
             return tab2;
         case 2:
             Tab_3 tab3=new Tab_3();
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
                        return "Suggestions";
                case 2:
                    if (a == "de")
                        return "Fragen";
                    else
                        return "Questions";
            }
            return null;
        }



    }
    public void Likeselected(View view) {
//        EditText txtlike;
//        txtlike = (EditText)view.findViewById(R.id.txtLike);
//        if ( view.is )
//        {
//
//            txtlike.setVisibility(buttonView.VISIBLE);// perform logic
//        }
//        else {
//            txtlike.setVisibility(buttonView.INVISIBLE);
//        }




//        EditText txtlike;
//        EditText txtdontlike;
//        txtlike = (EditText)findViewById(R.id.txtLike);
//        txtdontlike = (EditText)findViewById(R.id.txtDontLike);
//        txtlike.setVisibility(view.VISIBLE);

    }

    public void Dontlikeselected(View view) {
//

    }


}
