//
//  Legend.swift
//  RocknRoll_SplitView
//
//  Created by Ari Klebanov on 2/26/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import Foundation

struct Legend: Decodable {
    let name : String
    let url : String
}

class LegendDataModelController {
    var allData = [Legend]()
    let filename = "rocknroll"
    
    func loadData() {
        if let pathURL = Bundle.main.url(forResource: filename, withExtension: "plist") {
            let plistdecoder = PropertyListDecoder()
            do {
                let data = try Data(contentsOf: pathURL)
                allData = try plistdecoder.decode([Legend].self, from: data)
            } catch {
                print(error)
            }
        }
    }
    
    func getLegends() -> [String] {
        var legends = [String]()
        for legend in allData {
            legends.append(legend.name)
        }
        return legends
    }
    
    func getURL(index: Int) -> String {
        return allData[index].url
    }
}
