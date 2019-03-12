//
//  AddFoodViewController.swift
//  fitnessTrackerPro
//
//  Created by Ari Klebanov on 3/10/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import UIKit

class AddFoodViewController: UIViewController, UITextFieldDelegate {
    
    var foodData = FoodDataController()
    
    var addedFoodName = String()
    var addedFoodCalories = Int()
    var addedFoodTime = String()
    
    @IBAction func onTapGestureRecognized(_ sender: UITapGestureRecognizer) {
        addedFoodNameField.resignFirstResponder()
        addedFoodCaloriesField.resignFirstResponder()
        addedFoodTimeField.resignFirstResponder()
    }
    @IBOutlet weak var addedFoodNameField: UITextField!
    @IBOutlet weak var addedFoodCaloriesField: UITextField!
    @IBOutlet weak var addedFoodTimeField: UITextField!

    func textFieldShouldReturn(_ textField : UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        addedFoodNameField.delegate = self
        addedFoodCaloriesField.delegate = self
        addedFoodTimeField.delegate = self
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
