<?php
    $key = pack('H*', "123456789");
    $plaintext = "123456789";

    $iv_size = mcrypt_get_iv_size(MCRYPT_RIJNDAEL_128, MCRYPT_MODE_CBC);
    $iv = mcrypt_create_iv($iv_size, MCRYPT_RAND);
    
    $ciphertext = mcrypt_encrypt(MCRYPT_RIJNDAEL_128, $key,  $plaintext, MCRYPT_MODE_CBC, $iv);
    $ciphertext = $iv . $ciphertext;
    $ciphertext_base64 = base64_encode($ciphertext);

    $array = array(
        "time" => "20190804" ,
        "cert" => $ciphertext_base64,
    );
    $json = json_encode( $array );
    echo $json;
    //echo  $ciphertext_base64 . "\n";
?>