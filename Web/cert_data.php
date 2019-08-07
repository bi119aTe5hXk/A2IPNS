<?php

 	$updatetime = "20190101";
	$authKey = "AuthKey_YOURAUTHKEY.p8";
	$array = array(
        "authKey" => base64_encode(file_get_contents($authKey)) ,
        "teamid" => 'YOURTEAMID',
		"authkeyid" => 'YOURAUTHKEY',
		"id" => 'net.bi119aTe5hXk.A2IPNS',
    );

    $key = "YOURENCRYPTKEY";
    $plaintext = json_encode( $array );
?>