//
//  Movie.swift
//  Midterm_Movies
//
//  Created by Ari Klebanov on 3/14/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import Foundation

struct Movie : Codable {
    let name : String
    let url : String
}

class MovieDataController {
    var allData = [Movie]()
    let filename = "movies"
    
    func loadData() {
        if let pathURL = Bundle.main.url(forResource: filename, withExtension: "plist") {
            let plistdecoder = PropertyListDecoder()
            do {
                let data = try Data(contentsOf: pathURL)
                allData = try plistdecoder.decode([Movie].self, from: data)
            } catch {
                print(error)
            }
        }
    }
    
    func getMovies() -> [String] {
        var movies = [String]()
        for movie in allData {
            movies.append(movie.name)
        }
        return movies
    }
    
    func getURL(index:Int) -> String {
        return allData[index].url
    }
    
    func addMovie(name: String, url: String) {
        allData.append(Movie(name: name, url: url))
    }
    
}

