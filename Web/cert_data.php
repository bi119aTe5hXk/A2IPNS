<?php

 	$updatetime = "20190101";
	$cert = "AuthKey_YOUCERT.pem";
	$array = array(
        "cert" => base64_encode(file_get_contents($cert)),
		"id" => 'net.bi119aTe5hXk.A2IPNS',
    );

    $key = "YOURENCRYPTKEY";
    $plaintext = json_encode( $array );
?>