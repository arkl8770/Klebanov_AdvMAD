//
//  SearchResultsController.swift
//  Shoe Rack Pro
//
//  Created by Ari Klebanov on 2/18/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import UIKit

class SearchResultsController: UITableViewController, UISearchResultsUpdating {
    
    var allShoes = [Shoes]()
    var styles = [String]()
    var filteredShoes = [String]()

    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableView.register(UITableViewCell.self, forCellReuseIdentifier: "ShoeIdentifier")


        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
    }

    // MARK: - Table view data source
    
    //UISearchResultsUpdating protocol required method to implement the search
    func updateSearchResults(for searchController: UISearchController) {
        let searchString = searchController.searchBar.text //search string
        filteredShoes.removeAll(keepingCapacity: true) //removes all elements
        if searchString?.isEmpty == false {
            //closure that will be called for each word to see if it matches the search string
            let filter: (String) -> Bool = { name in
                //look for the search string as a substring of the word
                let range = name.range(of: searchString!, options: .caseInsensitive)
                return range != nil //returns true if the value matches and false if there’s no match
            }
            //iterate over all the shoes
            for item in allShoes {
                let shoesForLetter = item.shoes //array of shoes for each letter
                let matches = shoesForLetter.filter(filter) //filter using the closure
                filteredShoes.append(contentsOf: matches) //add shoes that match
            }
        }
        tableView.reloadData() //reload table data with search results
    }

    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return filteredShoes.count
    }

    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "ShoeIdentifier", for: indexPath)

        // Configure the cell...
        cell.textLabel?.text = filteredShoes[indexPath.row]
        return cell
    }
    

    /*
    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
