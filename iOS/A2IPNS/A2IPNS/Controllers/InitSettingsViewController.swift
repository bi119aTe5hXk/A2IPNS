//
//  InitSettingsViewController.swift
//  A2IPNS
//
//  Created by billgateshxk on 2019/09/20.
//  Copyright © 2019 bi119aTe5hXk. All rights reserved.
//

import UIKit
import UserNotifications
//import CoreBluetooth //USE FOR TESTING BLE DEVICE

class InitSettingsViewController: UITableViewController{//,CBPeripheralManagerDelegate { //USE FOR TESTING BLE DEVICE
    
    

    @IBOutlet weak var grantNotifyPermissionBTN: UIButton!
    @IBOutlet weak var installA2PNSBTN: UIButton!
    @IBOutlet weak var showQRCodeBTN: UIButton!
    @IBOutlet weak var doneBTN: UIButton!
    
    //var peripheralManager: CBPeripheralManager! //USE FOR TESTING BLE DEVICE
    

    override func viewDidLoad() {
        super.viewDidLoad()
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
        
        //USE FOR TESTING BLE DEVICE
        //peripheralManager = CBPeripheralManager.init(delegate: self, queue: nil)


        self.resetUI()
        let notFirstTimeBoot = UserDefaults.standard.bool(forKey: "not_first_time")
        if notFirstTimeBoot {
            self.tryGrantNotificationPermission()//TODO
            //self.checkTokenAndSetUI()
        }
    }


    @IBAction func grantNotifyPermissionBTNPressed(_ sender: Any) {
        self.tryGrantNotificationPermission()
    }

    @IBAction func installA2PNSBTNPressed(_ sender: Any) {
        let url = URL(string: a2pnsGooglePlayStoreURL)
        if(UIApplication.shared.canOpenURL(url!)) {
            if #available(iOS 10.0, *) {
                UIApplication.shared.open(url!)
            } else {
                // Fallback on earlier versions
                UIApplication.shared.openURL(url!)
            }
        }
    }


    @IBAction func doneBTNPressed(_ sender: Any) {
        UserDefaults.standard.set(true, forKey: "not_first_time")
        self.dismiss(animated: true, completion: nil)
    }

    func tryGrantNotificationPermission() {
        if #available(iOS 10.0, *) {
            let center = UNUserNotificationCenter.current()
            // Request permission to display alerts and play sounds.
            center.requestAuthorization(options: [.alert, .badge, .sound])
            { (granted, error) in
                if (error != nil){
                    print("grant notifation error \(String(error!.localizedDescription))")
                    DispatchQueue.main.async {
                        let alert = UIAlertController(title: NSLocalizedString("ERROR_ALERT_TITLE", comment: ""), message: error!.localizedDescription, preferredStyle: .alert)
                        alert.addAction(UIAlertAction(title: NSLocalizedString("OK", comment: ""), style: .cancel, handler: nil))
                        self.present(alert, animated: true, completion: nil)
                    }
                }
                
                // Enable or disable features based on authorization.
                if granted {
                    print("APNS Allowed")
                    DispatchQueue.main.async {
                        UIApplication.shared.registerForRemoteNotifications()
                        self.checkTokenAndSetUI()
                    }
                } else {
                    print("APNS NOT ALLOWED.")
                    DispatchQueue.main.async {
                        let alert = UIAlertController(title: NSLocalizedString("GRANT_NOFITICATION_ERROR_ALERT_TITLE", comment: ""), message: NSLocalizedString("GRANT_NOFITICATION_ERROR_ALERT_MESSAGE", comment: ""), preferredStyle: .alert)
                        alert.addAction(UIAlertAction(title: NSLocalizedString("OPEN_SETTINGS", comment: ""), style: .default, handler: { (action) in
                            if let url = URL(string: UIApplication.openSettingsURLString) {
                                if UIApplication.shared.canOpenURL(url) {
                                    if #available(iOS 10.0, *) {
                                        UIApplication.shared.open(url, options: [:], completionHandler: nil)
                                    } else {
                                        // Fallback on earlier versions
                                        UIApplication.shared.openURL(url)
                                    }
                                }
                            }
                        }))
                        alert.addAction(UIAlertAction(title: NSLocalizedString("OK", comment: ""), style: .cancel, handler: nil))
                        self.present(alert, animated: true, completion: nil)
                    }
                }
            }
        }else {
            let type: UIUserNotificationType = [UIUserNotificationType.badge, UIUserNotificationType.alert, UIUserNotificationType.sound]
            let setting = UIUserNotificationSettings(types: type, categories: nil)
            UIApplication.shared.registerUserNotificationSettings(setting)
            UIApplication.shared.registerForRemoteNotifications()
            self.checkTokenAndSetUI()
        }
    }
    
    
    
    func checkTokenAndSetUI() {
        let token = UserDefaults.standard.string(forKey: "pushtoken")
        if token != nil && (token?.lengthOfBytes(using: .utf8))! > 0 {
            self.grantNotifyPermissionBTN.isEnabled = false
            self.grantNotifyPermissionBTN.setTitle("✅ Permission granted", for: .normal)
            self.showQRCodeBTN.isEnabled = true
            self.installA2PNSBTN.isEnabled = true
            self.doneBTN.isEnabled = true
        } else {
            print(" Error: Somehow permission is granted but token is nil. Try again?")
            DispatchQueue.main.async {
                let alert = UIAlertController(title: "Error (X_X)", message: "Somehow permission is granted but token is nil. Try again?", preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                self.present(alert, animated: true, completion: nil)
            }
            self.resetUI()
        }
    }
    
    func resetUI() {
        self.grantNotifyPermissionBTN.isEnabled = true
        self.grantNotifyPermissionBTN.setTitle("Grant permissions", for: .normal)
        self.showQRCodeBTN.isEnabled = false
        self.showQRCodeBTN.setTitle("Show QR Code", for: .normal)
        self.installA2PNSBTN.isEnabled = false
        self.doneBTN.isEnabled = false
    }


    // MARK: - Table view data source

//    override func numberOfSections(in tableView: UITableView) -> Int {
//        // #warning Incomplete implementation, return the number of sections
//        return 0
//    }
//
//    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
//        // #warning Incomplete implementation, return the number of rows
//        return 0
//    }

    /*
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "reuseIdentifier", for: indexPath)

        // Configure the cell...

        return cell
    }
    */

    /*
    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */
    
    //USE FOR TESTING BLE DEVICE
//    func peripheralManagerDidUpdateState(_ peripheral: CBPeripheralManager) {
//        if(peripheral.state == .poweredOn) {
//            self.peripheralManager.startAdvertising([CBAdvertisementDataLocalNameKey:"A2IPNS"])
//            print("peripheral is advertising...")
//        }else{
//            self.peripheralManager.stopAdvertising()
//        }
//    }

}
