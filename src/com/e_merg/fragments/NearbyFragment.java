package com.e_merg.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.e_merg.R;
import com.e_merg.adapters.CenterAdapter;
import com.e_merg.interfaces.IMakeCall;
import com.e_merg.interfaces.OnChangeFragmentListener;
import com.e_merg.services.GPSTracker;
import com.e_merg.types.Center;
import com.e_merg.types.ServiceHandler;
import com.google.android.gms.maps.model.LatLng;

public class NearbyFragment extends ListFragment implements OnItemClickListener{

	GPSTracker gpsTracker;
	
	OnChangeFragmentListener fragmentListener;
	IMakeCall iMakeCall;
	ListView listView;
	CenterAdapter centerAdapter;
	List<Center> listCenters;
	
	private static String url = "http://www.sharemiale.info.ke/emerg_api/index.php";
	private ProgressDialog pDialog;
	
	private static final String TAG_REQ = "get-centers";
	private LatLng currentCoordinates;
	private int radius;
	
    private static String TAG_SUCCESS = "success";
    private static String TAG_SUCCESS_MSG = "success_msg";
    private static String TAG_ERROR = "error";
    private static String TAG_ERROR_MSG = "error_msg";
    private static String TAG_CENTERS = "centers";
    private static String TAG_NAME = "name";
    private static String TAG_CATEGORY = "cat";
    private static String TAG_LAT = "lat";
    private static String TAG_LON = "lon";
    private static String TAG_CONTACTS = "contacts";
    private static String TAG_PHONE1 = "phone1";
    private static String TAG_PHONE2 = "phone2";
    private static String TAG_PHONE3 = "phone3";
    private static String TAG_EMAIL = "email";
    private static String TAG_SERVICES = "services";
    
    JSONArray centers = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_nearby, container,false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		radius = 50;
		listView = getListView();
		listCenters = new ArrayList<Center>();
			
		gpsTracker = new GPSTracker(getActivity());
        
		 if(gpsTracker.hasInternetConnection()){
	        	
			if (gpsTracker.canGetLocation()) {

				currentCoordinates = new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude());
				new GetCenterList().execute();
			} else {
				// can't get location
				// GPS or Network is not enabled
				// Ask user to enable GPS/network in settings
				// currentCoordinates = new LatLng(0,0);
				gpsTracker.showSettingsAlert();
			}
	        		
		} else {
			Toast.makeText(getActivity(), "No Internet Connection!",
					Toast.LENGTH_SHORT).show();
		}
		
	
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
            fragmentListener = (OnChangeFragmentListener)activity;
            iMakeCall = (IMakeCall)activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnChangeFragmentListener and IMakeCall");
        }
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		fragmentListener = null;
		iMakeCall = null;
		super.onDetach();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		// TODO Auto-generated method stub
		//call emergency number
		Center center = (Center) listView.getItemAtPosition(position);
		iMakeCall.makeCall(center.getPhone1());
		
		final String title = center.getName();
    	AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
		alertBuilder.setTitle("Call Emergency");
		alertBuilder.setMessage("Call "+title);
		alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				for(int i=0;i<listCenters.size();i++){
					Center center = listCenters.get(i);
					if(title.equalsIgnoreCase(center.getName())){
						//call number
						iMakeCall.makeCall(center.getPhone1());
					}
				}
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
	
	private class GetCenterList extends AsyncTask<String, String, String> {

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
            nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(currentCoordinates.latitude)));
            nameValuePairs.add(new BasicNameValuePair("lon", String.valueOf(currentCoordinates.longitude)));
            nameValuePairs.add(new BasicNameValuePair("radius", String.valueOf(radius)));
            
           // Log.d("Resquest: "," > "+);
            
            //Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST,nameValuePairs);

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
	
	                        String name = s.getString(TAG_NAME);
	                        String category = s.getString(TAG_CATEGORY);
	                        String lat = s.getString(TAG_LAT);
	                        String lon = s.getString(TAG_LON);
	                        
	                        JSONArray contacts = s.getJSONArray(TAG_CONTACTS);
	                        JSONObject contact = contacts.getJSONObject(0);
	                        
	                        String phone1 = contact.getString(TAG_PHONE1);
	                        String phone2 = contact.getString(TAG_PHONE2);
	                        String phone3 = contact.getString(TAG_PHONE3);
	                        
	                        String email = s.getString(TAG_EMAIL);
	                        String services = s.getString(TAG_SERVICES);
	                        
	                        
	                		center = new Center(name, Double.parseDouble(lat), Double.parseDouble(lon), phone1,phone2,phone3,services,category,email);
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
        		centerAdapter = new CenterAdapter(getActivity());
        		centerAdapter.setCenterList(listCenters);
        		listView.setAdapter(centerAdapter);
            	
            }else{
                Toast.makeText(getActivity(), "Error fetching emergency center(s)", Toast.LENGTH_SHORT).show();
            }

        }


    }
		
	
}//END OF CLASS NearbyFragment
