# How To Setup Server

1. Copy *.php, *.p8, and composer.* to the server
2. In the directory of above files, run **composer install** or **composer.phar install** (see which one works)
   * If both are not working, you need to [install Composer](https://getcomposer.org/download/)
3. Modify jwt_data.php, and replace all data with yours (see [here](https://developer.apple.com/documentation/usernotifications/setting_up_a_remote_notification_server/establishing_a_token-based_connection_to_apns) to know what are required)
4. Done
