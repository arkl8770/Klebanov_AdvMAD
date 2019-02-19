//
//  DataController.swift
//  Shoe Rack Pro
//
//  Created by Ari Klebanov on 2/18/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import Foundation




struct Shoes : Codable {
    let style : String
    var shoes : [String]
}

class DataController {
    var allData = [Shoes]()
    let fileName = "shoeList"
    
    func loadData(){
        if let pathURL = Bundle.main.url(forResource: fileName, withExtension: "plist"){
            //creates a property list decoder object
            let plistdecoder = PropertyListDecoder()
            do {
                let data = try Data(contentsOf: pathURL)
                //decodes the property list
                allData = try plistdecoder.decode([Shoes].self, from: data)
            } catch {
                // handle error
                print(error)
            }
        }
    }
    
    func getShoes() -> [Shoes]{
        return allData
    }
    
    func getStyles()->[String]{
        var styles = [String]()
        for style in allData{
            styles.append(style.style)
        }
        return styles
    }
    func addShoe(index:Int, newShoe:String, newIndex:Int) {
        allData[index].shoes.insert(newShoe, at: newIndex)
    }
    func deleteShoe(index:Int, badIndex:Int) {
        allData[index].shoes.remove(at: badIndex)
    }
    
    
}
