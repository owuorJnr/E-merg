package com.e_merg.adapters;

import java.util.List;

import com.e_merg.R;
import com.e_merg.types.Center;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CenterAdapter extends ArrayAdapter<Center> {

	private LayoutInflater layoutInflater;
	Context activity;

	public CenterAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_2);
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		activity = context;
	}// end of constructor

	public void setCenterList(List<Center> data) {
		clear();
		if (data != null) {
			for (Center appEntry : data) {
				add(appEntry);
			}
			notifyDataSetChanged();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.row_center, null);
			holder = new ViewHolder();
			
			holder.name = (TextView) convertView.findViewById(R.id.txtName);
			holder.services = (TextView) convertView.findViewById(R.id.txtServices);
			holder.logo = (ImageView) convertView.findViewById(R.id.imgCenter);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Center center = getItem(position);
		holder.name.setText(center.getName());
		holder.services.setText(center.getServices());
		if(center.getName().equalsIgnoreCase("Strathmore Clinic")){
			holder.logo.setImageDrawable(activity.getResources().getDrawable(R.drawable.strathmore_logo));
		}else if(center.getCategory().equalsIgnoreCase("Ambulance")){
			holder.logo.setImageDrawable(activity.getResources().getDrawable(R.drawable.ambulance));
		}else if(center.getCategory().equalsIgnoreCase("Medical")){
			holder.logo.setImageDrawable(activity.getResources().getDrawable(R.drawable.hospital));
		}else if(center.getCategory().equalsIgnoreCase("Fire Station")){
			holder.logo.setImageDrawable(activity.getResources().getDrawable(R.drawable.fire));
		}else if(center.getCategory().equalsIgnoreCase("Police Station")){
			holder.logo.setImageDrawable(activity.getResources().getDrawable(R.drawable.police));
		}else {
			holder.logo.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_launcher));
		}
		//holder.rTag.setText("");
		
		return convertView;
	}// end of method getView()

	static class ViewHolder {
		TextView name,services;
		ImageView logo;
	}
}//END OF CLASS CenterAdapter
