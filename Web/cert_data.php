<?php

 	$updatetime = "20190101";
	$maincert = "aps_pro.crt";
	$cacert = "cert_ca.crt";
	$rootcert = "cert_root.crt";
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
