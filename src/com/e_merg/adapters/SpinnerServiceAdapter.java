package com.e_merg.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.e_merg.R;
import com.e_merg.types.ServiceItem;

public class SpinnerServiceAdapter extends ArrayAdapter<ServiceItem> {

	LayoutInflater inflater;
	String header,subHeader;

	public SpinnerServiceAdapter(Context context, String header,String subheader) {
		super(context, android.R.layout.simple_list_item_2);
		this.header = header;
		this.subHeader = subheader;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setListData(ServiceItem[] data) {
		clear();
		if (data != null) {
			for (ServiceItem appEntry : data) {
				add(appEntry);
			}
			notifyDataSetChanged();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.spinner_row_title, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.txtSpTitle);
			holder.item = (TextView) convertView.findViewById(R.id.txtSpSubTitle);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.title.setText(header);
		holder.item.setText(subHeader);
		//CharSequence cs = getItem(position);
		//holder.item.setText(cs);

		return convertView;
	}// end of method getView()

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.spinner_row_service, null);
			holder = new ViewHolder();
			holder.item = (TextView) convertView.findViewById(R.id.txtSpItem);
			holder.cbxItem = (CheckBox) convertView.findViewById(R.id.cbxSpItem);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final ServiceItem serviceItem = getItem(position);
		holder.item.setText(serviceItem.getService());
		holder.cbxItem.setChecked(serviceItem.isSelected());
		
		holder.cbxItem.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				serviceItem.setSelected(holder.cbxItem.isChecked());
			}
		});

		return convertView;
	}

	static class ViewHolder {
		TextView item, title;
		CheckBox cbxItem;
	}
}