package com.e_merg.fragments;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.e_merg.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PickLocationMapFragment extends MapFragment{

    GoogleMap map;
    //Geocoder geocoder;

    LocationManager locationManager;

    //URL to get contacts JSON
    //private static String url = "http://10.0.2.2/mti_shopping/malls.php";
    private static String url = "http://www.sharemiale.info.ke/emerg_api/index.php";


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        map = this.getMap();
   		map.setMyLocationEnabled(true);
        //geocoder = new Geocoder(getActivity());

        Location location = map.getMyLocation();

        map.addMarker(new MarkerOptions()
                .position(new LatLng(location.getLatitude(),location.getLongitude()))
                .title("Me")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));


        // Move the camera instantly to sbs with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 15));

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

    }


}//END OF CLASS PickLocationMapFragment
