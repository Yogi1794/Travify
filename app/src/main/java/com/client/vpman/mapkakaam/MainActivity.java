package com.client.vpman.mapkakaam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.client.vpman.mapkakaam.Adapter.NearPlaceAdapter;
import com.client.vpman.mapkakaam.Model.ModelData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener, AppBarLayout.OnOffsetChangedListener {


    boolean gps_enabled = false;
    boolean network_enabled = false;

    String JsonUrl;
    private boolean isHideToolbarView = false;
    private static final int PERMISSION_REQUEST_CODE = 200;
    ImageView imageView;

    NearPlaceAdapter adapter;

    MaterialTextView cityName,cityName1;

    List<Address> addresses;
    NavigationView navigationView;
    Double rating;
    String photo_reference;
    String height;
    String width;
    RecyclerView recyclerView;
    String vicinity;

    private String NewApiKey="AIzaSyBKP76r3bGVpkf_-4-KHayb3sZVpadqMoA";
    private String OldApiKey="AIzaSyB7tnG5pbO6Lh0M7TqoYIcTzRYYGR9SUhk";

    private DrawerLayout mDrawereLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;

    private FusedLocationProviderClient fusedLocationClient;

    Double lat,lon;
    String countryCode=null;

    List<ModelData> list;
    String cityname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView=findViewById(R.id.imageView);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mDrawereLayout = findViewById(R.id.Main);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(" ");
        recyclerView=findViewById(R.id.recylerView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        appBarLayout=findViewById(R.id.appBar);
        appBarLayout.addOnOffsetChangedListener(this);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawereLayout, R.string.open, R.string.close);
        mDrawereLayout.addDrawerListener(drawerToggle);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");


        list=new ArrayList<>();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mDrawereLayout.openDrawer(GravityCompat.START);
                mDrawereLayout.openDrawer(GravityCompat.START);
            }
        });

        cityName=findViewById(R.id.PlaceName);
        cityName1=findViewById(R.id.PlaceName1);

        Volley();



    }

    public void Volley()
    {
       /* list=new ArrayList<>();
        adapter=new NearPlaceAdapter(MainActivity.this,list);
        RecyclerView.LayoutManager mLayoutmanager1;
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(true);
        mLayoutmanager1=new GridLayoutManager(MainActivity.this,2);
        recyclerView.setLayoutManager(mLayoutmanager1);
        recyclerView.setAdapter(adapter);*/
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(MainActivity.this, location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null)
                    {
                        // Logic to handle location object
                        Geocoder gcd = new Geocoder(MainActivity.this.getBaseContext(), Locale.getDefault());
                        try {
                            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                            if (addresses.size() > 0) {
                                cityname = addresses.get(0).getLocality();
                                lat = addresses.get(0).getLatitude();
                                lon = addresses.get(0).getLongitude();
                                countryCode = addresses.get(0).getCountryCode();
                                Log.d("wkegfiewgj", String.valueOf(lat));
                                Log.d("ewdwfew", String.valueOf(lon));
                                LocationRequest request = new LocationRequest();
                                request.setInterval(10 * 60 * 1000);
                                request.setMaxWaitTime(60 * 60 * 1000);
                                cityName.setText(cityname);
                                cityName1.setText(cityname);
                                JsonUrl="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lon+"&radius=10000&types=Monuments&keyword=historic&key=AIzaSyBKP76r3bGVpkf_-4-KHayb3sZVpadqMoA";
                                StringRequest stringRequest=new StringRequest(Request.Method.GET, JsonUrl, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Log.d("qewf",response);

                                        try {
                                            JSONObject jsonObject=new JSONObject(response);
                                            JSONArray jsonArray=jsonObject.getJSONArray("results");
                                            for (int i=0;i<jsonArray.length(); i++)
                                            {
                                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                                rating=jsonObject1.getDouble("rating");
                                                vicinity=jsonObject1.getString("vicinity");
                                                Log.d("rating", String.valueOf(rating));
                                                Log.d("vicinity",vicinity);
                                                JSONArray jsonArray1=jsonObject1.getJSONArray("photos");
                                                for (int j=0;j<jsonArray1.length();j++)
                                                {
                                                    JSONObject jsonObject2=jsonArray1.getJSONObject(j);
                                                    photo_reference=jsonObject2.getString("photo_reference");
                                                    Log.d("uopgkgiy",photo_reference);
                                                    height=jsonObject2.getString("height");
                                                     width=jsonObject2.getString("width");



                                                    String photoUrl="https://maps.googleapis.com/maps/api/place/photo?maxwidth="+width+"&maxheight="+height+"&photoreference="+photo_reference+"&key=AIzaSyBKP76r3bGVpkf_-4-KHayb3sZVpadqMoA";
                                                    Log.d("ewopiry",photoUrl);
                                                    Glide.with(MainActivity.this).load(photoUrl).into(imageView);

                                                }
                                                Log.d("listData", String.valueOf(list.size()));
                                                list.add(new ModelData(vicinity,height,width,photo_reference,rating));

                                                adapter=new NearPlaceAdapter(MainActivity.this,list);
                                                RecyclerView.LayoutManager mLayoutmanager1;
                                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                                recyclerView.setNestedScrollingEnabled(true);
                                                mLayoutmanager1=new GridLayoutManager(MainActivity.this,2);
                                                recyclerView.setLayoutManager(mLayoutmanager1);
                                                recyclerView.setAdapter(adapter);



                                            }
/*
                                            Random random=new Random();
                                            int n = random.nextInt(list.size());*/


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        NetworkResponse response = error.networkResponse;
                                        if (error instanceof ServerError && response != null) {
                                            try {
                                                String res = new String(response.data,
                                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                                // Now you can use any deserializer to make sense of data
                                                JSONObject obj = new JSONObject(res);
                                            } catch (UnsupportedEncodingException e1) {
                                                // Couldn't properly decode data to string
                                                e1.printStackTrace();
                                            } catch (JSONException e2) {
                                                // returned data is not JSONObject?
                                                e2.printStackTrace();
                                            }
                                        }

                                    }
                                });


                                stringRequest.setShouldCache(false);

                                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                requestQueue.add(stringRequest);
                            }}
                        catch (Exception e){}
                    }});
                    Log.d("eflhwoe", String.valueOf(lat));
                    Log.d("eflhwoe", String.valueOf(lon));


    }










    public void checkGpsStatus()
    {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("GPS is not enabled")
                    .setPositiveButton("Open Location Setting", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            MainActivity.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                        }
                    })
                    .setNegativeButton("Cancel",null)
                    .show();

        }

        if (checkLocationON())
        {
           // findWeather();
        }


    }
    public boolean checkLocationON()
    {
        LocationManager locationManager = (LocationManager) Objects.requireNonNull(MainActivity.this.getSystemService(LOCATION_SERVICE));



        try {
            if (locationManager != null) {
                gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }
        } catch(Exception ex) {}

        try {
            if (locationManager != null) {
                network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            return false;
        }
        else
        {
            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        Log.d("hgfkj", "onRequestPermissionsResult:989 ");
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkLocationON())
                    {
                       // findWeather();
                    }
                    else {
                        checkGpsStatus();
                    }


                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i)
    {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(i) / (float) maxScroll;
        if (percentage==1f && isHideToolbarView)
        {
            imageView.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            cityName.setVisibility(View.VISIBLE);
            cityName1.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;

        }
        else if (percentage < 1f && !isHideToolbarView)
        {
            imageView.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            cityName.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;

        }


    }
}