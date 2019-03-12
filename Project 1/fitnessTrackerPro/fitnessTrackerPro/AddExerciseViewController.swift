//
//  AddExerciseViewController.swift
//  fitnessTrackerPro
//
//  Created by Ari Klebanov on 3/10/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import UIKit

class AddExerciseViewController: UIViewController, UITextFieldDelegate {
    
    var exerciseData = ExerciseDataController()
    
    var addedExerciseName = String()
    var addedExerciseDuration = Int()
    var addedExerciseTime = String()

    @IBAction func onTapGestureRecognized(_ sender: UITapGestureRecognizer) {
        addedExerciseNameField.resignFirstResponder()
        addedExerciseDurationField.resignFirstResponder()
        addedExerciseTimeField.resignFirstResponder()
    }
    @IBOutlet weak var addedExerciseNameField: UITextField!
    @IBOutlet weak var addedExerciseDurationField: UITextField!
    @IBOutlet weak var addedExerciseTimeField: UITextField!
    
    func textFieldShouldReturn(_ textField : UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        addedExerciseNameField.delegate = self
        addedExerciseDurationField.delegate = self
        addedExerciseTimeField.delegate = self
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
