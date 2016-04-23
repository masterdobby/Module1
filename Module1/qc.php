<?php
    include('./db_connect.php');
    $carNumber = $_POST['carNumber'];
	$sql = "select * from androidqc where carNumber='$carNumber'";
	$r = mysqli_query($conn,$sql);
	$res = mysqli_fetch_array($r);
	$result = array();
	array_push($result,array(
		"Exterior"=>$res['Exterior'],
		"Interior"=>$res['Interior'],
		"Odour"=>$res['Odour']
		)
	);
	echo json_encode(array("result"=>$result));
	$conn->close();
?>