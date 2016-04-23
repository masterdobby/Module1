<?php
    include('./db_connect.php');
    $username = $_POST['username'];
	$password = $_POST['password'];
	$sql = "select * from androidlogin where username='$username' and password='$password'";
	$res = mysqli_query($conn,$sql);
	$check = mysqli_fetch_array($res);
	if (isset($check)) {
		echo 'success';
	} else {
		echo 'failure';
	}
    $conn->close();
?>