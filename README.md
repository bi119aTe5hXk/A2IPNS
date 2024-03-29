# Android to iOS Push Notification Services
An app series to enable Android notification redirection to iOS platforms.

This project require Android client [A2PNS](https://github.com/xlfdll/A2PNS).

<a href="https://play.google.com/store/apps/details?id=org.xlfdll.a2pns">
  <img src="https://github.com/xlfdll/xlfdll.github.io/raw/master/images/google-play-badge.png" alt="Get A2PNS (Android) on Google Play Store" height="64">
</a>

[Join the iOS testflight](https://testflight.apple.com/join/4ioSUsT3)

## How to Build

1. Add a new Explicit App ID in your Apple Developer account. [Then get a .cert file from Apple](https://developer.apple.com/documentation/usernotifications/setting_up_a_remote_notification_server/establishing_a_certificate-based_connection_to_apns?language=objc).

    You have to enroll in the Apple Developer Program, and you can't use a Wildcard App ID for push notification services. The App ID must be explicit. Don't forget to check the "Push Notifications" box in "Capabilities" list.

2. Create a p8 files. See [INSTALL.md in Web folder](https://github.com/bi119aTe5hXk/A2IPNS/tree/master/Web/INSTALL.md) or [bi119aTe5hXk's post](https://blog.bi119ate5hxk.net/2023/05/30/%e6%89%8b%e5%8a%a8%e9%83%a8%e7%bd%b2a2ipns%e6%9c%8d%e5%8a%a1%e6%95%99%e7%a8%8b/) for more detail.


3. Generate a Provisioning Profile with the Explicit App ID, and download the .mobileprovision file.

4. Import .mobileprovision file into Xcode and build.

Refer to [here](https://github.com/xlfdll/A2PNS/blob/master/README.md) for Android app build instructions

## How to Use

1. Install A2IPNS app on your iOS device and enable notification permissions.

2. Install [A2PNS](https://github.com/xlfdll/A2PNS) app on your Android device, and grant notification permissions. Scan the QR code which shown on your iOS device, using your Android device to pair two devices.

3. Enable the service on Android app. And select applications to be monitored in settings.

4. Notifications should showing up on your Android device. They should be automatically delivered to your iOS devices momentarily.

## License / Contributors

(C) 2019-2022 [bi119aTe5hXk](https://blog.bi119ate5hxk.net) (iOS, PHP, Icon Design (Initial))

(C) 2019-2022 [Xlfdll Workstation](https://xlfdll.github.io) (Android, PHP, Icon Design)
