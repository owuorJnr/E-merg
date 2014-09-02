package com.e_merg.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.e_merg.R;
import com.e_merg.adapters.CenterAdapter;
import com.e_merg.interfaces.OnChangeFragmentListener;
import com.e_merg.types.Center;
import com.e_merg.types.ServiceHandler;
import com.google.android.gms.maps.model.LatLng;

public class NearbyFragment extends ListFragment{

	OnChangeFragmentListener fragmentListener;
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
    private static String TAG_LAT = "lat";
    private static String TAG_LON = "lon";
    
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
		
		radius = 1;
		
		listView = getListView();
		
		new GetCenterList().execute();
		
		
		/*listCenters = new ArrayList<Center>();
		Center center;
		center = new Center("Strathmore Clinic", 0, 0, "0728558822", "0727775712","037129012", "Out-Patient Services");
		listCenters.add(center);*/
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
            nameValuePairs.add(new BasicNameValuePair("lon", String.valueOf(currentCoordinates.latitude)));
            nameValuePairs.add(new BasicNameValuePair("radius", String.valueOf(radius)));
            
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
        		centerAdapter = new CenterAdapter(getActivity());
        		centerAdapter.setCenterList(listCenters);
        		listView.setAdapter(centerAdapter);
            	
            }else{
                Toast.makeText(getActivity(), "No emergency center(s) Found", Toast.LENGTH_SHORT).show();
            }

        }


    }
		
	
}//END OF CLASS NearbyFragment
