//
//  SecondViewController.swift
//  MusicMasterPro
//
//  Created by Ari Klebanov on 1/31/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import UIKit

class SecondViewController: UIViewController {
    @IBAction func openMusic(_ sender: UIButton) {
        //check to see if there's an app installed to handle this URL scheme
        if (UIApplication.shared.canOpenURL(URL(string: "spotify://")!)){
            //open the app with this URL scheme
            UIApplication.shared.open(URL(string: "spotify://")!, options: [:], completionHandler: nil)
        } else {
            if (UIApplication.shared.canOpenURL(URL(string: "music://")!)){
                UIApplication.shared.open(URL(string: "music://")!, options: [:], completionHandler: nil)
            } else {
                UIApplication.shared.open(URL(string: "http://www.apple.com/music/")!, options: [:], completionHandler: nil)
            }
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }


}

