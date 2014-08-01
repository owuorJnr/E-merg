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

		function add_center()
		{
			$alreadyAdded = $this->check_location($lat,$lon);

			if($alreadyAdded === FALSE){
				$query = "INSERT INTO tbl_emerg_centers(CENTER_NO,NAME,LOC_LAT,LOC_LON,EMAIL) VALUES()";
				$result = mysql_query($query) or die(mysql_error());
				if(mysql_affected_rows() > 0){

					$this->add_contact($center_no,$phone1,$verified);
					$this->add_contact($center_no,$phone2,$verified);
					$this->add_contact($center_no,$phone3,$verified);

					$this->response["success"] = 1;
					$this->response["success_msg"] = "Center added";
				}else{

					$this->response["error"] = 1;
					$this->response["error_msg"] = "Center not added!";
				}

			}else{

				$this->response["error"] = 1;
				$this->response["error_msg"] = "Center already added!";
			}
			

			return json_encode($this->response);
		}//end of function


		function add_services()
		{
			
			$query = "INSERT INTO tbl_center_services(CENTER_NO,SERVICE) VALUES()";
			$result = mysql_query($query) or die(mysql_error());
			if(mysql_affected_rows() > 0){

				$this->response["success"] = 1;
				$this->response["success_msg"] = "Services added";
			}else{

				$this->response["error"] = 1;
				$this->response["error_msg"] = "Services not added!";
			}

			return json_encode($this->response);
		}//end of function



		function get_centers()
		{
			
			$this->response["success"] = 1;
			$this->response["success_msg"] = "Nearby centers retrieved";

			return json_encode($this->response);
		}//end of function

//============================================================================================

		private function check_location($lat,$lon)
		{

			$query = "SELECT * FROM tbl_emerg_centers WHERE LOC_LAT = '$lat' AND LOC_LAT='$lon'";
			$result = mysql_query($query) or die(mysql_error());
			if(mysql_affected_rows() > 0){
				return TRUE;
			}else{
				return FALSE;
			}
		}//end of function

		private function add_contact($center_no,$contact,$verified)
		{
			
			$query = "SELECT * FROM tbl_center_contacts WHERE CENTER_NO = '$center_no' AND CONTACT='$contact'";
			$result = mysql_query($query) or die(mysql_error());
			if(mysql_affected_rows() > 0){
				return TRUE;

			}else{
				
				$query = "INSERT INTO tbl_center_contacts(CENTER_NO,CONTACT,VERIFIED) VALUES()";
				$result = mysql_query($query) or die(mysql_error());
				if(mysql_affected_rows() > 0){

					return TRUE;
				}else{

					return FALSE;
				}

			}

		}//end of function

	}

?>