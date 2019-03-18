//
//  LoginViewController.swift
//  Recipes_FirebaseDemo
//
//  Created by Ari Klebanov on 3/4/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import UIKit
import Firebase
import GoogleSignIn

class LoginViewController: UIViewController, GIDSignInDelegate, GIDSignInUIDelegate {
    
    func sign(_ signIn: GIDSignIn!, didSignInFor user: GIDGoogleUser!, withError error: Error!) {
        if let error = error {
            print(error.localizedDescription)
            return
        }
        guard let authentication = user.authentication
            else {
                return
        }
        let credential = GoogleAuthProvider.credential(withIDToken: authentication.idToken, accessToken: authentication.accessToken)
        Auth.auth().signInAndRetrieveData(with: credential, completion: {(user, error) in
            if let error = error {
                print(error.localizedDescription)
                return
            }
            print("User logged in with Google")
            
            //create a UIAlertController object
            let alert=UIAlertController(title: "Firebase", message: "Welcome to Firebase " + (user?.user.displayName)!, preferredStyle: UIAlertController.Style.alert)
            
            //create a UIAlertAction object for the button
            let okAction=UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: {action in
                //perform segue
                self.performSegue(withIdentifier: "showRecipes", sender: nil)
            })
            alert.addAction(okAction)
            self.present(alert, animated: true, completion: nil)
        })
    }
    
    

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        GIDSignIn.sharedInstance().delegate = self
        GIDSignIn.sharedInstance().uiDelegate = self
        
        let googleSignInButton = GIDSignInButton()
        googleSignInButton.style = .wide
        googleSignInButton.center = view.center
        view.addSubview(googleSignInButton)
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
