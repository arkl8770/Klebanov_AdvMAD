//
//  ExerciseDataController.swift
//  fitnessTrackerPro
//
//  Created by Ari Klebanov on 3/10/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import Foundation
import RealmSwift

class ExerciseDataController {
    var myRealm : Realm!
    var exerciseData : Results<Exercise>
    {
        get {
            return myRealm.objects(Exercise.self)
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
    
    func getExercises() -> [Exercise] {
        return Array(exerciseData)
    }
    
    func addItem(newItem : Exercise) {
        do {
            try self.myRealm.write({
                self.myRealm.add(newItem)
            })
        } catch let error {
            print(error.localizedDescription)
        }
    }
    
    func editItem(item : Exercise, newItem : Exercise) {
        do {
            try self.myRealm.write({
                item.name = newItem.name
                item.duration = newItem.duration
                item.timeStarted = newItem.timeStarted
            })
        } catch let error {
            print(error.localizedDescription)
        }
    }
    
    func deleteItem(item : Exercise) {
        do {
            try self.myRealm.write({
                self.myRealm.delete(item)
            })
        } catch let error {
            print(error.localizedDescription)
        }
    }
}
