package com.apps.googlemaps.activities;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.googlemaps.R;
import com.apps.googlemaps.adapters.MarkerInfoAdapter;
import com.apps.googlemaps.adapters.PlacesAutoCompleteAdapter;
import com.apps.googlemaps.models.MapLocation;
import com.apps.googlemaps.services.MyLocationReceiver;
import com.apps.googlemaps.services.MyNetworkReceiver;
import com.apps.googlemaps.utils.ConstantKey;
import com.apps.googlemaps.utils.GpsUtility;
import com.apps.googlemaps.utils.Network;
import com.apps.googlemaps.utils.Utility;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback, PlacesAutoCompleteAdapter.CallBackListener {

    public static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 99;
    private int REQUEST_CHECK_SETTINGS = 0;

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient mFusedLocationClient;

    private MyNetworkReceiver mNetworkReceiver;
    private MyLocationReceiver mLocationReceiver;
    private LocationManager manager;

    private PlacesAutoCompleteAdapter mAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNetworkReceiver = new MyNetworkReceiver(this);
        mLocationReceiver = new MyLocationReceiver(this);

        //-----------------------------------------------| GPS/Location
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        checkGpsEnabled(manager);

        //====================================================| To Display Navigation Bar Icon and Back
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false); //Remove title
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            //toolbar.getBackground().setAlpha(200);
        }

        //====================================| Google Maps
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        //ImageButton searchButton = (ImageButton) findViewById(R.id.search_button);
        recyclerView = (RecyclerView) findViewById(R.id.places_recycler_view);
        initRecyclerView(recyclerView, ConstantKey.getLocations());

        ((EditText) findViewById(R.id.place_search)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    if (mAdapter != null) {
                        mAdapter.getFilter().filter(s.toString());
                    }
                    if (recyclerView.getVisibility() == View.GONE) {recyclerView.setVisibility(View.VISIBLE);}
                } else {
                    if (recyclerView.getVisibility() == View.VISIBLE) {recyclerView.setVisibility(View.GONE);}
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    //===============================================| onStart(), onPause(), onResume(), onStop()
    @Override
    protected void onResume() {
        super.onResume();
        try {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)); //After Oreo version this code must be used
            registerReceiver(mLocationReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));  //After Oreo version this code must be used
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(mNetworkReceiver); //After Oreo version this code must be used
            unregisterReceiver(mLocationReceiver); //After Oreo version this code must be used
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.standard:
                Utility.mapsStyle(HomeActivity.this, mMap, R.raw.style_standard);
                break;
            case R.id.silver:
                Utility.mapsStyle(HomeActivity.this, mMap, R.raw.style_silver);
                break;
            case R.id.retro:
                Utility.mapsStyle(HomeActivity.this, mMap, R.raw.style_retro);
                break;
            case R.id.dark:
                Utility.mapsStyle(HomeActivity.this, mMap, R.raw.style_dark);
                break;
            case R.id.night:
                Utility.mapsStyle(HomeActivity.this, mMap, R.raw.style_night);
                break;
            case R.id.aubergine:
                Utility.mapsStyle(HomeActivity.this, mMap, R.raw.style_aubergine);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //===============================================| onMapReady()
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        //-------------------------------------------| Pass a JSON style object to your map
        Utility.mapsStyle(HomeActivity.this, mMap, R.raw.style_silver);
        //-------------------------------------------| If above the android version Marshmallow then call the location permission
        requestPermissions();

        /*mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);*/

        ((ToggleButton) findViewById(R.id.traffic_toggle_button)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (mMap != null)
                        mMap.setTrafficEnabled(true);
                } else {
                    if (mMap != null)
                        mMap.setTrafficEnabled(false);
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (MapLocation obj : ConstantKey.getLocations()) {
                    LatLng latLng = new LatLng(obj.getLatitude(), obj.getLongitude());
                    String[] arr = {};
                    //mMap.addMarker(new MarkerOptions().position(latLng).title(obj.getAddress()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))); //Default Icon
                    //mMap.addMarker(new MarkerOptions().position(latLng).title(obj.getAddress())).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.admin)); //PNG Icon
                    //mMap.addMarker(new MarkerOptions().position(latLng).title(obj.getAddress()).icon(Utility.getMarkerIconFromDrawable( getApplicationContext().getResources().getDrawable(R.drawable.ic_icon_house) ))); //XML Icon

                    //multiple markers with multiple information show on markers for android google maps
                    Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(obj.getAddress()).snippet(getResources().getString(R.string.app_name)).icon(Utility.getMarkerIconFromDrawable( getApplicationContext().getResources().getDrawable(R.drawable.ic_icon_house) ))); //XML Icon
                    //marker.showInfoWindow();

                    //---------------------------------------------| Add titles for showing offline google maps
                    MarkerInfoAdapter mAdapter = new MarkerInfoAdapter(HomeActivity.this, arr, obj);
                    mMap.setInfoWindowAdapter(mAdapter);
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            Toast.makeText(HomeActivity.this, ""+obj.getAddress(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }, 3000);

        //---------------------------------------------| MarkerDrag Listener for Origin
        /*Utility.markerDragListener(new Utility.MarkerDragCallback() {
            @Override
            public void onCallback(LatLng latLng) {
                if (latLng != null) {
                    origin = new LatLng(latLng.latitude, latLng.longitude);
                    currentLocationMarker = Utility.addMarkerPosition(mMap, currentLocationMarker, latLng.latitude, latLng.longitude,Utility.getAddress(HomeActivity.this, latLng), getResources().getDrawable(R.drawable.ic_marker_origin));
                }
            }
        }, HomeActivity.this, mMap);*/
        //---------------------------------------------| CameraMove Listener for Destination
        /*if (isSetLocationOnMap) {
            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    LatLng latLng = mMap.getCameraPosition().target; //float mZoom = mMap.getCameraPosition().zoom; //double lat =  mMap.getCameraPosition().target.latitude; //double lng =  mMap.getCameraPosition().target.longitude;
                    destination = new LatLng(latLng.latitude, latLng.longitude);
                    if (isSetLocationOnMap) {searchEditText.setText(Utility.getAddress(HomeActivity.this, destination));}
                    if (searchButton.getVisibility() == View.GONE) {
                        searchButton.setVisibility(View.VISIBLE);
                    }
                }
            });
            *//*mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    LatLng latLng = mMap.getCameraPosition().target; //float mZoom = mMap.getCameraPosition().zoom; //double lat =  mMap.getCameraPosition().target.latitude; //double lng =  mMap.getCameraPosition().target.longitude;
                    destination = new LatLng(latLng.latitude, latLng.longitude);
                    if (isSetLocationOnMap) {searchEditText.setText(Utility.getAddress(HomeActivity.this, destination));}
                }
            });*//*
            mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                @Override
                public void onCameraMoveStarted(int reason) {
                    if (reason ==REASON_GESTURE) {
                        if (searchButton.getVisibility() == View.VISIBLE) {
                            searchButton.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }*/
    }

    //====================================| Explain why the app needs the request permissions
    //https://developers.google.com/maps/documentation/android-sdk/location
    private void requestPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION); //if there is no permission allowed then, display permission request dialog
        } else {
            mMap.setMyLocationEnabled(true);
            Utility.changeCurrentLocationIcon(mapFragment);
            getDeviceLocation();
        }
    }

    //====================================| Handle the permissions request response
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_ACCESS_FINE_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //For allow button
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    Utility.changeCurrentLocationIcon(mapFragment);
                    getDeviceLocation();
                }
            } else {
                //For denied button
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                requestPermissions();
            }
        }
    }

    //===============================================| Get Device Location/LatLng
    private void getDeviceLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng origin = new LatLng(location.getLatitude(),location.getLongitude());
                        Utility.moveToLocation(mMap, new LatLng(origin.latitude, origin.longitude));
                        if (Network.haveNetwork(HomeActivity.this)) {
                            //String mAddress = Utility.getAddress(HomeActivity.this, origin);
                            //SharedPrefManager.getInstance(HomeActivity.this).saveCurrentLatLng(origin);
                        }
                    }
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    //===============================================| GPS/Location
    private void checkGpsEnabled(LocationManager manager) {
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && GpsUtility.hasGPSDevice(this)) {
            GpsUtility.displayLocationSettingsRequest(new GpsUtility.GpsOnListenerCallBack() {
                @Override
                public void gpsResultCode(int resultCode) {
                    REQUEST_CHECK_SETTINGS = resultCode;
                }
            }, this);
        } else {
            //checkPermissions();
        }
    }

    //===============================================| Search Place
    private void initRecyclerView(RecyclerView recyclerView, ArrayList<MapLocation> arrayList) {
        mAdapter = new PlacesAutoCompleteAdapter(HomeActivity.this, arrayList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        //mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(5)); //px spacing
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onRecyclerViewItems(MapLocation place) {
        if (recyclerView.getVisibility() == View.VISIBLE) {
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
        }
        Utility.moveToLocation(mMap, new LatLng(place.getLatitude(), place.getLongitude()));
    }
}