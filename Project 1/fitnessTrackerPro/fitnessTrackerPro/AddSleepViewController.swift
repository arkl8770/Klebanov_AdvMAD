//
//  AddSleepViewController.swift
//  fitnessTrackerPro
//
//  Created by Ari Klebanov on 3/10/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import UIKit

class AddSleepViewController: UIViewController, UITextFieldDelegate {
    
    var sleepData = SleepDataController()
    
    var addedSleepStart = String()
    var addedSleepRating = Int()
    var addedSleepEnd = String()

    @IBAction func onTapGestureRecognized(_ sender: UITapGestureRecognizer) {
        addedSleepStartField.resignFirstResponder()
        addedSleepEndField.resignFirstResponder()
        addedSleepRatingField.resignFirstResponder()
    }
    @IBOutlet weak var addedSleepStartField: UITextField!
    @IBOutlet weak var addedSleepEndField: UITextField!
    @IBOutlet weak var addedSleepRatingField: UITextField!
    
    func textFieldShouldReturn(_ textField : UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        addedSleepStartField.delegate = self
        addedSleepEndField.delegate = self
        addedSleepRatingField.delegate = self
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
