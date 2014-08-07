package com.e_merg.types;

public class Center {
	
	 private String name;
     private double lat;
     private double lon;

     public Center(String name,double lat,double lon){
         this.name = name;
         this.lat = lat;
         this.lon = lon;
     }

     public String getName(){
         return name;
     }

     public double getLat(){
         return lat;
     }

     public double getLon(){
         return lon;
     }
	
}
