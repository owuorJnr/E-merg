package com.e_merg.fragments;

import com.e_merg.R;
import com.e_merg.interfaces.OnChangeFragmentListener;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AddCenterFragment extends Fragment implements OnClickListener{
//Will trigger AddServices
	OnChangeFragmentListener fragmentListener;
	
	LinearLayout llFirst,llSecond,llThird;
	EditText editPhone1,editPhone2,editPhone3,editEmail,editName;
	ImageView imgAdd1,imgAdd2,imgDel2,imgDel3;
	Button btnNext;
	
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
		
		llFirst = (LinearLayout)getView().findViewById(R.id.llFirst);
		llSecond = (LinearLayout)getView().findViewById(R.id.llSecond);
		llThird = (LinearLayout)getView().findViewById(R.id.llThird);
		editPhone1 = (EditText)getView().findViewById(R.id.editPhone1);
		editPhone2 = (EditText)getView().findViewById(R.id.editPhone2);
		editPhone3 = (EditText)getView().findViewById(R.id.editPhone3);
		editEmail = (EditText)getView().findViewById(R.id.editEmail);
		editName = (EditText)getView().findViewById(R.id.editName);
		imgAdd1 = (ImageView)getView().findViewById(R.id.imgAdd1);
		imgAdd2 = (ImageView)getView().findViewById(R.id.imgAdd2);
		imgDel2 = (ImageView)getView().findViewById(R.id.imgDelete2);
		imgDel3 = (ImageView)getView().findViewById(R.id.imgDelete3);
		btnNext = (Button)getView().findViewById(R.id.btnNext);
		
		imgAdd1.setOnClickListener(this);
		imgAdd2.setOnClickListener(this);
		imgDel2.setOnClickListener(this);
		imgDel3.setOnClickListener(this);
		btnNext.setOnClickListener(this);
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
			fragmentListener.onChangeFragment(new AddServicesFragment());
		}
	}
		
	
}//END OF CLASS AddCenterFragment