# How To Setup Server

1. Download .cert file from Apple developer portal.

2. Open Terminal.app, cd to the path where the .cer file is. And execute this command to covert .cer file to .crt file.

```
openssl x509 -in aps.cer -inform DER -out aps.crt
```

3. Edit Web/cert_data.php file by replacing $cert and change $updatetime to currect date or your favorite number. 

4. Upload cert.php, cert_data.php and aps.crt files to your server. 
	
	Notice that this requires PHP 7.1 or later versions with OpenSSL extension installed. 
	This process only needs to be done once to provide authentication token to Android app at launch.
