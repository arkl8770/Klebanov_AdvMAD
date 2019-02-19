//
//  ViewController.swift
//  Shoe Rack Pro
//
//  Created by Ari Klebanov on 2/18/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import UIKit

class TableViewController: UITableViewController {
    
    var allShoes = [Shoes]()
    var styles = [String]()
    var data = DataController()
    var searchController = UISearchController()
    var selectedSection = 0
    var shoeList = [String]()
    
    @IBAction func unwindSegue(_ segue:UIStoryboardSegue) {
        if segue.identifier == "doneSegue" {
            let source = segue.source as! AddShoeViewController
            if (source.newShoe.isEmpty) == false {
                data.addShoe(index: selectedSection, newShoe: source.newShoe, newIndex: shoeList.count)
            }
            shoeList.append(source.newShoe)
            tableView.reloadData()
        }
    }
    
    
    override func viewWillAppear(_ animated: Bool) {
    }
    

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        data.loadData()
        allShoes = data.getShoes()
        styles = data.getStyles()
        
        let resultsController = SearchResultsController()
        resultsController.allShoes = allShoes
        resultsController.styles = styles
        searchController = UISearchController(searchResultsController: resultsController)
        searchController.searchBar.placeholder = "Enter a search term"
        tableView.tableHeaderView = searchController.searchBar
        searchController.searchResultsUpdater = resultsController
        navigationController?.navigationBar.prefersLargeTitles = true
        //self.navigationItem.rightBarButtonItem = self.editButtonItem
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return allShoes[section].shoes.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let section = indexPath.section
        let shoesSection = allShoes[section].shoes
        //configure the cell
        let cell = tableView.dequeueReusableCell(withIdentifier: "ShoeIdentifier", for: indexPath)
            cell.textLabel?.text = shoesSection[indexPath.row]
        return cell
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        //let section = indexPath.section
        //let shoesSection = allShoes[section].shoes
        tableView.deselectRow(at: indexPath, animated: true) //deselects the row that had been choosen
    }
    
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return styles.count
    }
    
    override func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        //tableView.headerView(forSection: section)?.textLabel?.textAlignment = NSTextAlignment.center
        return styles[section]
    }
    
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        return true
    }
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            allShoes[indexPath.section].shoes.remove(at: indexPath.row)
            data.deleteShoe(index: indexPath.section, badIndex: indexPath.row)
            tableView.deleteRows(at: [indexPath], with: .fade)
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "detailSegue" {
            let detailViewController = segue.destination as! DetailViewController
            let indexPath = tableView.indexPath(for: sender as! UITableViewCell)!
            
            let section = indexPath.section
//            let shoesSection = allShoes[section].shoes
            
//            detailViewController.title = shoesSection[indexPath.row]
            detailViewController.title = allShoes[section].style
            detailViewController.data = data
            detailViewController.selectedStyle = allShoes[section].style
            detailViewController.selectedShoe =  allShoes[section].shoes[indexPath.row]
            
        }
    }
    
}

