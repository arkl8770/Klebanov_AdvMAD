//
//  FirstViewController.swift
//  MusicMasterPro
//
//  Created by Ari Klebanov on 1/31/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import UIKit

class FirstViewController: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource {
    
    @IBOutlet weak var musicPicker: UIPickerView!
    @IBOutlet weak var resultLabel: UILabel!
    let artistComponent = 0
    let albumComponent = 1
    
    var artistAlbumsController = ArtistAlbumsController()
    var artists = [String]()
    var albums = [String]()
    
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 2
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        if component == artistComponent {
            return artists.count
            
        }else {
            return albums.count
        }
    }
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if component == artistComponent {
            return artists[row]
        } else {
            return albums[row]
        }
    }
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        if component == artistComponent {
            albums = artistAlbumsController.getAlbums(index: row)
            musicPicker.reloadComponent(albumComponent)
            musicPicker.selectRow(0, inComponent: albumComponent, animated: true)
        }
        let artistRow = pickerView.selectedRow(inComponent: artistComponent)
        let albumRow = pickerView.selectedRow(inComponent: albumComponent)
        resultLabel.text = "You should listen to \(albums[albumRow]) by \(artists[artistRow])"
    }
    

    override func viewDidLoad() {
        super.viewDidLoad()
        
        artistAlbumsController.loadData()
        artists = artistAlbumsController.getArtists()
        albums = artistAlbumsController.getAlbums(index: 0)
        // Do any additional setup after loading the view, typically from a nib.
    }


}

