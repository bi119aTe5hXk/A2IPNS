<?php
    require_once "vendor/autoload.php";
    require_once "jwt.php";

    $jwt_file_name = "/tmp/A2IPNS_JWT.json";
    $jwt_file_expire_minutes = 50;

    $time = time();
    $jwt_existing_data = file_exists($jwt_file_name) ? json_decode(file_get_contents($jwt_file_name)) : NULL;

    if (is_null($jwt_existing_data) || ($time - $jwt_existing_data->iat) / 60 > $jwt_file_expire_minutes)
    {
        $jwt_existing_data = array(
            "iat" => $time,
            "jwt" => (string)generate_a2ipns_jwt($time)
        );

        file_put_contents($jwt_file_name, json_encode($jwt_existing_data));
    }
    
    echo json_encode($jwt_existing_data);
?>
