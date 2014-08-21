package com.e_merg.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.e_merg.R;
import com.e_merg.interfaces.OnChangeFragmentListener;

public class AddServicesFragment extends Fragment implements OnClickListener{

	OnChangeFragmentListener fragmentListener;
	
	EditText editOther;
	Spinner spServices;
	Button btnSkip,btnFinish;
	
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
			fragmentListener.onChangeFragment(new NearbyFragment());
		}
	}
		
	
}//END OF CLASS AddServicesFragment