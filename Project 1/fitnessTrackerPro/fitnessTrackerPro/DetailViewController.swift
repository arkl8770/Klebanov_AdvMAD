//
//  DetailViewController.swift
//  fitnessTrackerPro
//
//  Created by Ari Klebanov on 3/10/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import UIKit

class DetailViewController: UIViewController, UITextFieldDelegate {
    
    var label1 = String()
    var label2 = String()
    var label3 = String()
    
    var textfield1 = String()
    var textfield2 = String()
    var textfield3 = String()
    
    var oldFoodItem = Food()
    var oldDrinkItem = Drink()
    var oldExerciseItem = Exercise()
    var oldSleepItem = Sleep()
    
    
    var selectedEntry = 0

    @IBOutlet weak var UILabel1: UILabel!
    @IBOutlet weak var UILabel2: UILabel!
    @IBOutlet weak var UILabel3: UILabel!
    
    
    @IBOutlet weak var textField1: UITextField!
    @IBOutlet weak var textField2: UITextField!
    @IBOutlet weak var textField3: UITextField!
    
    func textFieldShouldReturn(_ textField : UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        textField1.delegate = self
        textField2.delegate = self
        textField3.delegate = self
    }
    
    override func viewWillAppear(_ animated: Bool) {
        UILabel1.text! = label1
        UILabel2.text! = label2
        UILabel3.text! = label3
        textField1.text! = textfield1
        textField2.text! = textfield2
        textField3.text! = textfield3
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
