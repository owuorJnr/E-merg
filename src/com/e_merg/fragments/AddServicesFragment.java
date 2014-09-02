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
import android.widget.Spinner;
import android.widget.Toast;

import com.e_merg.R;
import com.e_merg.activities.MainActivity;
import com.e_merg.interfaces.OnChangeFragmentListener;
import com.e_merg.types.ServiceHandler;

public class AddServicesFragment extends Fragment implements OnClickListener{

	OnChangeFragmentListener fragmentListener;
	
	EditText editOther;
	Spinner spServices;
	Button btnSkip,btnFinish;
	
	private static String url = "http://www.sharemiale.info.ke/emerg_api/index.php";
	private ProgressDialog pDialog;
	

    private static String TAG_SUCCESS = "success";
    private static String TAG_SUCCESS_MSG = "success_msg";
    private static String TAG_ERROR = "error";
    private static String TAG_ERROR_MSG = "error_msg";
	private static final String TAG_REQ = "add-service";
	
	private String services;//,other;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		return inflater.inflate(R.layout.fragment_add_services, container,false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		editOther = (EditText)getView().findViewById(R.id.editOtherService);
		spServices = (Spinner)getView().findViewById(R.id.spServices);
		btnSkip = (Button)getView().findViewById(R.id.btnSkip);
		btnFinish = (Button)getView().findViewById(R.id.btnFinish);
		
		btnSkip.setOnClickListener(this);
		btnFinish.setOnClickListener(this);
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
		if(v == btnSkip){
			
			fragmentListener.onChangeFragment(new NearbyFragment());
			
		}else if(v == btnFinish){
			//pick services and send online
			String services = (String) spServices.getSelectedItem();
			services = services.concat(","+editOther.getText().toString().trim());
			
			if(services.equalsIgnoreCase(",")){
				new AddCenterService().execute();
			}else{
				Toast.makeText(getActivity(), "Enter Service", Toast.LENGTH_SHORT).show();
			}
			//fragmentListener.onChangeFragment(new NearbyFragment());
		}
	}
		
	
	private class AddCenterService extends AsyncTask<String, String, String> {

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
            nameValuePairs.add(new BasicNameValuePair("center-no", MainActivity.currentCenterNo));
            nameValuePairs.add(new BasicNameValuePair("services", services));
            
            //Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.POST,nameValuePairs);

            //shows the response gotten from the http request
            Log.d("Response: "," > "+jsonStr);

            if(jsonStr != null){

                try {
                JSONObject jsonObj = new JSONObject(jsonStr);
	            String success = jsonObj.getString(TAG_SUCCESS);
	            if(success != null && success.equalsIgnoreCase("1")){ 

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
            	fragmentListener.onChangeFragment(new NearbyFragment());

            }else{
                Toast.makeText(getActivity(), "Service Not Added", Toast.LENGTH_SHORT).show();
            }

        }


    }
	
}//END OF CLASS AddServicesFragment