//
//  QRViewController.swift
//  A2IPNS
//
//  Created by billgateshxk on 2019/08/04.
//  Copyright © 2019 bi119aTe5hXk. All rights reserved.
//

import UIKit
import UserNotifications
import CoreImage

class QRViewController: UIViewController {
    @IBOutlet weak var imgQRCode: UIImageView!
    @IBOutlet weak var tokenLabel:UILabel!
    var qrcodeImage: CIImage!
    let token = UserDefaults.standard.string(forKey: "pushtoken")
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        if token != nil {
            
            self.tokenLabel.text = token!
            let qrDict:[String:String] = ["id":"A2IPNS","token":token!];
            let data = try? JSONSerialization.data(withJSONObject: qrDict, options: [])
            print("qrstr:" + String(data: data!, encoding: String.Encoding.utf8)!)
            let qrimage = generateQRCode(from: String(data: data!, encoding: String.Encoding.utf8)!)
            self.imgQRCode.image = qrimage
        }else{
            self.tokenLabel.text = "Please enable Notification in Settings application."
        }
    }
    
    func generateQRCode(from string: String) -> UIImage? {
        let data = string.data(using: String.Encoding.ascii)
        
        if let filter = CIFilter(name: "CIQRCodeGenerator") {
            filter.setValue(data, forKey: "inputMessage")
            let transform = CGAffineTransform(scaleX: 3, y: 3)
            
            if let output = filter.outputImage?.transformed(by: transform) {
                return UIImage(ciImage: output)
            }
        }
        return nil
    }
    @IBAction func showInfoBTNPressed(_ sender: Any) {
        let alert = UIAlertController(title: "How to use", message: "Scan this QR code with A2PNS app on your Android device.", preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
