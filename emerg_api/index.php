<?php
/**
*All Honor and Glory belongs to God Almighty
*@since 31st Jul, 2014
*@version 1.0
*@author Dickson Owuor
*/
	
	if (isset($_POST['req']) && $_POST['req'] != "") {
		// get tag
		$req = filter_input(INPUT_POST, 'req', FILTER_SANITIZE_STRING);
		
		require_once("functions.php");
		$emerg = new FUNCTIONS($req);
		
		if($req == "test"){
		
			echo $emerg->test($req)."<br>";
			//echo $emerg->add_center()."<br>";
			//echo $emerg->add_services()."<br>";
			//echo $emerg->get_centers()."<br>";
		
		}else if($req == "add-center"){
			//center-no
			$name = filter_input(INPUT_POST, 'name', FILTER_SANITIZE_STRING);
			$lat = filter_input(INPUT_POST, 'lat', FILTER_SANITIZE_STRING);
			$lon = filter_input(INPUT_POST, 'lon', FILTER_SANITIZE_STRING);
			$email = filter_input(INPUT_POST, 'email', FILTER_SANITIZE_STRING);
			$phone1 = filter_input(INPUT_POST, 'phone1', FILTER_SANITIZE_STRING);
			$phone2 = filter_input(INPUT_POST, 'phone2', FILTER_SANITIZE_STRING);
			$phone3 = filter_input(INPUT_POST, 'phone3', FILTER_SANITIZE_STRING);

			echo $emerg->add_center($name,$lat,$lon,$email,$phone1,$phone2,$phone3);


		}else if($req == "add-services"){
			//center-no
			//service(s)
			$center_no = filter_input(INPUT_POST, 'center_no', FILTER_SANITIZE_STRING);
			$service = filter_input(INPUT_POST, 'service', FILTER_SANITIZE_STRING);

			echo $emerg->add_services($center_no,$service);

		}else if($req == "get-centers"){
			//radius or current position (lat, lon)

			echo $emerg->get_centers();

		}else{
		
			$response =  array("request" => $req, "success" => 0, "error" => 1, "error_msg" => "Invalid Request");
			//print_r($response);
			echo json_encode($response);
		}
	
	}else{
	
		$response =   array("success" => 0, "error" => 1, "error_msg" => "Access Denied");
		//print_r($response);
		echo json_encode($response);
	}


?>