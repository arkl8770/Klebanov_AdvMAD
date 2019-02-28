//
//  DetailViewController.swift
//  paintingsPro
//
//  Created by Ari Klebanov on 2/27/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import UIKit

class DetailViewController: UIViewController {
    
    @IBOutlet weak var imageView: UIImageView!

    @IBAction func share(_ sender: UIBarButtonItem) {
        var paintingArray = [UIImage]()
        paintingArray.append(imageView.image!)
        let shareScreen = UIActivityViewController(activityItems: paintingArray, applicationActivities: nil)
        shareScreen.modalPresentationStyle = .popover
        shareScreen.popoverPresentationController?.barButtonItem = sender
        present(shareScreen, animated: true, completion: nil)
        
    }
    
    
    var imageName : String?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    override func viewWillAppear(_ animated: Bool) {
        if let name = imageName {
            imageView.image = UIImage(named: name)
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
