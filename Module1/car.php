<?php
    include('./db_connect.php');
    $carNumber = $_POST['carNumber'];
	$sql = "select * from androidcar where carNumber='$carNumber'";
	$res = mysqli_query($conn,$sql);
	$check = mysqli_fetch_array($res);
	if (isset($check)) {
		echo 'success';
	} else {
		echo 'failure';
	}
    $conn->close();
?>