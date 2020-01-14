//
//  AppDelegate.swift
//  A2IPNS
//
//  Created by billgateshxk on 2019/08/04.
//  Copyright © 2019 bi119aTe5hXk. All rights reserved.
//

import UIKit
import UserNotifications

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?


    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        
        UserDefaults.standard.register(defaults: ["pushtoken": ""])
        UserDefaults.standard.register(defaults: ["notification_history": []])
        UserDefaults.standard.register(defaults: ["not_first_time" : false])
        
        
//        let center = UNUserNotificationCenter.current()
//        // Request permission to display alerts and play sounds.
//        center.requestAuthorization(options: [.alert, .badge, .sound])
//        { (granted, error) in
//            // Enable or disable features based on authorization.
//            if granted {
//                print("APNS Allowed")
//                DispatchQueue.main.async {
//                    self.tryRegisterAPNS()
//                }
//            } else {
//                print("APNS NOT ALLOWED.")
//                let alert = UIAlertController(title: "Notification required", message: "This application will not work without notification promission.\nPlease enable it in Settings.", preferredStyle: .alert)
//                alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
//                //                self.window.present(alert, animated: true, completion: nil)
//                self.window?.rootViewController?.present(alert, animated: true, completion: nil)
//            }
//        }
        
        
        
        return true
    }
//    func tryRegisterAPNS() {
//        UIApplication.shared.registerForRemoteNotifications()
//        //UIApplication.shared.registerUserNotificationSettings(center)
//    }

    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }

    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
        //self.tryRegisterAPNS()
        UIApplication.shared.registerForRemoteNotifications()
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }


    func application(_ application: UIApplication,
                     didRegisterForRemoteNotificationsWithDeviceToken
                     deviceToken: Data) {
        //self.sendDeviceTokenToServer(data: deviceToken)
        let token = deviceToken.map { String(format: "%.2hhx", $0) }.joined()
        print("tokenIs:" + token)
        UserDefaults.standard.set(token, forKey: "pushtoken")
        UserDefaults.standard.synchronize()
    }

    func application(_ application: UIApplication,
                     didFailToRegisterForRemoteNotificationsWithError
                     error: Error) {
        // Try again later.
        print("getTokenErr:" + error.localizedDescription)
    }
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        //foreground with open from press push item, update list TODO
        addNotificationToArr(userInfo: userInfo)
        //print("foreground")
        completionHandler(UIBackgroundFetchResult.newData)
    }
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any]) {
        //background
        //print("background")
        addNotificationToArr(userInfo: userInfo)
    }
    func addNotificationToArr(userInfo:[AnyHashable : Any]){
        print(userInfo)
        var notifyarr = UserDefaults.standard.array(forKey: "notification_history")
        
        let aps = userInfo["aps"] as? [AnyHashable: Any]
        let alert = aps?["alert"] as? Dictionary<String, Any>
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm"
        let date = Date()
        let dateString = dateFormatter.string(from: date)
        let dic2 = ["time":dateString]
        let newDic = alert?.merging(dic2,uniquingKeysWith: { (_, last) in last })
        
        notifyarr!.append([newDic])
        UserDefaults.standard.set(notifyarr, forKey: "notification_history")
        NotificationCenter.default.post(name: NSNotification.Name(rawValue: "update_history_list"), object: nil, userInfo: nil)
    }
    

}

