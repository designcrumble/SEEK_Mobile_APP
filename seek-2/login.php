
<?php
 
/*
 * Following code will get user details and log him in
 * A product is identified by email
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// check for post data
if (isset($_POST['name'])&& isset($_POST['pass'])) {
    $name = $_POST['name'];
	$pass = $_POST['pass'];
	$con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD,DB_DATABASE) ;
    // get a user validated from login table
	

    $result = mysqli_query($con, "select * from login where name = '".$name."' AND pass = '".$pass. "'");
 
	$rows = mysqli_num_rows($result);
	//echo $rows;
	if($rows == 0) { 
		$response["success"] = 0;
        $response["message"] = "No user exists";
        // echoing JSON response
        echo json_encode($response);
	}
	else  {
		$response["success"] = 1;
        $response["message"] = "Welcome";
        // echoing JSON response
        echo json_encode($response);
	}
} 
else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    //echoing JSON response
    echo json_encode($response);
}
?>