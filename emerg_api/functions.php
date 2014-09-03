<?php
/**
*All Honor and Glory belongs to God Almighty
*@since 31st Jul, 2014
*@version 1.0
*@author Dickson Owuor
*/

	class FUNCTIONS
	{

		private $response;
		private $db;


		public function __construct($req)
		{

			//response array
			$this->response = array("request" => $req, "success" => 0, "error" => 0);

			//call function to connect to the database
			require_once("database/db_connect.php");
			
			$this->db = new DB_CONNECT();
			$this->db->connect();
		}
		
		public function __destruct()
		{
			//call function to disconnect from database
			$this->db->disconnect();
		
		}
		
	//===============================
	
		function test($test)
		{
			
			$this->response["success"] = 1;
			$this->response["success_msg"] = "You Entered ".$test;


			return json_encode($this->response);
		}//end of function test

		function add_center($name,$category,$lat,$lon,$email,$phone1,$phone2,$phone3)
		{

			if($name == "" || $lat=="" || $lon=="" || $phone1==""){

				$this->response["error"] = 1;
				$this->response["error_msg"] = "Some values empty!";

			}else{

				$alreadyAdded = $this->check_location($lat,$lon);

				if($alreadyAdded == FALSE){
					//generate center_no
					$center_no = uniqid('cntr.');
					$query = "INSERT INTO tbl_emerg_centers(CENTER_NO,NAME,CATEGORY,LOC_LAT,LOC_LON,EMAIL,CREATED_AT) VALUES('$center_no','$name','$category','$lat','$lon','$email',NOW())";
					$result = mysql_query($query) or die(mysql_error());
					if(mysql_affected_rows() > 0){
						$verified = "NO";
						$this->add_contact($center_no,$phone1,$verified);
						$this->add_contact($center_no,$phone2,$verified);
						$this->add_contact($center_no,$phone3,$verified);

						$this->response["success"] = 1;
						$this->response["success_msg"] = "Center added";
						$this->response["center_no"] = $center_no;
					}else{

						$this->response["error"] = 1;
						$this->response["error_msg"] = "Center not added!";
					}

				}else{

					$this->response["error"] = 1;
					$this->response["error_msg"] = "Center already added!";
				}
			}
			

			return json_encode($this->response);
		}//end of function


		function add_services($center_no,$service)
		{
			
			if($center_no=="" || $service==""){

				$this->response["error"] = 1;
				$this->response["error_msg"] = "Some values empty!";

			}else{

				$query = "SELECT * FROM tbl_center_services WHERE CENTER_NO = '$center_no' AND SERVICE='$service'";
				$result = mysql_query($query) or die(mysql_error());

				if(mysql_affected_rows() > 0){

					$this->response["error"] = 1;
					$this->response["error_msg"] = "Services already added!";
					
				}else{

					$query = "INSERT INTO tbl_center_services(CENTER_NO,SERVICE) VALUES('$center_no','$service')";
					$result = mysql_query($query) or die(mysql_error());
					if(mysql_affected_rows() > 0){

						$this->response["success"] = 1;
						$this->response["success_msg"] = "Services added";
					}else{

						$this->response["error"] = 1;
						$this->response["error_msg"] = "Services not added!";
					}
				}
			}



			return json_encode($this->response);
		}//end of function



		function get_centers($lat,$lon,$radius)
		{
			
			//$radius = 0.2;
			$query = "SELECT *, ( 6371 * acos( cos( radians({$lat}) ) * cos( radians( `LOC_LAT` ) ) * cos( radians( `LOC_LON` ) - radians({$lon}) ) + sin( radians({$lat}) ) * sin( radians( `LOC_LAT` ) ) ) ) AS distance FROM `tbl_emerg_centers` HAVING distance <= {$radius} ORDER BY distance ASC";

			/*$query = "SELECT *, ( 6371 * acos( cos( radians('$lat') ) * cos( radians( `LOC_LAT` ) ) * cos( radians( `LOC_LAT` ) - radians('$lon') ) + sin( radians('$lat') ) * sin( radians( `LOC_LAT` ) ) ) ) AS distance FROM `tbl_emerg_centers` HAVING distance <= '$radius' ORDER BY distance ASC";*/


			//$query = "SELECT * FROM tbl_emerg_centers";
			$result = mysql_query($query) or die(mysql_error());
			if(mysql_affected_rows() > 0){

				$this->response["success"] = 1;
				$this->response["success_msg"] = "Nearby centers retrieved";

				$this->response["centers"] = array();

    			while ($row = mysql_fetch_assoc($result)) {
			        $itemArray = array();

			        $center_no = $row['CENTER_NO'];
			        $itemArray["name"] = $row['NAME'];
			        $itemArray["cat"] = $row['CATEGORY'];
			        $itemArray["lat"] = $row['LOC_LAT'];
			        $itemArray["lon"] = $row['LOC_LON'];
			        $itemArray["email"] = $row['EMAIL'];
			        $itemArray["contacts"] = $this->get_center_contacts($center_no);
			        //$itemArray["phone2"] = $row['NAME'];
			        //$itemArray["phone3"] = $row['NAME'];
			        $itemArray["services"] = $this->get_center_services($center_no);
			        array_push($this->response["centers"], $itemArray );

			    }

			}else{

				$this->response["error"] = 1;
				$this->response["error_msg"] = "No centers found!";
			}

			return json_encode($this->response);
		}//end of function

//============================================================================================

		private function check_location($lat,$lon)
		{
			$radius = 1;
			$query = "SELECT *, ( 6371 * acos( cos( radians({$lat}) ) * cos( radians( `LOC_LAT` ) ) * cos( radians( `LOC_LON` ) - radians({$lon}) ) + sin( radians({$lat}) ) * sin( radians( `LOC_LAT` ) ) ) ) AS distance FROM `tbl_emerg_centers` HAVING distance <= $radius ORDER BY distance ASC";

			//$query = "SELECT * FROM tbl_emerg_centers WHERE LOC_LAT = '$lat' AND LOC_LON='$lon'";
			$result = mysql_query($query) or die(mysql_error());
			if(mysql_num_rows($result) > 0){
				
				return TRUE;
			}else{
				
				return FALSE;
			}
		}//end of function

		private function add_contact($center_no,$contact,$verified)
		{
			if($contact != ""){

				$query = "SELECT * FROM tbl_center_contacts WHERE CENTER_NO = '$center_no' AND CONTACT='$contact'";
				$result = mysql_query($query) or die(mysql_error());
				if(mysql_affected_rows() > 0){
					return TRUE;

				}else{
					
					$query = "INSERT INTO tbl_center_contacts(CENTER_NO,CONTACT,VERIFIED) VALUES('$center_no','$contact','$verified')";
					$result = mysql_query($query) or die(mysql_error());
					if(mysql_affected_rows() > 0){

						return TRUE;
					}else{

						return FALSE;
					}

				}
			}else{
				return FALSE;
			}
			
		}//end of function

		private function get_center_contacts($center_no){

			$verified = "NO";

			$query = "SELECT * FROM tbl_center_contacts WHERE CENTER_NO = '$center_no' AND VERIFIED='$verified'";
			$result = mysql_query($query) or die(mysql_error());
			if(mysql_affected_rows() > 0){
				$i=1;
				$contacts = array();
				while($row = mysql_fetch_assoc($result)){

					$contact = array('phone'.$i => $row['CONTACT']);
					$i = $i +1;
					array_push($contacts, $contact);
				}
				return $contacts;
			}else{
				return "";
			}
		}//end of function

		private function get_center_services($center_no){

			$query = "SELECT * FROM tbl_center_services WHERE CENTER_NO = '$center_no'";
			$result = mysql_query($query) or die(mysql_error());
			if(mysql_affected_rows() > 0){
				if($row = mysql_fetch_assoc($result)){

					return $row['SERVICE'];
				}else{
					return "";
				}
			}else{
				return "";
			}
		}//end of function

	}

?>