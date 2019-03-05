//
//  HeadlineDataController.swift
//  CNNHeadlines_JSON
//
//  Created by Ari Klebanov on 3/4/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import Foundation

class HeadlineDataController {
    var headlineData = HeadlineData()
    var onDataUpdate: ((_ data: [Headline]) -> Void)?
    
    func loadJSON() {
        let urlPath = "https://newsapi.org/v2/everything?sources=cnn&apiKey=78bc13f4fcf14d2091a7a6683c203f18"
        guard let url = URL(string: urlPath)
            else {
                print("url error")
                return
        }
        let session = URLSession.shared.dataTask(with: url, completionHandler: {(data, response, error) in
            let httpResponse = response as! HTTPURLResponse
            let statusCode = httpResponse.statusCode
            guard statusCode == 200
                else {
                    print("file download error")
                    return
            }
            print("download successful")
            DispatchQueue.main.async {self.parseJSON(data!)}
        })
        session.resume()
    }
    
    func parseJSON(_ data: Data) {
        do {
            
            let api = try JSONDecoder().decode(HeadlineData.self, from: data)
            print(api)
            for headline in api.articles {
                headlineData.articles.append(headline)            }
        } catch let jsonErr{
            print("JSON error")
            print(jsonErr.localizedDescription)
            return
        }
        print("parseJSON done")
        onDataUpdate?(headlineData.articles)
    }
    
    func getHeadlines() -> [Headline] {
        return headlineData.articles
    }
}
