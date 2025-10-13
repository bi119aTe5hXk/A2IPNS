<?php
use Lcobucci\JWT\Configuration;
use Lcobucci\JWT\Signer\Ecdsa\Sha256;
use Lcobucci\JWT\Signer\Key\InMemory;

function generate_a2ipns_jwt(int $time): string
{
    require_once "jwt_data.php";

    $config = Configuration::forAsymmetricSigner(
        new Sha256(),
        InMemory::file($signing_key_file), 
        InMemory::file($signing_key_file)
    );

    $issuedAt = (new DateTimeImmutable())->setTimestamp($time);

    $token = $config->builder()
        ->withHeader('kid', $kid)  // header: key id
        ->issuedBy($iss)           // payload: iss
        ->issuedAt($issuedAt)      // payload: iat
        ->getToken($config->signer(), $config->signingKey());

    return $token->toString();
}
?>
