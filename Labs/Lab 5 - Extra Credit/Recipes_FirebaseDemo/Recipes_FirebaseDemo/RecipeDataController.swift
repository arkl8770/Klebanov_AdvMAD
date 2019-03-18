//
//  RecipeDataController.swift
//  Recipes_FirebaseDemo
//
//  Created by Ari Klebanov on 2/28/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import Foundation
import Firebase

class RecipeDataController {
    var ref : DatabaseReference!
    var recipeData = [Recipe]()
    
    var onDataUpdate: ((_ data: [Recipe]) -> Void)?
    
    func setupFirebaseListener() {
        ref = Database.database().reference().child("recipes")
        ref.observe(DataEventType.value, with: {snapshot in
            self.recipeData.removeAll()
            for snap in snapshot.children.allObjects as! [DataSnapshot] {
                let recipeID = snap.key
                if let recipeDict = snap.value as? [String:String], let recipeName = recipeDict["name"], let recipeUrl = recipeDict["url"] {
                    let newRecipe = Recipe(id: recipeID, name: recipeName, url: recipeUrl)
                    self.recipeData.append(newRecipe)
                }
                
            }
            
            self.onDataUpdate?(self.recipeData)
        })
    }
    
    func getRecipes() -> [Recipe] {
        return recipeData
    }
    
    func addRecipe(name: String, url: String) {
        let newRecipeDict = ["name": name, "url": url]
        let recipeRef = ref.childByAutoId()
        recipeRef.setValue(newRecipeDict)
    }
    
    func deleteRecipe(recipeID: String) {
        ref.child(recipeID).removeValue()
    }
}
