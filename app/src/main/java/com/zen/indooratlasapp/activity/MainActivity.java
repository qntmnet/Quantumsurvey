package com.zen.indooratlasapp.activity;


import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IARegion;
import com.indooratlas.android.sdk.resources.IAFloorPlan;
import com.indooratlas.android.sdk.resources.IALatLng;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import com.zen.indooratlasapp.R;
import com.zen.indooratlasapp.database.Database;

import java.util.ArrayList;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String TAG = MainActivity.class.getSimpleName();

    /* used to decide when bitmap should be downscaled */
    private static final int MAX_DIMENSION = 1048;
    TextView txt, txt1, txt2, txt3, txt4;
    Button btn;

    private final Database database = new Database();

    private GoogleMap mMap;
    private Marker mHeadingMarker;
    private Circle mCircle;
    private IALocation mIaLocation;
    private IARegion iaRegion;

    private IALocationManager mIALocationManager;
    private final IALocationRequest iaLocationRequest = IALocationRequest.create();


    private final IALocationListener mIALocationListener = new IALocationListener() {
        // Called when the location has changed.
        @SuppressWarnings("CommentedOutCode")
        @SuppressLint("SetTextI18n")
        @Override
        public void onLocationChanged(@NonNull IALocation location) {
            //Update the user's location on the map as their location changes.
            txt.setText("FloorLevel: " + location.getFloorLevel());
            txt1.setText("Latitude: " + location.getLatitude());
            txt2.setText("Longitude: " + location.getLongitude());
            txt3.setText("Accuracy: " + location.getAccuracy());
            //Log.e(TAG, "getAccuracy: " + location.getAccuracy());
            //Log.e(TAG, " location.toLocation();: " + location.toLocation());
            //int mFloor = location.getFloorLevel();
            final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            showLocationCircle(latLng, location.getAccuracy());
            mIaLocation = location;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.e(TAG, "onStatusChanged: " + i);
            //Log.e(TAG, "onStatusChanged_bundle: " + bundle);
            switch (i) {
                case IALocationManager.STATUS_AVAILABLE:
                    Log.d(TAG, "onStatusChanged: Available");
                    txt4.setText("Available");
                    break;
                case IALocationManager.STATUS_LIMITED:
                    Log.d(TAG, "onStatusChanged: Limited");
                    txt4.setText("Limited");
                    break;
                case IALocationManager.STATUS_OUT_OF_SERVICE:
                    Log.d(TAG, "onStatusChanged: Out of service");
                    txt4.setText("Out of service");
                    break;
                case IALocationManager.STATUS_TEMPORARILY_UNAVAILABLE:
                    Log.d(TAG, "onStatusChanged: Temporarily unavailable");
                    txt4.setText("Temporarily unavailable");
                    break;
            }
        }
    };


    /**
     * Fetching Floor Plan Images
     */
    private final IARegion.Listener mRegionListener = new IARegion.Listener() {
        @SuppressWarnings("CommentedOutCode")
        @Override
        public void onEnterRegion(@NonNull final IARegion region) {
            Log.e(TAG, "region " + region);
            if (region.getType() == IARegion.TYPE_FLOOR_PLAN) {
                Log.e(TAG, "onEnterRegion_floor plan ID: =  " + region.getId());
                Log.e(TAG, "onEnterRegion_Entered: " + region.getName());
                fetchFloorPlanBitmap(region.getFloorPlan());
                //setupPoIs(mVenue.getPOIs(), region.getFloorPlan().getFloorLevel());

            } else if (region.getType() == IARegion.TYPE_VENUE) {
                // triggered when near a new location
                // Log.e(TAG, "onEnterRegion_Location changed : " + region.getVenue());
                //IAVenue mVenue = region.getVenue();
                iaRegion = region;
                database.addLocation(MainActivity.this, region);
                //String locationId = region.getId();
                //String locationName = region.getName();
            }
        }

        @Override
        public void onExitRegion(@NonNull IARegion iaRegion) {
            // leaving a previously entered region
            if (iaRegion.getType() == IARegion.TYPE_FLOOR_PLAN) {
                // notice that a change of floor plan (e.g., floor change)
                // is signaled by an exit-enter pair so ending up here
                // does not yet mean that the device is outside any mapped area
                Log.e(TAG, "onExitRegion_Location changed to " + iaRegion.getVenue());
            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //creating the IALocationManager
        mIALocationManager = IALocationManager.create(this);
        String[] neededPermissions = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.BLUETOOTH_SCAN};
        ActivityCompat.requestPermissions(this, neededPermissions, 0);
        setContentView(R.layout.activity_main);
        txt = findViewById(R.id.txt);
        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        txt3 = findViewById(R.id.txt3);
        txt4 = findViewById(R.id.txt4);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(v -> {
            if (mIaLocation != null) {
                Log.e(TAG, "mIaLocation =" + mIaLocation);
                final LatLng latLng = new LatLng(mIaLocation.getLatitude(), mIaLocation.getLongitude());
                addDot(latLng);
                database.addSurveyReading(this, mIaLocation, iaRegion);
                ArrayList<IALocation> list = database.getAllReading(this);
                Log.e(TAG, "list = " + list);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // start receiving location updates & monitor region changes
        mIALocationManager.requestLocationUpdates(iaLocationRequest, mIALocationListener);
        mIALocationManager.registerRegionListener(mRegionListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIALocationManager.removeLocationUpdates(mIALocationListener);
        mIALocationManager.unregisterRegionListener(mRegionListener);
    }

    @Override
    protected void onDestroy() {
        mIALocationManager.destroy();
        super.onDestroy();
    }


    @SuppressWarnings({"CommentedOutCode", "Convert2Lambda", "unused"})
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // do not show Google's outdoor location
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        MapStyleOptions style1 = new MapStyleOptions("[" +
                "  {" +
                "    \"featureType\":\"all\"," +
                "    \"elementType\":\"labels\"," +
                "    \"stylers\":[" +
                "      {" +
                "        \"visibility\":\"off\"" +
                "      }" +
                "    ]" +
                "  }" +
                "]");

        mMap.setMapStyle(style1);
        mMap.setMyLocationEnabled(false);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
               /* geta();
                if (mPoIMarkers.isEmpty()) {
                    // if PoIs exist, only allow way finding to PoI markers
                    setWayFindingTarget(latLng, true);
                }*/
            }
        });
        // disable various Google maps UI elements that do not work indoors
        mMap.getUiSettings().setMapToolbarEnabled(false);
    }


    /**
     * Download floor plan using Picasso library.
     */
    private void fetchFloorPlanBitmap(final IAFloorPlan floorPlan) {
        if (floorPlan == null) {
            Log.e(TAG, " floor plan null in fetchFloorPlanBitmap");
            return;
        }
        final String url = floorPlan.getUrl();
        Log.e(TAG, "loading floor plan bitmap from " + url);

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(@NonNull Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.e(TAG, "onBitmap loaded with dimensions: " + bitmap.getWidth() + "x" + bitmap.getHeight());
                setupGroundOverlay(floorPlan, bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        RequestCreator request = Picasso.get().load(url);
        final int bitmapWidth = floorPlan.getBitmapWidth();
        final int bitmapHeight = floorPlan.getBitmapHeight();
        if (bitmapHeight > MAX_DIMENSION) {
            request.resize(0, MAX_DIMENSION);
        } else if (bitmapWidth > MAX_DIMENSION) {
            request.resize(MAX_DIMENSION, 0);
        }
        request.into(target);
    }

    /**
     * Sets bitmap of floor plan as ground overlay on Google Maps
     */
    private void setupGroundOverlay(IAFloorPlan floorPlan, Bitmap bitmap) {
        if (mMap != null) {
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
            IALatLng iaLatLng = floorPlan.getCenter();
            LatLng latLng = new LatLng(iaLatLng.latitude, iaLatLng.longitude);
            GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions()
                    .image(bitmapDescriptor)
                    .zIndex(0.0f)
                    .position(latLng, floorPlan.getWidthMeters(), floorPlan.getHeightMeters())
                    .bearing(floorPlan.getBearing());
            mMap.addGroundOverlay(groundOverlayOptions);
        }
    }


    private void showLocationCircle(LatLng latLng, double accuracyRadius) {
        if (mCircle == null) {
            // location can received before map is initialized, ignoring those updates
            if (mMap != null) {
                mCircle = mMap.addCircle(
                        new CircleOptions()
                                .center(latLng)
                                //.radius(accuracyRadius)
                                .fillColor(0x201681FB)
                                .strokeColor(0x500A78DD)
                                //.zIndex(0.1f)
                                // .visible(true));
                                .strokeWidth(5.0f)
                );

                if (mHeadingMarker == null) {
                    // first location, add marker
                    mHeadingMarker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_green_dot))
                            .anchor(0.5f, 0.5f)
                            .flat(true));
                } else {
                    // move existing markers position to received location
                    //mHeadingMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_green_dot));
                    mHeadingMarker.setPosition(latLng);
                }

                // our camera position needs updating if location has significantly changed
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.5f));
            }
        } else {
            // move existing markers position to received location
            mCircle.setCenter(latLng);
            mHeadingMarker.setPosition(latLng);
            mCircle.setRadius(accuracyRadius);
        }
    }

    private void addDot(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        mMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_blue_dot)));
    }

    /**
     * Fetches floor plan data from IndoorAtlas server.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        initializeData();
    }

    private void initializeData() {
        // prevent the screen going to sleep while app is on foreground
        findViewById(android.R.id.content).setKeepScreenOn(true);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
    }

}


