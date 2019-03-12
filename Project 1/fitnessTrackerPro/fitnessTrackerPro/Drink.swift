//
//  Drink.swift
//  fitnessTrackerPro
//
//  Created by Ari Klebanov on 3/10/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import Foundation
import RealmSwift

class Drink : Object {
    @objc dynamic var name = ""
    @objc dynamic var volume = Int()
    @objc dynamic var timeDrank = ""
}
