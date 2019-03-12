//
//  Food.swift
//  fitnessTrackerPro
//
//  Created by Ari Klebanov on 3/7/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import Foundation
import RealmSwift

class Food : Object {
    @objc dynamic var name = ""
    @objc dynamic var calories = Int()
    @objc dynamic var timeEaten = ""
}
