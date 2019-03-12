//
//  FoodViewController.swift
//  fitnessTrackerPro
//
//  Created by Ari Klebanov on 3/6/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import UIKit

class FoodViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var tableView: UITableView!
    
    var addedFoodName = String()
    var addedFoodCalories = Int()
    var addedFoodTime = String()
    
    var totalCalories = Int()
    
    var date = String()
    
    var foodData = FoodDataController()
    var foodList = [Food]()
    
    func render() {
        foodList = foodData.getFood()
        titleLabel.text! = "You've consumed " + String(totalCalories) + " Calories so far."
        tableView.reloadData()
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        foodData.dbSetup()
        foodList = foodData.getFood()
        for i in foodList {
            totalCalories = totalCalories + i.calories
        }
        titleLabel.text! = "You've consumed " + String(totalCalories) + " Calories today."
        print(String(totalCalories))
    }
    
     func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }
    
     func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return foodList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath)
        let item = foodList[indexPath.row]
        let currentDate = Date()
        let dateFormatter = DateFormatter()
        dateFormatter.setLocalizedDateFormatFromTemplate("MMd")
        date = dateFormatter.string(from: currentDate)
        //print(date)
        cell.textLabel!.text = date + " @ " + item.timeEaten + "  -  " + item.name + " (" + String(item.calories) + "C)"
        return cell
    }
    
    func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        return true
    }
    
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            let item = foodList[indexPath.row]
            totalCalories = totalCalories - item.calories
            foodData.deleteItem(item: item)
            print(String(totalCalories))
            render()
        }
    }
    
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
     // Get the new view controller using segue.destination.
     // Pass the selected object to the new view controller.
        if segue.identifier == "foodDetail" {
            let detailVC = segue.destination as! DetailViewController
            let indexPath = tableView.indexPath(for: sender as! UITableViewCell)!
            detailVC.label1 = "Name:"
            detailVC.label2 = "Calories:"
            detailVC.label3 = "Time Eaten:"
            detailVC.textfield1 = foodList[indexPath.row].name
            detailVC.textfield2 = String(foodList[indexPath.row].calories)
            detailVC.textfield3 = foodList[indexPath.row].timeEaten
            detailVC.title = foodList[indexPath.row].name
            detailVC.selectedEntry = indexPath.row
            detailVC.oldFoodItem = foodList[indexPath.row]
        }
     }
    
    @IBAction func unwindSegue (_ segue:UIStoryboardSegue){
        if segue.identifier == "doneSegue" {
            let source = segue.source as! AddFoodViewController
            if (source.addedFoodNameField.text!.isEmpty == false) && (source.addedFoodCaloriesField.text!.isEmpty == false) && (source.addedFoodTimeField.text!.isEmpty == false){
                addedFoodName = source.addedFoodNameField.text!
                addedFoodCalories = Int(source.addedFoodCaloriesField.text!)!
                addedFoodTime = source.addedFoodTimeField.text!
                let newFoodItem = Food()
                newFoodItem.name = addedFoodName
                newFoodItem.calories = addedFoodCalories
                newFoodItem.timeEaten = addedFoodTime
                totalCalories = totalCalories + addedFoodCalories
                print(newFoodItem.name)
                print(String(newFoodItem.calories))
                print(newFoodItem.timeEaten)
                self.foodData.addItem(newItem: newFoodItem)
                self.render()
            }
        }
        
        else if segue.identifier == "saveSegue" {
            let source = segue.source as! DetailViewController
            if (source.textField1.text! != source.textfield1) || (source.textField2.text! != source.textfield2) ||  (source.textField3.text! != source.textfield3) {
                let newItem = Food()
                let oldItem = source.oldFoodItem
                newItem.name = source.textField1.text!
                newItem.calories = Int(source.textField2.text!)!
                newItem.timeEaten = source.textField3.text!
                totalCalories = totalCalories - oldItem.calories + newItem.calories
                self.foodData.editItem(item: oldItem, newItem: newItem)
                self.render()
            }
        }
    }
     

}
