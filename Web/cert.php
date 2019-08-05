<?php
    include_once "cert_data.php";

    $cipher = "aes-256-gcm";
    $ivlen = openssl_cipher_iv_length($cipher);
    $iv = openssl_random_pseudo_bytes($ivlen);
    $tag = "";
    $tag_length = 16;
    $aad = "A2IPNS";

    $ciphertext_raw = openssl_encrypt($plaintext, $cipher, $key, OPENSSL_RAW_DATA, $iv, $tag, $aad, $tag_length);
    $ciphertext_base64 = base64_encode($ciphertext_raw).":".base64_encode($tag).":".base64_encode($iv);

    $array = array(
        "time" => $updatetime ,
        "cert" => $ciphertext_base64,
    );
    $json = json_encode( $array );
    echo $json;
?>