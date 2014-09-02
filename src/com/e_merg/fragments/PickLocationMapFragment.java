package com.e_merg.fragments;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.e_merg.R;
import com.e_merg.interfaces.OnChangeFragmentListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PickLocationMapFragment extends SupportMapFragment implements OnMapClickListener{

	OnChangeFragmentListener fragmentListener;
    GoogleMap map;
    //Geocoder geocoder;

    LocationManager locationManager;


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
        
        map.setOnMapClickListener(this);

    }

    @Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
            fragmentListener = (OnChangeFragmentListener)activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnChangeFragmentListener");
        }
	}
    
    @Override
	public void onDetach() {
		// TODO Auto-generated method stub
		fragmentListener = null;
		super.onDetach();
	}

	@Override
	public void onMapClick(LatLng coord) {
		// TODO Auto-generated method stub
		AddCenterFragment addCenterFragment = new AddCenterFragment();
		addCenterFragment.setLocation(coord);
		fragmentListener.onChangeFragment(addCenterFragment);
	}
    
    
}//END OF CLASS PickLocationMapFragment
