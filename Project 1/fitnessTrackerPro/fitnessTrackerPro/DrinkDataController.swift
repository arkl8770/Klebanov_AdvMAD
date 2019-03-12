//
//  DrinkDataController.swift
//  fitnessTrackerPro
//
//  Created by Ari Klebanov on 3/10/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import Foundation
import RealmSwift

class DrinkDataController {
    var myRealm : Realm!
    var drinkData : Results<Drink>
    {
        get {
            return myRealm.objects(Drink.self)
        }
    }
    
    func dbSetup() {
        do {
            myRealm = try Realm()
        } catch let error {
            print(error.localizedDescription)
        }
        print(Realm.Configuration.defaultConfiguration.fileURL!)
    }
    
    func getDrinks() -> [Drink] {
        return Array(drinkData)
    }
    
    func addItem(newItem : Drink) {
        do {
            try self.myRealm.write({
                self.myRealm.add(newItem)
            })
        } catch let error {
            print(error.localizedDescription)
        }
    }
    
    func editItem(item : Drink, newItem : Drink) {
        do {
            try self.myRealm.write({
                item.name = newItem.name
                item.volume = newItem.volume
                item.timeDrank = newItem.timeDrank
            })
        } catch let error {
            print(error.localizedDescription)
        }
    }
    
    func deleteItem(item : Drink) {
        do {
            try self.myRealm.write({
                self.myRealm.delete(item)
            })
        } catch let error {
            print(error.localizedDescription)
        }
    }
}
