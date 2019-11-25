<?php
    use Lcobucci\JWT\Builder;
    use Lcobucci\JWT\Signer\Key;
    use Lcobucci\JWT\Signer\Ecdsa\Sha256;
    
    function generate_a2ipns_jwt($time)
    {
        // Require statements must be placed here to get all required data into function scope
        require_once "jwt_data.php";

        return (new Builder())
            ->withHeader("kid", $kid)
            ->issuedBy($iss)
            ->issuedAt($time)
            ->getToken(new Sha256(), new Key("file://".$signing_key_file));
    }
?>
