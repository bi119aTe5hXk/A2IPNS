# Android to iOS Push Notification Services
An app series to enable Android notification redirection to iOS platforms

## How to Build

1. Add a new Explicit App ID in your Apple Developer account. [Then get a .p8 file from Apple](https://developer.apple.com/documentation/usernotifications/setting_up_a_remote_notification_server/establishing_a_token-based_connection_to_apns).

    You have to enroll in the Apple Developer Program, and you can't use a Wildcard App ID for push notification services. The App ID must be explicit. Don't forget to check the "Push Notifications" box in "Capabilities" list.

2. Edit Web/cert_data.php file by replacing $authKey, and [teamid, authkeyid, id] in the array (id is the App ID added in step 1). Then upload cert.php, cert_data.php and .p8 files to your server. Notice that this requires PHP 7.1 or later versions with OpenSSL extension installed. This process only needs to be done once to provide authentication token to Android app at launch.

3. Generate a Provisioning Profile with the Explicit App ID, and download the .mobileprovision file.

4. Import .mobileprovision file into Xcode and build.

5. Open the Android project in Android Studio.

6. Create an ExternalData.kt file with the following code structures, and add your server URL and decryption secret:

```
package net.bi119ate5hxk.a2ipns

internal object ExternalData {
    // true - Create Apple Push Notification JSON payload but do not send
    // false - Production Mode. Send actual JSON payload to Apple Push Notification Service
    const val MockDebugMode = true
    const val APNSAuthTokenURL = <Your Server URL: String>
    const val DecryptionSecret = <Your Decryption Secret: String>
}
```

7. Build the Android project and test.


## How to Use

1. Install A2IPNS app on your iOS device and enable notification permissions.

2. Install [A2PNS](https://github.com/xlfdll/A2PNS) app on your Android device, and grant notification permissions. Scan the QR code which shown on your iOS device, using your Android device to pair two devices.

3. Enable the service on Android app.

4. Wait for notifications showing up on your Android device. They should be automatically delivered to your iOS devices momentarily.

## License / Contributors

(C) 2019 [bi119aTe5hXk](https://blog.bi119ate5hxk.net) (iOS, PHP, Icon Design (Initial))

(C) 2019 [Xlfdll Workstation](https://xlfdll.github.io) (Android, PHP, Icon Design)

bi119aTe5hXk has the right of the explanation on licensing options for the whole system.
