package net.simplifiedlearning.firebaseauth;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.simplifiedlearning.firebaseauth.Modle.SearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;




public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    //widgets
    private EditText mSearchText;
    private ImageView mGps;
    private SeekBar seekBar;
    private TextView radiusView;

    FirebaseAuth mAuth;
    String phone;
    String sellerName;




    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // Latitude&Longitude Location
    public static  double _locLatitude;
    public static  double _locLongitude;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);
        mSearchText = (EditText) findViewById(R.id.input_search);
        mGps = (ImageView) findViewById(R.id.ic_gps);


        mAuth = FirebaseAuth.getInstance();


        getLocationPermission();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        //Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        //Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        //  if (mLocationPermissionsGranted) { //problem with mLocationPermissionsGranted
        getDeviceLocation();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false); // Disable focus button
       init();



     //geting information from search activity

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            ArrayList<SearchResult> value = (ArrayList<SearchResult>)getIntent().getSerializableExtra("key");
            //The key argument here must match that used in the other activity
            geoLocateItem(value);


        }

    }



   private void init(){
        Log.d(TAG, "init: initializing");

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    geoLocate();

                }

                return false;
            }
        });

       mGps.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Log.d(TAG, "onClick: clicked gps icon");
               getDeviceLocation();
           }
       });
       hideSoftKeyboard();

   }

//adress finder
    private void geoLocate(){

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d(TAG, "GeoLocate: found a location: " + address.toString());
              Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

               moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                   address.getAddressLine(0),"","");

        }
    }

    int progress=0;


    //display items on map marks
    private void geoLocateItem(final ArrayList<SearchResult> adressItem){

        final Geocoder geocoder = new Geocoder(MapActivity.this);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        seekBar.setProgress(progress);
        radiusView=(TextView) findViewById(R.id.raduis_view);
        radiusView.setText(progress+"m");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override

            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {



                CircleOptions circleOptions = new CircleOptions();

                progress=i*1000;
                if(i!=6)
                    radiusView.setText(progress+" Meter");

                else { radiusView.setText("Unlimited distance");
                }
                mMap.clear();
                // Specifying the center of the circle
                circleOptions.center(new LatLng(_locLatitude, _locLongitude));

                // Radius of the circle
                if(i!=6)
                    circleOptions.radius(progress);
                else  mMap.clear();


                // Border color of the circle
                circleOptions.strokeColor(Color.GREEN);

                // Fill color of the circle
                circleOptions.fillColor(0x3053f673 );


                // Border width of the circle
                circleOptions.strokeWidth(2);

                // Adding the circle to the GoogleMap
                mMap.addCircle(circleOptions);

                //print items on the map that in th radius!!
                for(int j=0; j < adressItem.size(); j++){
                    List<Address> list = new ArrayList<>();
                    try{
                        list = geocoder.getFromLocationName(adressItem.get(j).getAddress(), 1);
                    }catch (IOException e){
                        Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
                    }

                    if(list.size() > 0) {

                         Address address = list.get(0);
                         phone=adressItem.get(j).getPhone();
                         sellerName=adressItem.get(j).getSellerName();



                        double x_=address.getLatitude()-_locLatitude;
                        double y_=address.getLongitude()-_locLongitude;
                        double Dhypot = Math.hypot(x_,y_)*100000;

                        if(Dhypot<=progress || i==6){
                            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                                    address.getAddressLine(0),sellerName,phone);}
                    }
                }




            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }





   private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

       mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            final Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,"My Location","","");


                            _locLatitude=currentLocation.getLatitude();
                            _locLongitude=currentLocation.getLongitude();


                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    //final mark detail on map
    private void moveCamera(LatLng latLng, float zoom,String title,String displayName,String phoneNumber){

//        FirebaseUser user =mAuth.getCurrentUser();


        //device location
        if(title.equals("My Location"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions()
                    .position(latLng) //position
                    .title(displayName) // seller name
                    .snippet(phoneNumber); // seller phone
            mMap.addMarker(options);
        }

       hideSoftKeyboard();
    }



    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }
    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}
