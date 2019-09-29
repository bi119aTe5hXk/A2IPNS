# How To Setup Server

1. Download and import your .cert file to your KeychainAccess.app, find the certificate and export .p12 private key.

2. Open Terminal.app, cd to the path where the .p12 file is. And execute these commandline:

   - openssl pkcs12 -clcerts -nokeys -out cert.pem -in Certificates.p12

   - openssl pkcs12 -nocerts -out key.pem -in Certificates.p12

   - openssl rsa -in key.pem -out key.unencrypted.pem

   - cat cert.pem key.unencrypted.pem > ck.pem


3. Edit Web/cert_data.php file by replacing $cert and change $updatetime to currect date or your favorite number. 

4. Upload cert.php, cert_data.php and ck.pem files to your server. 
	
	Notice that this requires PHP 7.1 or later versions with OpenSSL extension installed. 
	This process only needs to be done once to provide authentication token to Android app at launch.