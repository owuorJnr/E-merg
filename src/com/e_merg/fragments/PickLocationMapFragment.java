package com.e_merg.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.e_merg.interfaces.OnChangeFragmentListener;
import com.e_merg.services.GPSTracker;
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
    GPSTracker gpsTracker;

    LocationManager locationManager;


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        map = this.getMap();
        gpsTracker = new GPSTracker(getActivity());
        
        if(gpsTracker.hasInternetConnection()){
        	
			if (gpsTracker.canGetLocation()) {

				LatLng ME = new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude());

				map.addMarker(new MarkerOptions()
						.position(ME)
						.title("Me")
						.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

				// Move the camera instantly to current location with a zoom of
				// 1000.
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(ME, 1000));

				// Zoom in, animating the camera.
				map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
				map.setOnMapClickListener(this);

			} else {
				// can't get location
				// GPS or Network is not enabled
				// Ask user to enable GPS/network in settings
				gpsTracker.showSettingsAlert();
			}
        }else{
        	Toast.makeText(getActivity(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }

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
	public void onMapClick(final LatLng coord) {
		// TODO Auto-generated method stub
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
		alertBuilder.setTitle("Pick This Location");
		alertBuilder.setMessage("You are sure you want to pick this location?");
		alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				AddCenterFragment addCenterFragment = new AddCenterFragment();
				addCenterFragment.setLocation(coord);
				dialog.cancel();
				fragmentListener.onChangeFragment(addCenterFragment);
			}
			
		});
		
		alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
			
		});
		alertBuilder.create().show();
	}
    
    
}//END OF CLASS PickLocationMapFragment
