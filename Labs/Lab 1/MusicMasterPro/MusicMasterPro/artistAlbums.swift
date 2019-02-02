//
//  artistAlbums.swift
//  MusicMasterPro
//
//  Created by Ari Klebanov on 1/31/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import Foundation

struct artistAlbums : Decodable {
    let name : String
    let albums : [String]
}
