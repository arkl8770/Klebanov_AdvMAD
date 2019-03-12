//
//  FoodDataController.swift
//  fitnessTrackerPro
//
//  Created by Ari Klebanov on 3/7/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import Foundation
import RealmSwift

class FoodDataController {
    var myRealm : Realm!
    var foodData : Results<Food>
    {
        get {
            return myRealm.objects(Food.self)
        }
    }
    
    func dbSetup() {
        do {
            myRealm = try Realm()
        } catch let error{
            print(error.localizedDescription)
        }
        print(Realm.Configuration.defaultConfiguration.fileURL!)
    }
    
    func getFood() -> [Food]{
        return Array(foodData)
    }
    
    func addItem(newItem : Food) {
        do {
            try self.myRealm.write({
                self.myRealm.add(newItem)
            })
        } catch let error {
            print(error.localizedDescription)
        }
    }
    
    func editItem(item : Food, newItem : Food) {
        do {
            try self.myRealm.write({
                item.name = newItem.name
                item.calories = newItem.calories
                item.timeEaten = newItem.timeEaten
            })
        } catch let error {
            print(error.localizedDescription)
        }
    }
    
    func deleteItem(item : Food) {
        do {
            try self.myRealm.write({
                self.myRealm.delete(item)
            })
        } catch let error {
            print(error.localizedDescription)
        }
    }
}
