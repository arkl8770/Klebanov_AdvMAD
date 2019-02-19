//
//  DetailViewController.swift
//  Shoe Rack Pro
//
//  Created by Ari Klebanov on 2/18/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import UIKit

class DetailViewController: UIViewController {
    
    var data = DataController()
    var selectedStyle = String()
    var selectedShoe = String()
    var allShoes = [Shoes]()
    var styles = [String]()
    
    @IBOutlet weak var shoeText: UILabel!
    
    override func viewWillAppear(_ animated: Bool) {
        
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        shoeText.text? = """
        Good choice!
        
        I love the \(selectedShoe) \(selectedStyle)'s
        """
        
       

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
    }
}
