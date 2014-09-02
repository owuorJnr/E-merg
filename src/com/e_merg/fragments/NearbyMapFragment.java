package com.e_merg.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.e_merg.R;
import com.e_merg.interfaces.OnChangeFragmentListener;
import com.e_merg.types.Center;
import com.e_merg.types.ServiceHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class NearbyMapFragment extends SupportMapFragment{

	OnChangeFragmentListener fragmentListener;
    GoogleMap map;
    //Geocoder geocoder;

    LocationManager locationManager;

    private ProgressDialog pDialog;

    //URL to get contacts JSON
    private static String url = "http://www.sharemiale.info.ke/emerg_api/index.php";

    //JSON Node Names
    private static String TAG_SUCCESS = "success";
    private static String TAG_SUCCESS_MSG = "success_msg";
    private static String TAG_ERROR = "error";
    private static String TAG_ERROR_MSG = "error_msg";
    private static String TAG_CENTERS = "centers";
    private static String TAG_NAME = "name";
    private static String TAG_LAT = "lat";
    private static String TAG_LON = "lon";
    
    private static LatLng ME;

    List<Center> listCenters;
    JSONArray centers = null;


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        map = getMap();
   		map.setMyLocationEnabled(true);
        //geocoder = new Geocoder(getActivity());

   		listCenters = new ArrayList<Center>();

        Location location = map.getMyLocation();
        
        if(location == null){
        	ME = new LatLng(-1.308987, 36.812712);
        }else{
        	ME = new LatLng(location.getLatitude(),location.getLongitude());
        }

        new GetCenterLocations().execute();

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



    private class GetCenterLocations extends AsyncTask<String, String, String> {

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

            //Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST);

            //shows the response gotten from the http request
            Log.d("Response: "," > "+jsonStr);

            if(jsonStr != null){

                try {
	                JSONObject jsonObj = new JSONObject(jsonStr);
	                String success = jsonObj.getString(TAG_SUCCESS);
	                if(success != null && success.equalsIgnoreCase("1")){   
	                    centers = jsonObj.getJSONArray(TAG_CENTERS);
	            		Center center;
	            		
	                    //looping through all items
	                    for(int i=0;i<centers.length();i++){
	                        JSONObject s = centers.getJSONObject(i);
	
	                        //String id = s.getString(TAG_ID);
	                        String name = s.getString(TAG_NAME);
	                        String lat = s.getString(TAG_LAT);
	                        String lon = s.getString(TAG_LON);
	                        String phone1 = "";//s.getString(TAG_NAME);
	                        String phone2 = "";//s.getString(TAG_LAT);
	                        String phone3 = "";//s.getString(TAG_LON);
	                        String detail = "";//s.getString(TAG_LAT);
	                        /*String email = "";//s.getString(TAG_NAME);
	                        String category = "";//s.getString(TAG_LON);
	                        */
	                        
	                        listCenters = new ArrayList<Center>();
	                		center = new Center(name, Double.parseDouble(lat), Double.parseDouble(lon), phone1,phone2,phone3,detail);
	                		listCenters.add(center);
	
	                    }
	
	                    return jsonObj.getString(TAG_SUCCESS_MSG);
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

                //add markers
                for(int i=0;i<listCenters.size();i++){
                    Center center= listCenters.get(i);
                    map.addMarker(new MarkerOptions().position(new LatLng(center.getLat(),center.getLon()))
                            .title(center.getName()));
                }

                map.addMarker(new MarkerOptions()
                .position(ME)
                .title("Me")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));


                // Move the camera instantly to sbs with a zoom of 15.
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(ME, 15));

        		// Zoom in, animating the camera.
                map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

            }else{
                Toast.makeText(getActivity(), "No emergency center(s) Found", Toast.LENGTH_SHORT).show();
            }

        }


    }

}//END OF CLASS NearbyMapFragment
