//
//  Exercise.swift
//  fitnessTrackerPro
//
//  Created by Ari Klebanov on 3/10/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import Foundation
import RealmSwift

class Exercise : Object {
    @objc dynamic var name = ""
    @objc dynamic var duration = Int()
    @objc dynamic var timeStarted = ""
}
