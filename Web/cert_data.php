<?php

 	$updatetime = "20190101";
	$maincert = "cert_pro.pem";
	$wwdrcacert = "cert_ca.pem";
	$rootcert = "cert_root.pem";
	$array = array(
        "cert" => array(
          base64_encode(file_get_contents($maincert)),
          base64_encode(file_get_contents($cacert)),
          base64_encode(file_get_contents($rootcert))),
        "id" => 'net.bi119aTe5hXk.A2IPNS',
    );

    $key = "YOURENCRYPTKEY";
    $plaintext = json_encode( $array );
?>