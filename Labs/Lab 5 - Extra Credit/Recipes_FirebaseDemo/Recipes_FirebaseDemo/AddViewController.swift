//
//  AddViewController.swift
//  Recipes_FirebaseDemo
//
//  Created by Ari Klebanov on 2/28/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import UIKit

class AddViewController: UIViewController {
    @IBOutlet weak var recipeName: UITextField!
    @IBOutlet weak var recipeUrl: UITextField!
    
    var addedRecipe = String()
    var addedUrl = String()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    

    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
        if segue.identifier == "saveSegue" {
            if recipeName.text?.isEmpty == false {
                addedRecipe = recipeName.text!
                addedUrl = recipeUrl.text!
            }
        }
    }

}
