package com.e_merg.types;

public class ServiceItem {

	private String service;
	private boolean selected;
	
	public ServiceItem(){
		this.service = "";
		this.selected = false;
	}
	
	public ServiceItem(String service,boolean selected){
		this.service = service;
		this.selected = selected;
	}
	
	public void setService(String service){
		this.service = service;
	}
	
	public void setSelected(boolean selected){
		this.selected = selected;
	}
	
	public String getService(){
		return this.service;
	}
	
	public boolean isSelected(){
		return this.selected;
	}
	
}
