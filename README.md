# Android to Push Notification Services
An app series to enable Android notification redirection to other platforms (iOS, Android, etc.)

## How to Build

1. Add a new Explicit App ID in your Apple developer account. [Then get a .p8 file from Apple](https://developer.apple.com/documentation/usernotifications/setting_up_a_remote_notification_server/establishing_a_token-based_connection_to_apns). (You have to enroll in the Apple Developer Program, and you can't use a Wildcard App ID for push notification services, it must be explicit. And don't forget to check the box in Push Notifications under the Capabilities)

2. Edit Web/cert_data.php file. Then upload cert.php, cert_data.php and .p8 files to your server. It requires OpenSSL and php7.1 installed and this only require once for save authentication token to Android app at the first time launch.

3. Generate a Provisioning Profile with the Explicit App ID. And download the .mobileprovision file.

4. Import the .mobileprovision file into Xcode and build.

5. Open Android project in Android Studio.

6. Create an ExternalData.kt file and add your server URL and decryption secret.

7. Build the Android project.


## How to Use

1. Open A2IPNS app on your iOS device, allow for receiving the push notification.

2. Open A2IPNS app on your android device, allow for notification permission. Scan the QR code on your iOS device using your Android device to pair.

3. Wait for your Android device to receive any notification, it should automatic shows up both on your Android and iOS devices.

--------------------------------------
## License / Contributors

(C) 2019 [bi119aTe5hXk](https://blog.bi119ate5hxk.net) (iOS, PHP, Icon Design)
(C) 2019 [Xlfdll Workstation](https://xlfdll.github.io) (Android, PHP, Icon Design)

bi119aTe5hXk has the right of the explanation on licensing options for the whole system.
