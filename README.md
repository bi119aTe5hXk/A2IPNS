# Android to iOS Push Notification Services
An app series to enable Android notification redirection to iOS platforms

<a href="https://play.google.com/store/apps/details?id=org.xlfdll.a2pns">
  <img src="https://github.com/xlfdll/xlfdll.github.io/raw/master/images/google-play-badge.png" alt="Get A2PNS (Android) on Google Play Store" height="64">
</a>

## How to Build

1. Add a new Explicit App ID in your Apple Developer account. [Then get a .p8 file from Apple](https://developer.apple.com/documentation/usernotifications/setting_up_a_remote_notification_server/establishing_a_token-based_connection_to_apns).

    You have to enroll in the Apple Developer Program, and you can't use a Wildcard App ID for push notification services. The App ID must be explicit. Don't forget to check the "Push Notifications" box in "Capabilities" list.

2. Edit Web/cert_data.php file by replacing $authKey, and [teamid, authkeyid, id] in the array (id is the App ID added in step 1). Then upload cert.php, cert_data.php and .p8 files to your server. Notice that this requires PHP 7.1 or later versions with OpenSSL extension installed. This process only needs to be done once to provide authentication token to Android app at launch.

3. Generate a Provisioning Profile with the Explicit App ID, and download the .mobileprovision file.

4. Import .mobileprovision file into Xcode and build.

Refer to [here](https://github.com/xlfdll/A2PNS/blob/master/README.md) for Android app build instructions

## How to Use

1. Install A2IPNS app on your iOS device and enable notification permissions.

2. Install [A2PNS](https://github.com/xlfdll/A2PNS) app on your Android device, and grant notification permissions. Scan the QR code which shown on your iOS device, using your Android device to pair two devices.

3. Enable the service on Android app. And select applications to be monitored in settings.

4. Wait for notifications showing up on your Android device. They should be automatically delivered to your iOS devices momentarily.

## License / Contributors

(C) 2019 [bi119aTe5hXk](https://blog.bi119ate5hxk.net) (iOS, PHP, Icon Design (Initial))

(C) 2019 [Xlfdll Workstation](https://xlfdll.github.io) (Android, PHP, Icon Design)

bi119aTe5hXk has the right of the explanation on licensing options for the whole system.
