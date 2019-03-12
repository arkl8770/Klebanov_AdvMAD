//
//  SleepDataController.swift
//  fitnessTrackerPro
//
//  Created by Ari Klebanov on 3/10/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import Foundation
import RealmSwift

class SleepDataController {
    var myRealm : Realm!
    var sleepData : Results<Sleep>
    {
        get {
            return myRealm.objects(Sleep.self)
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
    
    func getSleep() -> [Sleep]{
        return Array(sleepData)
    }
    
    func addItem(newItem : Sleep) {
        do {
            try self.myRealm.write({
                self.myRealm.add(newItem)
            })
        } catch let error {
            print(error.localizedDescription)
        }
    }
    
    func editItem(item : Sleep, newItem : Sleep) {
        do {
            try self.myRealm.write({
                item.timeOfSleep = newItem.timeOfSleep
                item.timeofAwake = newItem.timeofAwake
                item.rating = newItem.rating
            })
        } catch let error {
            print(error.localizedDescription)
        }
    }
    
    func deleteItem(item : Sleep) {
        do {
            try self.myRealm.write({
                self.myRealm.delete(item)
            })
        } catch let error {
            print(error.localizedDescription)
        }
    }
}
