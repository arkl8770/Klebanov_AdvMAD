//
//  Sleep.swift
//  fitnessTrackerPro
//
//  Created by Ari Klebanov on 3/10/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import Foundation
import RealmSwift

class Sleep : Object {
    @objc dynamic var timeOfSleep = ""
    @objc dynamic var rating = Int()
    @objc dynamic var timeofAwake = ""
}

