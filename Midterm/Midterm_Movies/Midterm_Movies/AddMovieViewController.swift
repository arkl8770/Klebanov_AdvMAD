//
//  AddMovieViewController.swift
//  Midterm_Movies
//
//  Created by Ari Klebanov on 3/14/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import UIKit

class AddMovieViewController: UIViewController {
    
    var addedMovieName = String()
    var addedMovieURL = String()

    @IBOutlet weak var nameTextField: UITextField!
    @IBOutlet weak var urlTextField: UITextField!
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
            if (nameTextField.text?.isEmpty == false) && (urlTextField.text?.isEmpty == false) {
                addedMovieName = nameTextField.text!
                addedMovieURL = urlTextField.text!
            }
        }
    }
    

}
