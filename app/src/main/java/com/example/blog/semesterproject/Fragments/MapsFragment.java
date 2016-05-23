package com.example.blog.semesterproject.Fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.blog.semesterproject.Entities.BlogPost;
import com.example.blog.semesterproject.R;
import com.example.blog.semesterproject.Utils.AjaxHelper;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    public ArrayList<BlogPost> allPostsList = AllPostsFragment.postlist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        super.onCreate(savedInstanceState);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        return view;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Looper igennem blogposts, og sætter en marker hvis der er registreret lat og lng.
        if (allPostsList.size() > 0) {
            for (int i = 0; i < allPostsList.size(); i++) {
                if (allPostsList.get(i).getLongitude() != null) {
                    LatLng postedFrom = new LatLng(Double.parseDouble(allPostsList.get(i).getLatitude()), Double.parseDouble(allPostsList.get(i).getLongitude()));
                    mMap.addMarker(new MarkerOptions().position(postedFrom).title(allPostsList.get(i).getTitle() + " by " + allPostsList.get(i).getAuthor()));
                }
                //Centrer på København og zoom!
                LatLng copenhagen = new LatLng(55.6761, 12.5683);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(copenhagen));
                CameraUpdate zoom=CameraUpdateFactory.zoomTo(10);
                mMap.animateCamera(zoom);
            }
        }

    }
}
