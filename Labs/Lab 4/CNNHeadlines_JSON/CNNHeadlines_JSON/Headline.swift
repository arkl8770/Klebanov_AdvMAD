//
//  Headline.swift
//  CNNHeadlines_JSON
//
//  Created by Ari Klebanov on 3/4/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import Foundation

struct Headline : Decodable {
    let title : String
    let author : String?
    let url : String
}

struct HeadlineData: Decodable {
    var articles = [Headline]()
}
