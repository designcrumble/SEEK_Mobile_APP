
<?php
 
/*
 * Following code will list all the products
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// get all report from report table
$con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD,DB_DATABASE) ;
$result = mysqli_query($con ,"SELECT * FROM report") ;
 
// check for empty result
if (mysqli_num_rows($result) > 0) {
    // looping through all results
    // products node
    $response["products"] = array();
 
    while ($row = mysqli_fetch_assoc($result)) {
        // temp user array
        $product = array();
        $product["pid"] = $row["P_pid"];
        $product["name"] = $row["P_name"];
        $product["description"] = $row["P_description"];
        $product["category"] = $row["P_category"];
        $product["created"] = $row["P_created"];
 
        // push single product into final response array
        array_push($response["products"], $product);
    }
    // success
    $response["success"] = 1;
 
    // echoing JSON response
    echo json_encode($response);
} else {
    // no products found
    $response["success"] = 0;
    $response["message"] = "No products found";
 
    // echo no users JSON
    echo json_encode($response);
}
//mysqli_close($con);
?>