<?php
    include('./db_connect.php');
    $carNumber = $_POST['carNumber'];
    $Exterior = $_POST['Exterior'];
    $Interior = $_POST['Interior'];
    $Odour = $_POST['Odour'];
	$sql = "insert into androidqcdata(carNumber, Exterior, Interior, Odour) values ('$carNumber', '$Exterior', '$Interior', '$Odour')";
  	if (mysqli_query($conn,$sql)) {
    	echo 'success';
  	}
  	else {
    	echo 'failure';
  	}
    $conn->close();
?>