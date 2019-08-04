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
    let token = UserDefaults.standard.string(forKey: "pushtoken");
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        

        // Do any additional setup after loading the view.
        if token != nil {
            self.tokenLabel.text = token!
            let qrimage = generateQRCode(from: token!)
            self.imgQRCode.image = qrimage
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
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
