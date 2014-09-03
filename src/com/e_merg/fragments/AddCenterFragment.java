package com.e_merg.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.e_merg.R;
import com.e_merg.activities.MainActivity;
import com.e_merg.adapters.SpinnerCategoryAdapter;
import com.e_merg.interfaces.OnChangeFragmentListener;
import com.e_merg.types.ServiceHandler;
import com.google.android.gms.maps.model.LatLng;

public class AddCenterFragment extends Fragment implements OnClickListener{
//Will trigger AddServices
	OnChangeFragmentListener fragmentListener;
	
	LinearLayout llFirst,llSecond,llThird;
	EditText editPhone1,editPhone2,editPhone3,editEmail,editName;
	Spinner spCategory;
	ImageView imgAdd1,imgAdd2,imgDel2,imgDel3;
	Button btnNext;
	
	SpinnerCategoryAdapter spinnerAdapter;
	
	private static String url = "http://www.sharemiale.info.ke/emerg_api/index.php";
	private ProgressDialog pDialog;
	
	private static final String TAG_REQ = "add-center";
	private String name,email,category,phone1,phone2,phone3;
	private Double lat,lon;
	

    private static String TAG_SUCCESS = "success";
    private static String TAG_SUCCESS_MSG = "success_msg";
    private static String TAG_ERROR = "error";
    private static String TAG_ERROR_MSG = "error_msg";
    private static String TAG_CENTER_NO = "center_no";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_add_center, container,false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		if(savedInstanceState != null){
			lat = savedInstanceState.getDouble("lat");
			lon = savedInstanceState.getDouble("lon");
		}else{
			name=email=category=phone1=phone2=phone3="";
		}
		
		llFirst = (LinearLayout)getView().findViewById(R.id.llFirst);
		llSecond = (LinearLayout)getView().findViewById(R.id.llSecond);
		llThird = (LinearLayout)getView().findViewById(R.id.llThird);
		editPhone1 = (EditText)getView().findViewById(R.id.editPhone1);
		editPhone2 = (EditText)getView().findViewById(R.id.editPhone2);
		editPhone3 = (EditText)getView().findViewById(R.id.editPhone3);
		editEmail = (EditText)getView().findViewById(R.id.editEmail);
		editName = (EditText)getView().findViewById(R.id.editName);
		spCategory = (Spinner)getView().findViewById(R.id.spCategory);
		imgAdd1 = (ImageView)getView().findViewById(R.id.imgAdd1);
		imgAdd2 = (ImageView)getView().findViewById(R.id.imgAdd2);
		imgDel2 = (ImageView)getView().findViewById(R.id.imgDelete2);
		imgDel3 = (ImageView)getView().findViewById(R.id.imgDelete3);
		btnNext = (Button)getView().findViewById(R.id.btnNext);
		
		spinnerAdapter = new SpinnerCategoryAdapter(getActivity(), getResources().getString(R.string.spCategoryHeader));
		
		String[] rawCategories = getResources().getStringArray(R.array.array_category);
		spinnerAdapter.setListData(rawCategories);
		spCategory.setAdapter(spinnerAdapter);
		
		imgAdd1.setOnClickListener(this);
		imgAdd2.setOnClickListener(this);
		imgDel2.setOnClickListener(this);
		imgDel3.setOnClickListener(this);
		btnNext.setOnClickListener(this);
	}

	public void setLocation(LatLng coord){
		lat = coord.latitude;
		lon = coord.longitude;
	}
	
	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putDouble("lat", lat);
		outState.putDouble("lon", lon);
		super.onSaveInstanceState(outState);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == imgAdd1){
			if(!llSecond.isShown()){
				llSecond.setVisibility(View.VISIBLE);
			}
			
		}else if(v == imgAdd2){
			if(!llThird.isShown()){
				llThird.setVisibility(View.VISIBLE);
			}
			
		}else if(v == imgDel2){
			if(llSecond.isShown()){
				llSecond.setVisibility(View.GONE);
			}
			
		}else if(v == imgDel3){
			if(llThird.isShown()){
				llThird.setVisibility(View.GONE);
			}
			
		}else if(v == btnNext){
			//pick details and send online
			name = editName.getText().toString().trim();
			email = editEmail.getText().toString().trim();
			category = (String) spCategory.getSelectedItem().toString();
			phone1 = editPhone1.getText().toString().trim();
			phone2 = editPhone2.getText().toString().trim();
			phone3 = editPhone3.getText().toString().trim();
			
			if(name.equalsIgnoreCase("") || phone1.equalsIgnoreCase("")){
				
				Toast.makeText(getActivity(), "Enter name, category, email and atleast phone1", Toast.LENGTH_SHORT).show();
				
			}else{
				new AddCenterContact().execute();
			}
			
			//fragmentListener.onChangeFragment(new AddServicesFragment());
		}
	}
	
	
	private class AddCenterContact extends AsyncTask<String, String, String> {

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
            nameValuePairs.add(new BasicNameValuePair("name", name));
            nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(lat)));
            nameValuePairs.add(new BasicNameValuePair("lon", String.valueOf(lon)));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("cat", category));
            nameValuePairs.add(new BasicNameValuePair("phone1", phone1));
            nameValuePairs.add(new BasicNameValuePair("phone2", phone2));
            nameValuePairs.add(new BasicNameValuePair("phone3", phone3));
            
            //Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST,nameValuePairs);

            //shows the response gotten from the http request
            Log.d("Response: "," > "+jsonStr);

            if(jsonStr != null){

                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
	                String success = jsonObj.getString(TAG_SUCCESS);
	                if(success != null && success.equalsIgnoreCase("1")){ 
	                    /*
	                    centers = jsonObj.getJSONArray(TAG_CENTERS);
	
	                    //looping through all items
	                    for(int i=0;i<centers.length();i++){
	                        JSONObject s = centers.getJSONObject(i);
	
	                        //String id = s.getString(TAG_ID);
	                        String name = s.getString(TAG_NAME);
	                        String lat = s.getString(TAG_LAT);
	                        String lon = s.getString(TAG_LON);
	
	                    }*/
	                    MainActivity.currentCenterNo = jsonObj.getString(TAG_CENTER_NO);
	                    
	
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


            if(!result.equalsIgnoreCase("") && result.equalsIgnoreCase("Center added")){
            	Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            	fragmentListener.onChangeFragment(new AddServicesFragment());
            	
            }else{
                Toast.makeText(getActivity(), "Center Not Added", Toast.LENGTH_SHORT).show();
            }

        }


    }
	
}//END OF CLASS AddCenterFragment