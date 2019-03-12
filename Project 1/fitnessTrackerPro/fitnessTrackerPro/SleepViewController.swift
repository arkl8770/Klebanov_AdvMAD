//
//  SleepViewController.swift
//  fitnessTrackerPro
//
//  Created by Ari Klebanov on 3/10/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import UIKit

class SleepViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var titleLabel: UILabel!
    
    var data = ["16", "17", "18", "19", "20"]
    
    var addedSleepStart = String()
    var addedSleepEnd = String()
    var addedSleepRating = Int()
    
    var totalSleep = Double()
    
    var date = String()
    
    var sleepData = SleepDataController()
    var sleepList = [Sleep]()
    
    func convertStringstoInterval(startString : String, endString : String) -> Double  {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "hh:mm a"
        let startTime = dateFormatter.date(from: startString)
        let endTime = dateFormatter.date(from: endString)
        let interval = endTime!.timeIntervalSince(startTime!)
        var hours = interval/3600
        if hours <= 0 {
            hours = hours + 24
        }
        print(String(hours))
        return hours
    }
    
    func render() {
        sleepList = sleepData.getSleep()
        titleLabel.text! = "You've slept for " + String(Int(round(totalSleep))) + " hours so far."
        tableView.reloadData()
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        sleepData.dbSetup()
        sleepList = sleepData.getSleep()
        for i in sleepList {
            totalSleep = totalSleep + convertStringstoInterval(startString: i.timeOfSleep, endString: i.timeofAwake)
        }
        titleLabel.text! = "You've slept for " + String(Int(round(totalSleep))) + " hours so far."
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return sleepList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath)
        let item = sleepList[indexPath.row]
        let currentDate = Date()
        let dateFormatter = DateFormatter()
        dateFormatter.setLocalizedDateFormatFromTemplate("MMd")
        date = dateFormatter.string(from: currentDate)
        //print(date)
        cell.textLabel!.text = date + "  -  " + item.timeOfSleep + " to " + item.timeofAwake + "  -  " + String(item.rating) + "/10"
        return cell
    }
    
    func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        return true
    }
    
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            let item = sleepList[indexPath.row]
            totalSleep = totalSleep - convertStringstoInterval(startString: item.timeOfSleep, endString: item.timeofAwake)
            sleepData.deleteItem(item: item)
            //print(String(totalExercise))
            render()
        } 
    }
    

    // MARK: - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
        if segue.identifier == "sleepDetail" {
            let detailVC = segue.destination as! DetailViewController
            let indexPath = tableView.indexPath(for: sender as! UITableViewCell)!
            detailVC.label1 = "Start time:"
            detailVC.label2 = "End time:"
            detailVC.label3 = "Rating (0-10):"
            detailVC.textfield1 = sleepList[indexPath.row].timeOfSleep
            detailVC.textfield2 = sleepList[indexPath.row].timeofAwake
            detailVC.textfield3 = String(sleepList[indexPath.row].rating)
            detailVC.title = sleepList[indexPath.row].timeOfSleep + " - " + sleepList[indexPath.row].timeofAwake
            detailVC.selectedEntry = indexPath.row
            detailVC.oldSleepItem = sleepList[indexPath.row]
            
        }
    }
    @IBAction func unwindSegue (_ segue:UIStoryboardSegue){
        if segue.identifier == "doneSegue" {
            let source = segue.source as! AddSleepViewController
            if (source.addedSleepStartField.text!.isEmpty == false) && (source.addedSleepEndField.text!.isEmpty == false) && (source.addedSleepRatingField.text!.isEmpty == false){
                addedSleepStart = source.addedSleepStartField.text!
                addedSleepRating = Int(source.addedSleepRatingField.text!)!
                addedSleepEnd = source.addedSleepEndField.text!
                let newSleepItem = Sleep()
                newSleepItem.timeOfSleep = addedSleepStart
                newSleepItem.rating = addedSleepRating
                newSleepItem.timeofAwake = addedSleepEnd
                let sleepInterval = convertStringstoInterval(startString: addedSleepStart, endString: addedSleepEnd)
                totalSleep = totalSleep + sleepInterval
                print(newSleepItem.timeOfSleep)
                print(String(newSleepItem.timeofAwake))
                print(newSleepItem.rating)
                self.sleepData.addItem(newItem: newSleepItem)
                self.render()
            }
        }
        else if segue.identifier == "saveSegue" {
            let source = segue.source as! DetailViewController
            if (source.textField1.text! != source.textfield1) || (source.textField2.text! != source.textfield2) ||  (source.textField3.text! != source.textfield3) {
                let newItem = Sleep()
                let oldItem = source.oldSleepItem
                newItem.timeOfSleep = source.textField1.text!
                newItem.timeofAwake = source.textField2.text!
                newItem.rating = Int(source.textField3.text!)!
                totalSleep = totalSleep - convertStringstoInterval(startString: oldItem.timeOfSleep, endString: oldItem.timeofAwake) + convertStringstoInterval(startString: newItem.timeOfSleep, endString: newItem.timeofAwake)
                self.sleepData.editItem(item: oldItem, newItem: newItem)
                self.render()
            }
        }
        
    }

}
