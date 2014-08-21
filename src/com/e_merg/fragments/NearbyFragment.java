package com.e_merg.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.e_merg.R;
import com.e_merg.adapters.CenterAdapter;
import com.e_merg.interfaces.OnChangeFragmentListener;
import com.e_merg.types.Center;

public class NearbyFragment extends ListFragment{

	OnChangeFragmentListener fragmentListener;
	ListView listView;
	CenterAdapter centerAdapter;
	List<Center> listCenters;
	
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
		
		listView = getListView();
		centerAdapter = new CenterAdapter(getActivity());
		
		listCenters = new ArrayList<Center>();
		Center center;
		center = new Center("Strathmore Clinic", 0, 0, "0728558822", "0727775712","037129012", "Out-Patient Services");
		listCenters.add(center);
		
		centerAdapter.setCenterList(listCenters);
		listView.setAdapter(centerAdapter);
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
		
	
}//END OF CLASS NearbyFragment
