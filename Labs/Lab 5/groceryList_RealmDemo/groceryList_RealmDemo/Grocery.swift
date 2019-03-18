//
//  Grocery.swift
//  groceryList_RealmDemo
//
//  Created by Ari Klebanov on 3/5/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import Foundation
import RealmSwift

class Grocery : Object {
    @objc dynamic var name = ""
    @objc dynamic var bought = false
}
