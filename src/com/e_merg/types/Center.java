package com.e_merg.types;

public class Center {
	
	 private String name;
     private double lat;
     private double lon;
     
     private String phone1;
     private String phone2;
     private String phone3;
     private String category;
     private String services;
     
     private String email;
     

     public Center(String name,double lat,double lon,String phone1,String phone2,String phone3,String services,String cat,String email){
         this.name = name;
         this.lat = lat;
         this.lon = lon;
         this.phone1 = phone1;
         this.phone2 = phone2;
         this.phone3 = phone3;
         this.services = services;
         this.category = cat;
         this.email = email;
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
     
     public String getPhone1(){
         return phone1;
     }
     
     public String getPhone2(){
         return phone2;
     }
     
     public String getPhone3(){
         return phone3;
     }
     
     public String getServices(){
         return services;
     }
     
     public String getCategory(){
         return category;
     }
     
     public String getEmail(){
         return email;
     }
	
}
