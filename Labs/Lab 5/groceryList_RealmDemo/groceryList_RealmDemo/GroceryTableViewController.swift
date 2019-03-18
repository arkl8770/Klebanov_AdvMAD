//
//  GroceryTableViewController.swift
//  
//
//  Created by Ari Klebanov on 3/5/19.
//

import UIKit

class GroceryTableViewController: UITableViewController {
    
    var groceryData = GroceryDataController()
    
    var groceryList = [Grocery]()
    
    func render() {
        groceryList = groceryData.getGroceries()
        tableView.reloadData()
    }
    @IBAction func addItem(_ sender: UIBarButtonItem) {
        let addAlert = UIAlertController(title: "New Item", message: "Add a new item to your grocery list", preferredStyle: .alert)
        addAlert.addTextField(configurationHandler: {(UITextField) in})
        let cancelAction = UIAlertAction(title: "Cancel", style: .cancel, handler: nil)
        addAlert.addAction(cancelAction)
        let addItemAction = UIAlertAction(title: "Add", style: .default, handler: {(UIAlertAction) in
            let newItem = addAlert.textFields![0]
            let newGroceryItem = Grocery()
            newGroceryItem.name = newItem.text!
            newGroceryItem.bought = false
            self.groceryData.addItem(newItem: newGroceryItem)
        })
        addAlert.addAction(addItemAction)
        present(addAlert, animated: true, completion: nil)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        groceryData.onDataUpdate = {[weak self] (data:[Grocery]) in self?.render()}
        groceryData.dbSetup()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return groceryList.count
    }

    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath)

        // Configure the cell...
        let item = groceryList[indexPath.row]
        cell.textLabel!.text = item.name
        cell.accessoryType = item.bought ? .checkmark : .none //set checkmark if bought

        return cell
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let boughtItem = groceryList[indexPath.row]
        groceryData.boughtItem(item: boughtItem)
    }
    
    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    

    
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            let item = groceryList[indexPath.row]
            groceryData.deleteItem(item: item)
            // Delete the row from the data source
            //tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
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

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
