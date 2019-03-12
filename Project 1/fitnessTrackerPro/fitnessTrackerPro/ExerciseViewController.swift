//
//  ExerciseViewController.swift
//  fitnessTrackerPro
//
//  Created by Ari Klebanov on 3/10/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import UIKit

class ExerciseViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var titleLabel: UILabel!
    
    var addedExerciseName = String()
    var addedExerciseDuration = Int()
    var addedExerciseTime = String()
    
    var date = String()
    
    var totalExercise = Int()
    
    var exerciseData = ExerciseDataController()
    var exerciseList = [Exercise]()
    
    func render() {
        exerciseList = exerciseData.getExercises()
        titleLabel.text! = "You've spent " + String(totalExercise) + " minutes working out so far."
        tableView.reloadData()
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        exerciseData.dbSetup()
        exerciseList = exerciseData.getExercises()
        for i in exerciseList {
            totalExercise = totalExercise + i.duration
        }
        titleLabel.text! = "You've spent " + String(totalExercise) + " minutes working out so far."
        print(String(totalExercise))
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return exerciseList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath)
        let item = exerciseList[indexPath.row]
        let currentDate = Date()
        let dateFormatter = DateFormatter()
        dateFormatter.setLocalizedDateFormatFromTemplate("MMd")
        date = dateFormatter.string(from: currentDate)
        //print(date)
        cell.textLabel!.text = date + " @ " + item.timeStarted + "  -  " + item.name + " (" + String(item.duration) + "min)"
        return cell
    }
    
    func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        return true
    }
    
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            let item = exerciseList[indexPath.row]
            totalExercise = totalExercise - item.duration
            exerciseData.deleteItem(item: item)
            print(String(totalExercise))
            render()
        } 
    }
    
    // MARK: - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
        if segue.identifier == "exerciseDetail" {
            let detailVC = segue.destination as! DetailViewController
            let indexPath = tableView.indexPath(for: sender as! UITableViewCell)!
            detailVC.label1 = "Name:"
            detailVC.label2 = "Duration (min):"
            detailVC.label3 = "Time Started:"
            detailVC.textfield1 = exerciseList[indexPath.row].name
            detailVC.textfield2 = String(exerciseList[indexPath.row].duration)
            detailVC.textfield3 = exerciseList[indexPath.row].timeStarted
            detailVC.title = exerciseList[indexPath.row].name
            detailVC.selectedEntry = indexPath.row
            detailVC.oldExerciseItem = exerciseList[indexPath.row]
        }
    }
    
    @IBAction func unwindSegue (_ segue:UIStoryboardSegue){
        if segue.identifier == "doneSegue" {
            let source = segue.source as! AddExerciseViewController
            if (source.addedExerciseNameField.text!.isEmpty == false) && (source.addedExerciseDurationField.text!.isEmpty == false) && (source.addedExerciseTimeField.text!.isEmpty == false){
                addedExerciseName = source.addedExerciseNameField.text!
                addedExerciseDuration = Int(source.addedExerciseDurationField.text!)!
                addedExerciseTime = source.addedExerciseTimeField.text!
                let newExerciseItem = Exercise()
                newExerciseItem.name = addedExerciseName
                newExerciseItem.duration = addedExerciseDuration
                newExerciseItem.timeStarted = addedExerciseTime
                totalExercise = totalExercise + addedExerciseDuration
                print(newExerciseItem.name)
                print(String(newExerciseItem.duration))
                print(newExerciseItem.timeStarted)
                self.exerciseData.addItem(newItem: newExerciseItem)
                self.render()
            }
        }
        
        else if segue.identifier == "saveSegue" {
            let source = segue.source as! DetailViewController
            if (source.textField1.text! != source.textfield1) || (source.textField2.text! != source.textfield2) ||  (source.textField3.text! != source.textfield3) {
                let newItem = Exercise()
                let oldItem = source.oldExerciseItem
                newItem.name = source.textField1.text!
                newItem.duration = Int(source.textField2.text!)!
                newItem.timeStarted = source.textField3.text!
                totalExercise = totalExercise - oldItem.duration + newItem.duration
                self.exerciseData.editItem(item: oldItem, newItem: newItem)
                self.render()
            }
        }
    }

}
