<?php
/**
*All Honor and Glory belongs to God Almighty
*@since 31st Jul, 2014
*@version 1.0
*@author Dickson Owuor
*/
	
	if (isset($_GET['req']) && $_GET['req'] != "") {
		// get tag
		$req = filter_input(INPUT_GET, 'req', FILTER_SANITIZE_STRING);
		
		require_once("functions.php");
		$emerg = new FUNCTIONS($req);
		
		if($req == "test"){
		
			echo $emerg->test($req)."<br>";
			//echo $emerg->add_center()."<br>";
			//echo $emerg->add_services()."<br>";
			//echo $emerg->get_centers()."<br>";
		
		}else if($req == "add-center"){
			//center-no
			//name
			//lat
			//lon
			//email

			echo $emerg->add_center();


		}else if($req == "add-services"){
			//center-no
			//service(s)

			echo $emerg->add_services();

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