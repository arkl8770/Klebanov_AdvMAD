//
//  artistAlbumsController.swift
//  MusicMasterPro
//
//  Created by Ari Klebanov on 1/31/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import Foundation

class ArtistAlbumsController {
    var allData = [artistAlbums]()
    let filename = "artistAlbums"
    
    func loadData() {
        if let pathURL = Bundle.main.url(forResource: filename, withExtension: "plist"){
            //creates a property list decoder
            let plistdecoder = PropertyListDecoder()
            do {
                let data = try Data(contentsOf: pathURL)
                //decodes the property list
                allData = try plistdecoder.decode([artistAlbums].self, from: data)
            } catch{
                print(error)
            }
        }
    }
    func getArtists() -> [String] {
        var artists = [String]()
        for artist in allData {
            artists.append(artist.name)
        }
        return artists
    }
    func getAlbums(index:Int) -> [String] {
        return allData[index].albums
    }
}
