//
//  QRViewController.swift
//  A2IPNS
//
//  Created by billgateshxk on 2019/08/04.
//  Copyright © 2019 bi119aTe5hXk. All rights reserved.
//

import UIKit

class QRViewController: UIViewController {
    @IBOutlet weak var imgQRCode: UIImageView!
    var qrcodeImage: CIImage!
    let token = UserDefaults.standard.string(forKey: "pushtoken");
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        showQRCode();
    }
    
    func showQRCode(){
        
        
        if qrcodeImage == nil && (token?.lengthOfBytes(using: .utf8))! > 0 {
            
            let data = token?.data(using: String.Encoding.isoLatin1, allowLossyConversion: false)
            
            let filter = CIFilter(name: "CIQRCodeGenerator")
            
            filter?.setValue(data, forKey: "inputMessage")
            filter?.setValue("Q", forKey: "inputCorrectionLevel")
            
            qrcodeImage = filter?.outputImage
        }
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
