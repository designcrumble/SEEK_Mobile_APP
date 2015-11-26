
<?php
 
/*
 * Following code will create a new user row
 * All user details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['name']) && isset($_POST['email']) && isset($_POST['pass'])) {
 
    $name = $_POST['name'];
    $email = $_POST['email'];
    $pass = $_POST['pass'];
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
	$con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD,DB_DATABASE) ;
    // mysql inserting a new row
    $result = mysqli_query($con,"INSERT INTO login(name, email, pass) VALUES('$name', '$email', '$pass')");
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Sign up done successfully.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
//mysqli_close($con);
?>