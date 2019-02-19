//
//  AddShoeViewController.swift
//  Shoe Rack Pro
//
//  Created by Ari Klebanov on 2/19/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import UIKit

class AddShoeViewController: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource{
    
    var pickerData = [String]()
    var newStyle = "Authentic"
    var newShoe = String()

    @IBOutlet weak var stylePicker: UIPickerView!
    @IBOutlet weak var shoeField: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        pickerData = ["Authentic", "Chima Pro 2", "Era", "Kyle Walker Pro", "Old-Skool", "Slip-On", "Sk8-Hi"]
        self.stylePicker.delegate = self
        self.stylePicker.dataSource = self
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return pickerData.count
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return pickerData[row]
    }
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        newStyle = pickerData[stylePicker.selectedRow(inComponent: 0)]
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "doneSegue" {
            if (shoeField.text?.isEmpty) == false {
                newShoe=shoeField.text!
                
            }
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
