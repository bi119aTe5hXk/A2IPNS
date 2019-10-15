# How To Setup Server

1. Download application's remote push notification .cert file from Apple developer portal.

2. Download AppleWWDRCA.cer and AppleRootCertificate.cer from [Apple PKI](https://www.apple.com/certificateauthority/).

3. Open Terminal.app, cd to the path where the .cer file is. And execute this command to covert .cer file to .crt file.

```
openssl x509 -in aps_pro.cer -inform DER -out cert_pro.crt

openssl x509 -in AppleWWDRCA.cer -inform DER -out cert_ca.crt

openssl x509 -in AppleRootCertificate.cer -inform DER -out cert_root.crt
```

4. Edit Web/cert_data.php file by replacing $cert and change $updatetime to currect date or your favorite number. 

5. Upload cert.php, cert_data.php and all .cert files to your server. 
	
	Notice that this requires PHP 7.1 or later versions with OpenSSL extension installed. 
	This process only needs to be done once to provide authentication token to Android app at launch.
