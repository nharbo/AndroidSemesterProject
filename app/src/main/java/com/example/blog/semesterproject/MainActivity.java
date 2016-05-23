package com.example.blog.semesterproject;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.blog.semesterproject.Fragments.AllPostsFragment;
import com.example.blog.semesterproject.Fragments.ComposeFragment;
import com.example.blog.semesterproject.Fragments.MapsFragment;
import com.example.blog.semesterproject.Fragments.MyPostsFragment;
import com.example.blog.semesterproject.Fragments.SinglePostFragment;
import com.example.blog.semesterproject.Utils.AjaxHelper;
import com.example.blog.semesterproject.Utils.AuthHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static FragmentManager fragmentManager;

    //Google maps
    public GoogleApiClient mGoogleApiClient;
    public Location mLastLocation;
    private static final int MY_LOCATION_REQUEST = 1;
    public static double currentLatitude;
    public static double currentLongitude;
    public static String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Viser allposts som startside.
        displayView(R.id.nav_allposts);

        //Henter alle posts ind fra backenden, og efterfølgende viser dem i "frontpage".
        AjaxHelper ajax = new AjaxHelper(this);
        ajax.getAllPosts();

        AuthHelper auth = new AuthHelper(this);
        username = auth.getUsernameFromExternalStorage();

        //initialising the object of the FragmentManager. Here I'm passing getSupportFragmentManager(). You can pass getFragmentManager() if you are coding for Android 3.0 or above.
        fragmentManager = getSupportFragmentManager();

        //Start Google Maps
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    protected void onStart() {
        //Connect to Google Maps
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    //Spørger brugeren om lov til at bruge hans/hendes location
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.w("INSIDE", " onRequestPermission");
        switch (requestCode) {
            case MY_LOCATION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.w("Permission for location", " granted!");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getLocation();
                } else {
                    Log.w("Permission not granted", "disabling maps");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            deleteFile("username");
            Log.w("USERNAME", "deleted from int storage!");
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displayView(item.getItemId());
        return true;
    }

    //Denne metode styrer hvilket view som skal vises i content_main (FrameLayout)
    public void displayView(int viewId) {

        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (viewId) {
            case R.id.nav_compose:
                fragment = new ComposeFragment();
                title = getString(R.string.title_compose);
                break;

            case R.id.nav_allposts:
                fragment = new AllPostsFragment();
                title = getString(R.string.title_all_posts);
                break;

            case R.id.nav_myposts:
                fragment = new MyPostsFragment();
                title = getString(R.string.title__my_posts);
                //Dette er for at få den rigtige brugers posts loaded.
                AjaxHelper ah = new AjaxHelper(this);
                Log.w("USERNAME FOR MYPOSTS", username);
                ah.getMyPosts(username);
                break;

            case R.id.nav_maps:
                fragment = new MapsFragment();
                title = getString(R.string.title_maps);
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_content, fragment);
            ft.commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }


    //Google maps autoimplemented methods
    @Override
    public void onConnected(Bundle bundle) {
        //We are now connected
        Log.w("GoogleMaps", " Connected!");

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.w("Permission for maps", "Not granted - requesting permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_LOCATION_REQUEST);
            return;
        } else {
            Log.w("Permission for maps", " Already granted!");
            getLocation();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Not connected anymore
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //We tried to connect, but failed.
    }

    public void getLocation() {
        Log.w("INSIDE", " getLocation!");
        //Nedenstående tjek SKAL være der, før metoden virker. Også selvom vi tjekker længere oppe :-) (i onConnected)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            Log.w("GETTING", " Location!");
            Log.w("Current latitude", " " + mLastLocation.getLatitude());
            Log.w("Current longitude", " " + mLastLocation.getLongitude());
            currentLatitude = mLastLocation.getLatitude();
            currentLongitude = mLastLocation.getLongitude();
        }
    }
}
