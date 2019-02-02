//
//  ThirdViewController.swift
//  MusicMasterPro
//
//  Created by Ari Klebanov on 2/1/19.
//  Copyright © 2019 Ari Klebanov. All rights reserved.
//

import UIKit
import AVFoundation

class ThirdViewController: UIViewController, AVAudioPlayerDelegate, AVAudioRecorderDelegate {
    
    func audioPlayerDidFinishPlaying(_ player: AVAudioPlayer, successfully flag: Bool) {
        recordButton.isEnabled = true
        pauseButton.isEnabled = false
    }

    @IBAction func playTrack(_ sender: UIButton) {
        //if not recording, play audio file
        if audioRecorder?.isRecording == false {
            pauseButton.isEnabled = true
            recordButton.isEnabled = false
            
            do{
                try audioPlayer = AVAudioPlayer(contentsOf: (audioRecorder?.url)!)
                audioPlayer!.delegate = self
                audioPlayer!.prepareToPlay() //prepares the audio playback by preloading its buffers
                audioPlayer!.play() //plays audio file
            } catch let error {
                    print("audioPlayer error: \(error.localizedDescription)")
            }
        }
    }
    @IBAction func pauseTrack(_ sender: UIButton) {
        pauseButton.isEnabled = false
        playButton.isEnabled = true
        recordButton.isEnabled = true
        //stop recording or playing
        if audioRecorder?.isRecording == true {
            audioRecorder?.stop()
        } else {
            audioPlayer?.stop()
        }
    }
    @IBAction func recordTrack(_ sender: UIButton) {
        //if not already recording, start recording
        if audioRecorder?.isRecording == false {
            playButton.isEnabled = false
            pauseButton.isEnabled = true
            audioRecorder?.delegate = self
            audioRecorder?.record()
        }
    }
    @IBOutlet weak var playButton: UIButton!
    @IBOutlet weak var pauseButton: UIButton!
    @IBOutlet weak var recordButton: UIButton!
    
    var audioPlayer : AVAudioPlayer?
    var audioRecorder : AVAudioRecorder?
    let filename = "audio.m4a"
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        playButton.isEnabled = false
        pauseButton.isEnabled = false
        
        //get path for audio file
        let dirPath = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        let docDir = dirPath[0] //documents directory
        print(docDir)
        let audioFileURL = docDir.appendingPathComponent(filename)
        print(audioFileURL)
        
        //the shared audio session instance
        let audioSession = AVAudioSession.sharedInstance()
        do {
            //sets the category for recording and playback of audio
            try audioSession.setCategory(AVAudioSession.Category.playAndRecord, mode: .default)
//          try audioSession.setCategory(AVAudioSessionCategoryPlayAndRecord)
        } catch {
            print("audio session error: \(error.localizedDescription)")
        }
        
        let settings = [
            AVFormatIDKey : Int(kAudioFormatMPEG4AAC), //specifies audio codec
            AVSampleRateKey: 12000, //sample rate in hertz
            AVNumberOfChannelsKey: 1, //number of channels
            AVEncoderAudioQualityKey: AVAudioQuality.high.rawValue //audio bit rate
        ]
        do{
            //create the AVAudioRecorder instance
            audioRecorder = try AVAudioRecorder(url: audioFileURL, settings: settings)
            audioRecorder?.prepareToRecord()
            print("audio recorder ready")
        } catch {
            print("audio recorder error: \(error.localizedDescription)")
        }
        // Do any additional setup after loading the view.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
