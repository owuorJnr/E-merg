package com.e_merg.adapters;

import java.util.List;

import com.e_merg.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigationAdapter extends ArrayAdapter<String> {

	private LayoutInflater layoutInflater;

	public NavigationAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_2);
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}// end of constructor

	public void setNavigationItems(List<String> data) {
		clear();
		if (data != null) {
			for (String appEntry : data) {
				add(appEntry);
			}
			notifyDataSetChanged();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			
			if(evenPosition(position)){
				convertView = layoutInflater.inflate(R.layout.row_nav_item, null);
			}else{
				convertView = layoutInflater.inflate(R.layout.row_nav_item_inverse, null);
			}
			
			holder = new ViewHolder();
			
			holder.item = (TextView) convertView.findViewById(R.id.txtNavItem);
			//holder.image = (TextView) convertView.findViewById(R.id.txtDesc);
			convertView.setTag(holder);
			
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String item = getItem(position);
		holder.item.setText(item);
		//holder.image.setText(cond.getDesc());
		
		return convertView;
	}// end of method getView()

	
	private boolean evenPosition(int position){
		if(position%2 != 0){
			return false;
		}else{
			return true;
		}
	}
	
	static class ViewHolder {
		TextView item;
		ImageView image;
	}
}//END OF CLASS NavigationAdapter