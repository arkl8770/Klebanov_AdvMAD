//
//  AddDrinkViewController.swift
//  fitnessTrackerPro
//
//  Created by Ari Klebanov on 3/10/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import UIKit

class AddDrinkViewController: UIViewController, UITextFieldDelegate {
    
    var drinkData = DrinkDataController()
    
    var addedDrinkName = String()
    var addedDrinkVolume = Int()
    var addedDrinkTime = String()

    @IBAction func onTapGestureRecognized(_ sender: UITapGestureRecognizer) {
        addedDrinkNameField.resignFirstResponder()
        addedDrinkVolumeField.resignFirstResponder()
        addedDrinkTimeField.resignFirstResponder()
    }
    @IBOutlet weak var addedDrinkNameField: UITextField!
    @IBOutlet weak var addedDrinkVolumeField: UITextField!
    @IBOutlet weak var addedDrinkTimeField: UITextField!
    
    func textFieldShouldReturn(_ textField : UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        addedDrinkNameField.delegate = self
        addedDrinkVolumeField.delegate = self
        addedDrinkTimeField.delegate = self
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
