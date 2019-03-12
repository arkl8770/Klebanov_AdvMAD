//
//  DrinkViewController.swift
//  fitnessTrackerPro
//
//  Created by Ari Klebanov on 3/6/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import UIKit

class DrinkViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var titleLabel: UILabel!
    
    var addedDrinkName = String()
    var addedDrinkVolume = Int()
    var addedDrinkTime = String()
    
    var totalVolume = Int()
    
    var date = String()
    
    var drinkData = DrinkDataController()
    var drinkList = [Drink]()
    
    func render() {
        drinkList = drinkData.getDrinks()
        titleLabel.text! = "You've drank " + String(totalVolume) + " ounces of fluids today."
        tableView.reloadData()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        drinkData.dbSetup()
        drinkList = drinkData.getDrinks()
        for i in drinkList {
            totalVolume = totalVolume + i.volume
        }
        titleLabel.text! = "You've drank " + String(totalVolume) + " ounces of fluids today."
        print(String(totalVolume))
    }

    func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return drinkList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath)
        let item = drinkList[indexPath.row]
        let currentDate = Date()
        let dateFormatter = DateFormatter()
        dateFormatter.setLocalizedDateFormatFromTemplate("MMd")
        date = dateFormatter.string(from: currentDate)
        //print(date)
        cell.textLabel!.text = date + " @ " + item.timeDrank + "  -  " + item.name + " (" + String(item.volume) + "oz)"
        return cell
    }
    
    func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        return true
    }
    
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            let item = drinkList[indexPath.row]
            totalVolume = totalVolume - item.volume
            drinkData.deleteItem(item: item)
            print(String(totalVolume))
            render()
        } 
    }
    
    // MARK: - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
        if segue.identifier == "drinkDetail" {
            let detailVC = segue.destination as! DetailViewController
            let indexPath = tableView.indexPath(for: sender as! UITableViewCell)!
            detailVC.label1 = "Name:"
            detailVC.label2 = "Volume (oz):"
            detailVC.label3 = "Time Drank:"
            detailVC.textfield1 = drinkList[indexPath.row].name
            detailVC.textfield2 = String(drinkList[indexPath.row].volume)
            detailVC.textfield3 = drinkList[indexPath.row].timeDrank
            detailVC.title = drinkList[indexPath.row].name
            detailVC.selectedEntry = indexPath.row
            detailVC.oldDrinkItem = drinkList[indexPath.row]
        }
    }
    
    @IBAction func unwindSegue (_ segue:UIStoryboardSegue){
        if segue.identifier == "doneSegue" {
            let source = segue.source as! AddDrinkViewController
            if (source.addedDrinkNameField.text!.isEmpty == false) && (source.addedDrinkVolumeField.text!.isEmpty == false) && (source.addedDrinkTimeField.text!.isEmpty == false){
                addedDrinkName = source.addedDrinkNameField.text!
                addedDrinkVolume = Int(source.addedDrinkVolumeField.text!)!
                addedDrinkTime = source.addedDrinkTimeField.text!
                let newDrinkItem = Drink()
                newDrinkItem.name = addedDrinkName
                newDrinkItem.volume = addedDrinkVolume
                newDrinkItem.timeDrank = addedDrinkTime
                totalVolume = totalVolume + addedDrinkVolume
                print(newDrinkItem.name)
                print(String(newDrinkItem.volume))
                print(newDrinkItem.timeDrank)
                self.drinkData.addItem(newItem: newDrinkItem)
                self.render()
            }
        }
        
        else if segue.identifier == "saveSegue" {
            let source = segue.source as! DetailViewController
            if (source.textField1.text! != source.textfield1) || (source.textField2.text! != source.textfield2) ||  (source.textField3.text! != source.textfield3) {
                let newItem = Drink()
                let oldItem = source.oldDrinkItem
                newItem.name = source.textField1.text!
                newItem.volume = Int(source.textField2.text!)!
                newItem.timeDrank = source.textField3.text!
                totalVolume = totalVolume - oldItem.volume + newItem.volume
                self.drinkData.editItem(item: oldItem, newItem: newItem)
                self.render()
            }
        }
    }
    

}

