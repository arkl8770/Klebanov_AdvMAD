//
//  RecipeTableViewController.swift
//  Recipes_FirebaseDemo
//
//  Created by Ari Klebanov on 2/28/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import UIKit

class RecipeTableViewController: UITableViewController {
    
    var recipes = [Recipe]()
    var recipeData = RecipeDataController()
    
    func render() {
        recipes = recipeData.getRecipes()
        tableView.reloadData()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
        
        recipeData.onDataUpdate = {[weak self] (data: [Recipe]) in self?.render()}
        recipeData.setupFirebaseListener()
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return recipes.count
    }
    
    @IBAction func unwindSegue(segue:UIStoryboardSegue) {
        if segue.identifier == "saveSegue" {
            let source = segue.source as! AddViewController
            if source.addedRecipe.isEmpty == false {
                recipeData.addRecipe(name: source.addedRecipe, url: source.addedUrl)
            }
        }
         
    }

    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "recipeCell", for: indexPath)
        let recipe = recipes[indexPath.row]
        cell.textLabel!.text = recipe.name
        // Configure the cell...

        return cell
    }
    

    
    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    

    
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            let recipeID = recipes[indexPath.row].id
            recipeData.deleteRecipe(recipeID: recipeID)
            //tableView.deleteRows(at: [indexPath], with: .fade)
        }  
    }
    

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

    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
        if segue.identifier == "showDetail" {
            let detailVC = segue.destination as! WebViewController
            let indexPath = tableView.indexPath(for: sender as! UITableViewCell)!
            let recipe = recipes[indexPath.row]
            detailVC.title = recipe.name
            detailVC.webPage = recipe.url
            
        }
    }
    

}
