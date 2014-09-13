package com.e_merg.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.e_merg.interfaces.OnChangeFragmentListener;
import com.e_merg.services.GPSTracker;
import com.e_merg.types.ServiceHandler;
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
    LatLng ME,NEW_CENTER;
    
    private ProgressDialog pDialog;

    //URL to get contacts JSON
    private static String url = "http://www.sharemiale.info.ke/emerg_api/index.php";

    //JSON Node Names
    private static final String TAG_REQ = "search-center";
    private static String TAG_SUCCESS = "success";
    private static String TAG_SUCCESS_MSG = "success_msg";
    private static String TAG_ERROR = "error";
    private static String TAG_ERROR_MSG = "error_msg";


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        map = this.getMap();
        gpsTracker = new GPSTracker(getActivity());
        
        if(gpsTracker.hasInternetConnection()){
        	
			if (gpsTracker.canGetLocation()) {

				ME = new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude());

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
				NEW_CENTER = coord;
				dialog.cancel();
				new SearchCenter().execute();
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
    
	
	private class SearchCenter extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            //Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("req", TAG_REQ));
            nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(NEW_CENTER.latitude)));
            nameValuePairs.add(new BasicNameValuePair("lon", String.valueOf(NEW_CENTER.longitude)));
            
            //Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST,nameValuePairs);

            //shows the response gotten from the http request
            Log.d("Response: "," > "+jsonStr);

            if(jsonStr != null){

                try {
	                JSONObject jsonObj = new JSONObject(jsonStr);
	                String success = jsonObj.getString(TAG_SUCCESS);
	                if(success != null && success.equalsIgnoreCase("1")){   
	                    
	                	String success_msg = jsonObj.getString(TAG_SUCCESS_MSG);
	                    //return success_msg;
	                    return success;
                	}else{
                		String error = jsonObj.getString(TAG_ERROR);
                		if(error != null && error.equalsIgnoreCase("1")){  
                			return jsonObj.getString(TAG_ERROR_MSG);
                		}else{
                			return "";
                		}
                	}
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return "";
                }

            }else{
                return "";
            }

        }


        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            if(pDialog.isShowing()){
                pDialog.dismiss();
            }


            if(!result.equalsIgnoreCase("")){

                //MOVE TO ADD CENTER FRAGMENT
            	if(result.equalsIgnoreCase("1")){
					AddCenterFragment addCenterFragment = new AddCenterFragment();
					addCenterFragment.setLocation(NEW_CENTER);
					fragmentListener.onChangeFragment(addCenterFragment);
            	}else{
            		Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            	}

            }else{
                Toast.makeText(getActivity(), "Error verifying emergency center", Toast.LENGTH_SHORT).show();
            }

        }


    }
    
}//END OF CLASS PickLocationMapFragment
