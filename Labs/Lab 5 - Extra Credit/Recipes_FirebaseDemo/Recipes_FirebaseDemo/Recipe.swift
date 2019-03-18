//
//  Recipe.swift
//  Recipes_FirebaseDemo
//
//  Created by Ari Klebanov on 2/28/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import Foundation

struct Recipe {
    var id: String
    var name: String
    var url: String
    
    init(id: String, name: String, url: String){
        self.id = id
        self.name = name
        self.url = url
    }
}
